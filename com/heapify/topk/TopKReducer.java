package com.heapify.topk;

import java.util.concurrent.PriorityBlockingQueue;
import com.heapify.subsequence.Subsequence;

public class TopKReducer {
	private PriorityBlockingQueue<Subsequence> topK=null;
	private int k=0;
	
	public TopKReducer(int k) {
		if (k > 0) {
			this.k = k;
			topK = new PriorityBlockingQueue<Subsequence>(k);
		}
	}
	
	public TopKReducer(TopKReducer topKReducer, int l) {
		if (l > 0) {
			topK = new PriorityBlockingQueue<Subsequence>(topKReducer.topK);
			k = topKReducer.k;
			
			if (k == l)
				return;
				
			if (k < l)
				k = l;
			else {
				while (k > l) {
					topK.poll();
					k--;
				}
			}
		}
	}
	
	public void add(Subsequence candidate) {
		if (k == 0)
			return;
			
		if (topK.remove(candidate) || topK.size() < k) {
			topK.add(candidate);
			return;
		}
		
		Subsequence min = topK.peek();
		if (candidate.compareTo(min) > 0) {
			topK.poll();
			topK.add(candidate);
		}
	}
	
	public Subsequence[] get() {
		if (topK == null)
			return null;
			
		Subsequence[] template = new Subsequence[1];
		Subsequence[] all = topK.toArray(template);
		return all;
	}
}	