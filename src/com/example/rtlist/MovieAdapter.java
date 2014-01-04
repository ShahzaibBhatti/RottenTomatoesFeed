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
			ImageView moviePicture = (ImageView) v.findViewById(R.id.icon);
		
			movieTitle.setText(i.getTitle());
			movieDate.setText(i.getDate());
			moviePicture.setImageBitmap(i.getPoster());
		}
		return v;
	}
		
}