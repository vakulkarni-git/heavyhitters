package com.heapify.subsequence;

import java.lang.Comparable;

public class Subsequence implements Comparable<Subsequence> {
	private String subsequence;
	private Integer count;
	
	public Subsequence(String subsequence, Integer count) {
		this.subsequence = subsequence;
		this.count = count;
	}
	
	public int compareTo(Subsequence target) {
		return this.count.intValue() - target.count.intValue();
	}
	
	@Override 
	public boolean equals(Object obj) { 
		if (obj == this)
			return true;
		
		if (obj == null || obj.getClass() != this.getClass())
			return false;
			
		Subsequence seq = (Subsequence) obj;
		return this.subsequence.equals(seq.subsequence);
	}
	
	@Override
	public int hashCode() {
		return subsequence.hashCode();
	}
	
	@Override
	public String toString() {
		return subsequence;
	}
}