package com.example.rtlist;

import android.graphics.Bitmap;

// Movie objects should only be created off the main thread
public class Movie {

	private String title;
	private String date;
	private String URL;
	private String ID;
	private Bitmap poster;
	
	public Movie (String title, String date, String url, String ID) {
		setTitle(title);
		setDate(date);
		setURL(url);
		setID(ID);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public Bitmap getPoster() {
		return poster;
	}

	public void setPoster(Bitmap poster) {
		this.poster = poster;
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}
}
