package com.csvcomp.api.compare;

import java.util.ArrayList;
import java.util.List;

import com.csvcomp.api.batch.Batch;
import com.csvcomp.api.batch.Batcher;

public class Compare {

	String BatchID;
	List<String[]> Missing = new ArrayList<>();
	int i=0;

	// take batchid
	// take index list
	// go one by one in the list
	// for each index get the docid from the cmod
	// send batchid and doc id to the alf comparator

	// alf comp
	// receive the docid and the batch id
	// get the indexes using the batchid
	// compare the docid with the relevent indexes in the alfRows

	void comparator(List<String[]> cmod, List<String[]> alf, Batcher cmodBatches, Batcher alfBatches) {
		for (Batch eachBatch : cmodBatches.getBatches()) {
			List<String> DocID = new ArrayList<>();
			BatchID = eachBatch.getBatchID();
			for (int eachIndex : eachBatch.getIndexes()) {
//				for (int i = 0; i <= eachIndex; i++) {
//				System.out.println(cmod.get(eachIndex)[1]);
					DocID.add(cmod.get(eachIndex)[1]);
//				}
				
			}
			checkInAlf(BatchID, DocID, alf, alfBatches);
		}
		System.out.println(Missing.size());
	}

	private void checkInAlf(String BatchID, List<String> DocID, List<String[]> alf, Batcher alfBatches) {
		String batch="";
		for (String eachDocID : DocID) {//go through the doc ids
			outerloop:
			for (Batch eachBatch : alfBatches.getBatches()) {//go through the alf batches to find the batch
				if (BatchID.equals(eachBatch.getBatchID())) {//compare batch ID
					batch="found";
					for (int eachIndex : eachBatch.getIndexes()) {
						if (eachDocID.equals(alf.get(eachIndex)[1])) {
							break outerloop;
						}
						
					}
					String[] record={BatchID,eachDocID};
					Missing.add(record);
					System.out.println(Missing.size()+" missing found");//947+1 duplicate
					break outerloop;
				}
				
			}
			if(!batch.equals("found")){
				//batch not found
//				System.out.println("Batchnotfound " +BatchID);
				for (String eachMissingDocID : DocID){
					String[] record={BatchID,eachMissingDocID};
					Missing.add(record);
				}
//				System.out.println(Missing.size()+" missing found");//947+1 duplicate
				break;
			}
		
		}
		
	}

}
