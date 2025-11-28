package kr.or.ddit.controller;

import java.io.FileReader;
import java.io.IOException;

public class ReadingMyPageTest {
	public static void main(String[] args) throws IOException {
		
		//읽기 객체생성 -> 읽기 -> [종료는 옵션]
		FileReader fr = new FileReader("src/test/java2/Test01.java");
		char[] ch = new char[10 ];
		while(true) {
			int read = fr.read(ch);
			if(read==-1) {break;}
			String string  =new String(ch,0,read);
			System.out.println(string);
		}
		fr.close();
	}
	
	
	
}
