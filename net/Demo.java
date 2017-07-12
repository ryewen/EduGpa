package net;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.cookie.Cookie;

import model.Student;

public class Demo {

	public static void main(String[] args) {
		List<Cookie> cookies = Client.getCookies("20143939", "a19960712", "STU");
		if (cookies != null) {
			System.out.println("OK");
			CountDownLatch latch = new CountDownLatch(4);
			List<Student> students = ScoreGetter.getStudentsFast("20143902", "20144199", cookies, latch);
			try {
				latch.await();
				Iterator<Student> it = students.iterator();
				int i = 0;
				while(it.hasNext()) {
					System.out.print("No. " +  ++ i + " ");
					it.next().print();
					System.out.println();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Fail");
		}
	}
}
