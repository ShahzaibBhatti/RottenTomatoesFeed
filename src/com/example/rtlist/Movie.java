package com.example.rtlist;

import android.graphics.Bitmap;

// Movie objects should only be created off the main thread
public class Movie {

	private String title;
	private String date;
	private String URL;
	private String originalPictureUrl;
	private String ID;
	private String creationTime;
	private String rating;
	private String review;
	private Bitmap poster;
	private boolean loaded;
	
	public Movie (String title, String date, String url, String ID, String rating, String review, String creationTime, String originalPictureUrl) {
		setTitle(title);
		setDate(date);
		setURL(url);
		setID(ID); //Currently unused
		setCreationTime(creationTime);
		setLoaded(false);
		setOriginalPictureUrl(originalPictureUrl);
		setRating(rating);
		setReview(review);
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

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public String getOriginalPictureUrl() {
		return originalPictureUrl;
	}

	public void setOriginalPictureUrl(String originalPictureUrl) {
		this.originalPictureUrl = originalPictureUrl;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}
