package com.example.rtlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
		
			movieTitle.setText(i.getTitle());
			movieDate.setText(i.getDate());
			
			if (i.getRating().equals("Unrated")) {
				movieRating.setText(i.getRating());
			} else {
				movieRating.setText("Rated:" + i.getRating());
			}
			
			if (!i.getReview().equals("-1")) {
				movieReview.setText("Critics Review it: " + i.getReview());
			} else {
				movieReview.setText("Not reviewed by critics");
			}
			moviePicture.setImageBitmap(i.getPoster());
		}
		return v;
	}
		
}