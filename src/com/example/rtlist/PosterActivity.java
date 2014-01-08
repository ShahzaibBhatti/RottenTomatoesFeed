package com.example.rtlist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;


public class PosterActivity extends Activity { 
	
	ImageView moviePicture;
	private GestureDetector gestureDetector;
	
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
        
        
        moviePicture.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
              finish();
              return true;
            }
        });
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
	
}
