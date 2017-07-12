package net;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import model.Student;

public class ScoreGetter {
	
	private static final String SCORE_URL = "http://202.202.1.176:8080/XSCJ/f_xsgrcj_rpt.aspx";
	
	private static final String NAME_URL_LEFT = "http://202.202.1.176:8080/XSCJ/Private/list_XS.aspx?id=";
	
	private static final String NAME_URL_RIGHT = "&s=&wd=&vp=undefined";
	
	public static List<Student> getStudentsFast(String startUsername, String endUsername, List<Cookie> cookies, CountDownLatch latch) {
		List<Student> students = new ArrayList<Student>();
		int start = Integer.valueOf(startUsername);
		int end = Integer.valueOf(endUsername);
		if (start > end) return students;
		if (end - start < latch.getCount() - 1) {
			students = getStudents(startUsername, endUsername, cookies);
		} else {
			int index = (end - start) / ((int) latch.getCount());
			int i = 0;
			for (i = 0; i < latch.getCount() - 1; ++ i) {
				new InsertStudentThread(students, String.valueOf(start + index * i), String.valueOf(start + index * (i + 1) - 1), cookies, latch).start();
			}
			new InsertStudentThread(students, String.valueOf(start + index * i), endUsername, cookies, latch).start();
		}
		return students;
	}
	
	public static void insertStudents(List<Student> students, String startUsername, String endUsername, List<Cookie> cookies) {
		int start = Integer.valueOf(startUsername);
		int end = Integer.valueOf(endUsername);
		if (start > end) return;
		for (int i = start; i <= end; ++ i) {
			String username = String.valueOf(i);
			System.out.print(username + "...");
			String name = getName(username, cookies);
			System.out.print(name + "...");
			double score = getScore(username, cookies);
			System.out.println(score + "...");
			if (name != null && score != -1) insertStudent(students, new Student(name, username, score));
		}
	}
	
	public static List<Student> getStudents(String startUsername, String endUsername, List<Cookie> cookies) {
		List<Student> students = new ArrayList<Student>();
		int start = Integer.valueOf(startUsername);
		int end = Integer.valueOf(endUsername);
		if (start > end) return students;
		for (int i = start; i <= end; ++ i) {
			String username = String.valueOf(i);
			System.out.print(username + "...");
			String name = getName(username, cookies);
			System.out.print(name + "...");
			double score = getScore(username, cookies);
			System.out.println(score + "...");
			if (name != null && score != -1) insertStudent(students, new Student(name, username, score));
		}
		return students;
	}
	
	private synchronized static void insertStudent(List<Student> students, Student student) {
		if (students == null || student == null) return;
		if (students.size() == 0) {
			students.add(student);
		} else {
			Iterator<Student> it = students.iterator();
			int i = 0;
			while (it.hasNext()) {
				if (student.getScore() > it.next().getScore()) {
					break;
				}
				++ i;
			}
			students.add(i, student);
		}
	}
	
	public static double getScore(String username, List<Cookie> cookies) {
		double score = 0;
		String html = null;
		html = Client.getHtmlByPost(SCORE_URL, getScoreParams(username), cookies);
		if (html != null)
			score = getScoreFromHtml(html);
		else
			score = -1;
		return score;
	}

	private static double getScoreFromHtml(String html) {
		String scoreStr = "";
		int time = 0;
		for (int i = html.length() - 1 - 4; i >= 0; -- i) {
			if (html.charAt(i) == '<' && html.charAt(i + 1) == '/' && html.charAt(i + 2) == 't' && html.charAt(i + 3) == 'd' && html.charAt(i + 4) == '>') ++ time;
			if (time == 3) {
				scoreStr += html.charAt(i - 4);
				scoreStr += html.charAt(i - 3);
				scoreStr += html.charAt(i - 2);
				scoreStr += html.charAt(i - 1);
				break;
			}
		}
		return Double.valueOf(scoreStr);
	}
	
	public static String getName(String username, List<Cookie> cookies) {
		String name = null;
		String html = null;
		html = Client.getHtmlByPost(NAME_URL_LEFT + username + NAME_URL_RIGHT, null, cookies);
		if (html != null) name = html.split("]")[1];
		return name;
	}
	
	private static List<NameValuePair> getScoreParams(String username) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("btn_search", "????"));
		params.add(new BasicNameValuePair("SelXNXQ", "0"));
		params.add(new BasicNameValuePair("sel_xs", username));
		params.add(new BasicNameValuePair("SJ", "1"));
		params.add(new BasicNameValuePair("txt_xm", "0001168"));
		params.add(new BasicNameValuePair("txt_xs", username));
		params.add(new BasicNameValuePair("zfx_flag", "0"));
		return params;
	}
}
