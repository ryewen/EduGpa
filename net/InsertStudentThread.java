package net;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.cookie.Cookie;

import model.Student;

public class InsertStudentThread extends Thread {

	private List<Student> students;
	
	private String startUsername;
	
	private String endUsername;
	
	private List<Cookie> cookies;
	
	private CountDownLatch latch;
	
	public InsertStudentThread(List<Student> students, String startUsername, String endUsername, List<Cookie> cookies, CountDownLatch latch) {
		this.students = students;
		this.startUsername = startUsername;
		this.endUsername = endUsername;
		this.cookies = cookies;
		this.latch = latch;
	}
	
	public void run() {
		ScoreGetter.insertStudents(students, startUsername, endUsername, cookies);
		latch.countDown();
	}
}
