package com.example.rtlist;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<Movie> {
	
	ArrayList<Movie> movies;

	public MovieAdapter(Context context, int resource, ArrayList<Movie> movie) {
		super(context, resource, movie);
		this.movies = movie;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// If the view is null then inflate it.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, null);
		}

		Movie i = movies.get(position);
		if (i!= null) {
			TextView movieTitle = (TextView) v.findViewById(R.id.title);
			TextView movieDate = (TextView) v.findViewById(R.id.date);
			TextView movieRating = (TextView) v.findViewById(R.id.rating);
			TextView movieReview = (TextView) v.findViewById(R.id.review);
			
			ImageView moviePicture = (ImageView) v.findViewById(R.id.icon);
			ImageView ratingBarBackground = (ImageView) v.findViewById(R.id.rectimage);
			ImageView ratingBar = (ImageView) v.findViewById(R.id.ratingBar);
			ImageView ratingBarNegative = (ImageView) v.findViewById(R.id.ratingBarNegative);
		
			movieTitle.setText(i.getTitle());
			movieDate.setText(i.getDate());

			movieRating.setText("Rated: " + i.getRating());
			
			if (!i.getReview().equals("-1")) {
				movieReview.setText("Critics Review it: " + i.getReview());
				//Rating bar:
				ratingBar.setVisibility(View.VISIBLE);
				ratingBarBackground.setVisibility(View.VISIBLE);
				ratingBarNegative.setVisibility(View.VISIBLE);
				int myRating = Integer.parseInt(i.getReview());
				if (myRating > 50) {
					ratingBar.setBackgroundColor(Color.GREEN);
				} else {
					ratingBar.setBackgroundColor(Color.RED);
				}
				//find the size of the black bar
				int barWidth = myRating*2;
				
				//This is a fix to the bar not being rendered to proper size for list items later in the list. This solution seems very backwards
				//TODO see if there is a better way to this solution (may be a binding issue)
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ratingBar.getLayoutParams());
				layoutParams.width = convertDpToPixel(barWidth, getContext());
				layoutParams.setMargins(convertDpToPixel(2, getContext()), convertDpToPixel(2, getContext()), 0, 0);
				
				ratingBar.setLayoutParams(layoutParams);
			} else {
				movieReview.setText("Not reviewed by critics");
				//If there is no rating just remove the bar
				ratingBar.setVisibility(View.INVISIBLE);
				ratingBarBackground.setVisibility(View.INVISIBLE);
				ratingBarNegative.setVisibility(View.INVISIBLE);
			}
			moviePicture.setImageBitmap(i.getPoster());
			
		}
		return v;
	}
	
	public static int convertDpToPixel(int dp, Context context){
		float myFloat = (float)dp;
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = myFloat * (metrics.densityDpi / 160f);
	    return Math.round(px);
	}
		
}