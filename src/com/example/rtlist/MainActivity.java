package com.example.rtlist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	String myJSON;
    int totalMovies = 0;
    int page = 1; //starting page
    boolean loading = false;

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
                	search(searchBar.getText().toString().replace(" ", "+"));
                }
                return false;
            }
        });
        
        myAdapter = new MovieAdapter(this, R.layout.list_item, myMovies);
        setListAdapter(myAdapter);
        
        ListView listView = getListView();
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(),
						myMovies.get(position).getTitle() +", ID: " + myMovies.get(position).getID(), Toast.LENGTH_SHORT)
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
                	//execute another search without removing previous items
                	String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=" +
           				 searchBar.getText().toString().replace(" ", "+") +
           				 "&page_limit=10&page=" + ++page + "&apikey=smm4764mtazphgk62e5zut7y";
                	new SearchMovies().execute(url);
                }
            }

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
			}
        });
        
        //Start off with a default search
        search("iron+man");
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
	        		JSONObject pictureJSON = new JSONObject(temp.getString("posters"));
	        		String tPic = pictureJSON.getString("profile");
		            myMovies.add(new Movie(tTitle, tYear, tPic, tID));
	        	}
	            myAdapter.notifyDataSetChanged();
	            for (int i = 0; i < myMovies.size(); i++) {
	            	new getBitmap().execute(myMovies.get(i));
	            }
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
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return null;
		}
		
		@Override
	    protected void onPostExecute(Movie movie) {
	        myAdapter.notifyDataSetChanged();
	    }
    }
    
    public void search(String q) {
    	myMovies.clear();
        page = 1;
    	String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=" +
    				 searchBar.getText().toString().replace(" ", "+") +
    				 "&page_limit=10&page=1&apikey=smm4764mtazphgk62e5zut7y";
    	new SearchMovies().execute(url);
    }
}
