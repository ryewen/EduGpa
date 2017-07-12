package net;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import org.apache.http.cookie.Cookie;

import model.Student;

public class Demo {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("����ѧ��:");
		String username = sc.nextLine();
		System.out.println("��������:");
		String password = sc.nextLine();
		List<Cookie> cookies = Client.getCookies(username, password, "STU");
		if (cookies != null) {
			System.out.println("OK");
			while (true) {
				CountDownLatch latch = new CountDownLatch(4);
				System.out.println("������ʼѧ��(����q�˳�):");
				String start = sc.nextLine();
				if (start.equals("q")) break;
				System.out.println("������ֹѧ��:");
				String end = sc.nextLine();
				List<Student> students = ScoreGetter.getStudentsFast(start, end, cookies, latch); //20143902-20144199
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
			}
		} else {
			System.out.println("Fail");
		}
	}
}
