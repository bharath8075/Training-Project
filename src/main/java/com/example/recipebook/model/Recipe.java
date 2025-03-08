package com.example.recipebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String title;
	
	@Column(length = 1000)
	private String ingridients;
	
	@Column(length = 1000)
	private String procedures;
	private String cookingTime;
	private String difficulty;
	
	@Column(length = 1000)
	private String about;
	private int viewCount;
	private boolean isEnabled=false;
	
	@Lob
	@Column(length = 100000)
	private byte[] image;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private User user;
	
	public Recipe() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Recipe(
			long id, String title, String ingridients, String procedures,
			String cookingTime, String about, String difficulty, User user, int viewCount, boolean isEnabled) {
		super();
		this.id = id;
		this.title = title;
		this.ingridients = ingridients;
		this.procedures = procedures;
		this.cookingTime = cookingTime;
		this.difficulty = difficulty;
		this.about = about;
		this.user = user;
		this.viewCount = viewCount;
		this.isEnabled= isEnabled;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIngridients() {
		return ingridients;
	}

	public void setIngridients(String ingridients) {
		this.ingridients = ingridients;
	}

	public String getProcedures() {
		return procedures;
	}

	public void setProcedures(String procedures) {
		this.procedures = procedures;
	}

	public String getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(String cookingTime) {
		this.cookingTime = cookingTime;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	
	
}
