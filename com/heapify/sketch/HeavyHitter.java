package com.heapify.sketch;

import java.util.Hashtable;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.Iterator;
import java.util.Set;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.Runtime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.Runnable;
import java.util.concurrent.CountDownLatch;

import com.heapify.filter.InputFilter;
import com.heapify.subsequence.Subsequence;
import com.heapify.subsequence.SubsequenceMapper;
import com.heapify.topk.TopKReducer;

public class HeavyHitter {
	private Hashtable<String, TopKReducer> reducers;
	private TopKReducer globalReducer;
	private String file;
	private String category;
	private int m;
	private int n;
	private int k;
	
	private BufferedReader reader=null;
	private InputFilter filter=null;
	private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
	private static final ExecutorService exec = Executors.newFixedThreadPool(NUM_THREADS);
	private CountDownLatch latch=null;
	
	public HeavyHitter(String file, String category, int m, int n) {
		this.file = file;
		this.category = category;
		this.m = m;
		this.n = n;
		if (m < 0 || n < 0) {
			System.out.println("HeavyHitter: Values for m and n should be positive");
			System.exit(1);
		}
		
		this.k = m > n ? m : n;
		reducers = new Hashtable<String, TopKReducer>();
		
		try {
			reader = new BufferedReader(
						new FileReader(file));
			filter = new InputFilter(reader);
		} catch (IOException ioe) {
			System.out.println(ioe.toString());
			System.exit(1);
		}
	}
	
	public void processFile() {
		latch = new CountDownLatch(NUM_THREADS);
		
		for (int i=0; i < NUM_THREADS; i++) {
			Runnable task = new Runnable() {
				public void run() {
					processLineByLine();
					latch.countDown();
				}
			};
			exec.execute(task);
		}
		
		try {
			latch.await();
			exec.shutdown();
			
			globalReducer = new TopKReducer(m);
			Set<String> keys = reducers.keySet();
		
			for (String key : keys) {
				TopKReducer reducer = reducers.get(key);
				Subsequence[] topK = reducer.get();
				for (int i=0; i < topK.length; i++)
					globalReducer.add(topK[i]);
			}
			
			if (k > n) {
				for (String key : keys) {
					TopKReducer reducer = reducers.get(key);
					reducers.put(key, new TopKReducer(reducer, n));
				}
			}
		} catch (InterruptedException ie) {}
	}
	
	private void processLineByLine() {
		String patternString = "[^" + category + "]+[" + category + "]+";
		Pattern pattern = Pattern.compile(patternString);
		
		try {
			String[] candidate = filter.next();
			while (candidate != null) {
				SubsequenceMapper mapper = new SubsequenceMapper();
					
				Matcher matcher = pattern.matcher(candidate[1]);
				while (matcher.find()) {
					mapper.add(matcher.group());
				}
				
				TopKReducer reducer = new TopKReducer(k);
				Vector<Subsequence> seqs = mapper.get();
				Iterator<Subsequence> iter = seqs.iterator();
				while (iter.hasNext())
					reducer.add(iter.next());
				reducers.put(candidate[0], reducer);

				candidate = filter.next();
			}
		} catch (IOException ioe) {}
		  catch (NullPointerException npe) {
			System.out.println("Input file does not exist");
		  }
		  finally {
			try {
				if (reader != null) 
					reader.close();
			} catch(IOException ioe) {}
		  }
	}
	
	public void output() {
		Set<String> keys = reducers.keySet();
			
		for (String key : keys) {
			TopKReducer reducer = reducers.get(key);
			Subsequence[] topK = reducer.get();
			System.out.println(key);
			try {
				for (int i=topK.length-1; i >= 0; i--)
					System.out.println("\t" + topK[i].toString());
			} catch (NullPointerException npe) {}
		}
		
		System.out.println("Across all IPs");
		Subsequence[] topK = globalReducer.get();
		try {
		   for (int i=topK.length-1; i >= 0; i--)
			 System.out.println("\t" + topK[i].toString());
		} catch (NullPointerException npe) {}
	}
}