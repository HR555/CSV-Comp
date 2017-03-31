package com.csvcomp.api.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Batcher3 {

	HashMap<String,List<String>> batches = new HashMap<>();
	HashSet<String[]> duplicates = new HashSet<>();
	
	DuplicatesImp allDuplicates = new DuplicatesImp();
	
	public void addBatch(String batchID, String docID) {

		List<String> docs = new ArrayList<>();
		
		if(batches.containsKey(batchID)){
			docs = batches.get(batchID);
		}
		if (docs.contains(docID)){
			String[] record = {batchID , docID};
			duplicates.add(record);
		}else{//adds only if it is not a duplicate
			docs.add(docID);
			batches.put(batchID, docs);
		}
		
	}

	
	public HashMap<String, List<String>> getBatches(){
		return batches;
	}
	
	public List<String> getDocs(String BatchID){
		return batches.get(BatchID);
	}

	public HashSet<String[]> getDuplicates() {
		return duplicates;
	}
}
