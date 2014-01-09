package com.example.rtlist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	// declare class variables
	private ArrayList<Movie> myMovies = new ArrayList<Movie>();
	private MovieAdapter myAdapter;
	private EditText searchBar;
	
	final String FIRST_SEARCH = "iron_man";
	
	
    int totalMovies = 0; //Total number of movies rotten tomatoes has on the given search parameter.
    int page = 1; //starting page
    boolean loading = false; //true when program is grabbing the movie list JSON
    
    // Variables associated with the custom made queue for loading bitmaps
    final int NUM_OF_ASYNC_IMAGE_DOWNLOADS = 3; //Total number of bitmaps that you're allowing to be downloading at once
    int asyncImageDownloads = 0; //Number of bitmap currently being downloaded
    private ArrayList<Movie> bitmapQueue = new ArrayList<Movie>(); //list of movies with bitmaps to download

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        searchBar = (EditText) findViewById(R.id.inputSearch);
        searchBar.setOnEditorActionListener(new OnEditorActionListener() {
            @SuppressLint("NewApi")
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                	myMovies.clear();
                	bitmapQueue.clear();
                    page = 1;
                	search(searchBar.getText().toString().replace(" ", "+")); //Rotten tomatoes way in URLS to deal with spaces
                }
                return false;
            }
        });
        
        myAdapter = new MovieAdapter(this, R.layout.list_item, myMovies);
        setListAdapter(myAdapter);
        
        ListView listView = getListView();
        //When the user clicks an item, present the time
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(),
						myMovies.get(position).getTitle()
						+ ", Item Creation Time: " + myMovies.get(position).getCreationTime(), Toast.LENGTH_SHORT)
					    .show();
			}  
        });
        
        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, 
                int visibleItemCount, int totalItemCount) {
                //Check if the last view is visible
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !loading && totalMovies > myMovies.size()) {
                	loading = true;
                	Toast.makeText(getApplicationContext(), "loading", Toast.LENGTH_SHORT).show();
                	//execute another search on the next page without removing previous items
                	page++;
                	search(searchBar.getText().toString().replace(" ", "+"));
                }
            }

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
			}
        });
        
        this.getListView().setLongClickable(true);
        //On long press, start a new activity with just the poster
        this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
             public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
            	 if (myMovies.get(position).isLoaded()) {
            		 Intent intent = new Intent(getBaseContext(), PosterActivity.class);
            		 intent.putExtra("POSTER_URL", myMovies.get(position).getOriginalPictureUrl());
            		 startActivity(intent);
            	 }
                 return true;
             }
         });
        
        //Start off with a default search
        myMovies.clear();
        page = 1;
        if (searchBar.getText().toString().equals("".trim())) { 
        	search("FIRST_SEARCH");
        } else {
        	search(searchBar.getText().toString().replace(" ", "+"));
        }
    }

    //Clears the list, does the requested search and outputs the results
    private class SearchMovies extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
            String result = "";
        	loading = true;
            try {
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
     
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                result = EntityUtils.toString(httpResponse.getEntity());
     
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        protected void onPostExecute(String result) {
        	 JSONObject myJSON;
			try {
				myJSON = new JSONObject(result);
				totalMovies =  myJSON.getInt("total");
	        	JSONArray jArray = myJSON.getJSONArray("movies");
	        	for (int i = 0; i < jArray.length(); i++) {
	        		JSONObject temp = jArray.getJSONObject(i);
	        		String tTitle = temp.getString("title");
	        		String tYear = temp.getString("year");
	        		String tID = temp.getString("id");
	        		//All the posters are embedded into an internal JSON, so grab that
	        		JSONObject pictureJSON = new JSONObject(temp.getString("posters"));
	        		String tPic = pictureJSON.getString("thumbnail");
	        		
	        		String tRating = temp.getString("mpaa_rating");
	        		
	        		//Want to get all the reviews
	        		JSONObject reviewJSON = new JSONObject(temp.getString("ratings"));
	        		String tReview = reviewJSON.getString("critics_score");
	        		
	        		//Store a URL to the original image, do not need to load it now, load when needed
	        		String originalPicture = pictureJSON.getString("original");
	        		
	        		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
	        		String currentDateandTime = sdf.format(new Date());
		            myMovies.add(new Movie(tTitle, tYear, tPic, tID, tRating, tReview, currentDateandTime, originalPicture));
	        	}
	            myAdapter.notifyDataSetChanged();
	            for (int i = 0; i < myMovies.size(); i++) {
	            	bitmapQueue.add(myMovies.get(i));
	            }
	            downloadBitmaps();
	        	loading = false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    private class getBitmap extends AsyncTask<Movie, Void, Movie> {
		@Override
		protected Movie doInBackground(Movie... params) {
			try {
		        URL url = new URL(params[0].getURL());
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        params[0].setPoster(BitmapFactory.decodeStream(input));
		        params[0].setLoaded(true);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return null;
		}
		
		@Override
	    protected void onPostExecute(Movie movie) {
	        myAdapter.notifyDataSetChanged();
			asyncImageDownloads--;
			downloadBitmaps();
	    }
    }
    
    public void search(String q) {
    	String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=" +
  				 searchBar.getText().toString().replace(" ", "+") +
  				 "&page_limit=10&page=" + page + "&apikey=smm4764mtazphgk62e5zut7y";
    	new SearchMovies().execute(url);
    }

	public void downloadBitmaps() {
		while (asyncImageDownloads < NUM_OF_ASYNC_IMAGE_DOWNLOADS && !bitmapQueue.isEmpty()) {
			asyncImageDownloads++;
			new getBitmap().execute(bitmapQueue.remove(0));
		}
		
	}
}
