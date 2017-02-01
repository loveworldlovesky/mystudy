package cn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyUtils {
	public static void main(String[] args) {
		testInput();
	}
	
	public static void testInput(){
		try {
			String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
}
