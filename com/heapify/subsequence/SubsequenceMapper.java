package com.heapify.subsequence;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;

public class SubsequenceMapper {
	private Hashtable<String, Integer> counter;
	
	public SubsequenceMapper() {
		counter = new Hashtable<String, Integer>();
	}
	
	public void add(String subsequence) {
		if (subsequence != null) {
			Integer count = counter.get(subsequence);
			if (count == null) 
				counter.put(subsequence, Integer.valueOf(1));
			else {
				counter.put(subsequence, Integer.valueOf(count.intValue()+1));
			}
		}
	}
	
	public Integer get(String subsequence) {
		if (subsequence != null) {
			return counter.get(subsequence);
		}
		return null;
	}
	
	public Vector<Subsequence> get() {
		Set<String> keys = counter.keySet();
		Vector<Subsequence> seqs = new Vector<Subsequence>();
		
		for (String key : keys) {
			Subsequence seq = new Subsequence(key, counter.get(key));
			seqs.add(seq);
		}
		
		return seqs;
	}
}

