package com.heapify.filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputFilter {
	private BufferedReader reader=null;
	private String key="";
	
	public InputFilter(BufferedReader reader) {
		if (reader == null)	// throw exception
			;
		this.reader = reader;
	}
	
	private String nextBufferFull() throws IOException, NullPointerException {
		String line;
		line = reader.readLine();
		return line;
	}
	
	public synchronized String[] next() throws IOException {
		String line = this.nextBufferFull();
		if (line == null)
			return null;
			
		String value = line;
		int index = line.indexOf("=");
		
		if (index != -1) {
			key = line.substring(0, index);
			value = line.substring(index+1);
		} else if (key.equals("")) {
			// throw format exception
		}
		
		String [] classified = new String [2];
		classified[0] = key;
		classified[1] = value;
		
		return classified;
	}
/*	
	public void process(String category) throws IOException {
		String patternString = "[^" + category + "]+[" + category + "]+";
		System.out.println(patternString);
		Pattern pattern = Pattern.compile(patternString);
		
		String[] candidate = this.next();
		while (candidate != null) {
			System.out.println("Candidate : " + candidate[1]);
			System.out.println("Patterns");
			Matcher matcher = pattern.matcher(candidate[1]);
			while (matcher.find()) {
				System.out.println(matcher.group());
			}
			
			candidate = this.next();
		}
	}
	
	public static void main(String [] args) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(
						new FileReader("input.txt"));
			InputFilter filter = new InputFilter(br);
			filter.process("D");
		} catch (IOException ioe) {}
		  finally {
			try {
				if (br != null) 
					br.close();
			} catch(IOException ioe) {}
		  }
	}
*/
}