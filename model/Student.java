package model;

public class Student {

	private String name;
	
	private String username;
	
	private double score;
	
	public Student() {
		
	}
	
	public Student(String name, String username, double score) {
		this.name = name;
		this.username = username;
		this.score = score;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public double getScore() {
		return score;
	}
	
	public void print() {
		System.out.print("Name: " + name + " Username: " + username + " Score: " + score);
	}
}
