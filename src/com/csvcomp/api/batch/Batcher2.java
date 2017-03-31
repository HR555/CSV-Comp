package com.csvcomp.api.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Batcher2 {

	HashMap<String,List<Integer>> batches = new HashMap<>();
	
	
	public void addBatch(String batchID, int index) {

		List<Integer> indexes = new ArrayList<>();
		
		if(batches.containsKey(batchID)){
			indexes = batches.get(batchID);
		}
		indexes.add(index);
		batches.put(batchID, indexes);
		
	}

	
	public HashMap<String, List<Integer>> getBatches(){
		return batches;
	}
	
	public List<Integer> getIndexes(String BatchID){
		return batches.get(BatchID);
	}
}
