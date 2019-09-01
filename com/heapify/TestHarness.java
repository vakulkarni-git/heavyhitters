package com.heapify;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.heapify.sketch.HeavyHitter;

public class TestHarness {
	public static void main(String [] args) {
		Properties properties = new Properties();
		String propertyFile = "default.properties";
		String fileName="";
		String category="";
		int m=0;
		int n=0;
		
		try {
			FileInputStream is = new FileInputStream(propertyFile);
			properties.load(is);
			
			try {
				fileName = properties.getProperty("file");
				category = properties.getProperty("category");
				m = Integer.parseInt(properties.getProperty("m"));
				n = Integer.parseInt(properties.getProperty("n"));
			} catch (Exception e) {
				System.out.println("Exception : " + e);
			} finally {
				is.close();
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("Exception : " + fnfe);
			System.exit(1);
		} catch (IOException ioe) {
			System.out.println("Exception : " + ioe);
			System.exit(1);
		}
		
		HeavyHitter heavyHitter = new HeavyHitter(fileName, category, m, n);
		heavyHitter.processFile();
		heavyHitter.output();
		
	}
}