package com.example.rtlist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;


public class PosterActivity extends Activity implements GestureDetector.OnGestureListener { 
	
	ImageView moviePicture;
	final int MIN_SWIPE_DISTANCE = 120;
	final int MIN_SWIPE_VELOCITY = 200;
	int konamiCount;
	boolean inKonamiCode = true;
	//Will be 1 after an up fling
	//2 -> up up
	//3 -> up up down
	//4 -> up up down down
	//5 -> up up down down left
	//6 -> up up down down left right
	//7 -> up up down down left right left
	//8 -> up up down down left right left right
	//9 -> up up down down left right left right tap
	//10 -> up up down down left right left right tap tap
	//11 -> up up down down left right left right tap tap tap (will restart at this point since the code is completed)
	
	private GestureDetectorCompat mDetector;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poster_screen);
        
		moviePicture = (ImageView) findViewById(R.id.poster);

        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
            String posterURL = extras.getString("POSTER_URL");
            new getBitmap().execute(posterURL);
        } else {
        	finish();	
        }
        
        konamiCount = 0;
        mDetector = new GestureDetectorCompat(this,this);
    }
	
	//TODO This code is very similar to the one in main class, make a common method
    private class getBitmap extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			try {
		        URL url = new URL(params[0]);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        return BitmapFactory.decodeStream(input);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return null;
		}
		
		@Override
	    protected void onPostExecute(Bitmap b) {
			moviePicture.setImageBitmap(b);
	    }
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

	@Override
	public boolean onDown(MotionEvent e) {
		if (konamiCount == 8 || konamiCount == 9) {
			konamiCount++;
			inKonamiCode = true;
		} else if (konamiCount == 10) {
			//If they successfully put in the Konami code, open the Rotten Tomatoes Nicolas Cage site
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("http://www.rottentomatoes.com/mobile/celebrity/nicolas_cage/"));
			konamiCount = 0;
			startActivity(i);
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//If you swipe left or right the correct velocity or up, go back
		if ((e2.getX() - e1.getX() > MIN_SWIPE_DISTANCE ||
                e1.getX() - e2.getX() > MIN_SWIPE_DISTANCE)
                && Math.abs(velocityX) > MIN_SWIPE_VELOCITY
                && e1.getY() - e2.getY() > MIN_SWIPE_DISTANCE
                && Math.abs(velocityY) > MIN_SWIPE_VELOCITY) {
			finish();
		} else if (e1.getX() - e2.getX() > MIN_SWIPE_DISTANCE
                && Math.abs(velocityX) > MIN_SWIPE_VELOCITY) {
			//LEFT CASE
			if (konamiCount == 4 || konamiCount == 6) {
        		konamiCount++;
        		inKonamiCode = true;
        	}
			return true;
        } else if (e2.getX() - e1.getX() > MIN_SWIPE_DISTANCE
                && Math.abs(velocityX) > MIN_SWIPE_VELOCITY) {
        	//RIGHT CASE
        	if (konamiCount == 5 || konamiCount == 7) {
        		konamiCount++;
        		inKonamiCode = true;
        	}
			return true;
        }  else if (e1.getY() - e2.getY() > MIN_SWIPE_DISTANCE
                && Math.abs(velocityY) > MIN_SWIPE_VELOCITY) {
        	//UP CASE
        	if (konamiCount == 0 || konamiCount == 1) {
        		konamiCount++;
        		inKonamiCode = true;
        	}
			return true;
        }  else if (e2.getY() - e1.getY() > MIN_SWIPE_DISTANCE
                && Math.abs(velocityY) > MIN_SWIPE_VELOCITY) {
        	//DOWN CASE
        	if (konamiCount == 2 || konamiCount == 3) {
        		konamiCount++;
        		inKonamiCode = true;
        	}
			return true;
        }
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (!inKonamiCode) {
			konamiCount = 0;
		}
		inKonamiCode = false;
		return true;
	}
	
}
