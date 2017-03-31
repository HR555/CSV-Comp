package com.csvcomp.api.batch;

import java.util.ArrayList;
import java.util.List;

public class Batcher {

	List<Batch> batches = new ArrayList<>();
	
	public void addBatch(String batchID, int index) {
		int batchIndex=find(batchID);
		if (batchIndex>-1) {
			batches.get(batchIndex).addIndex(index);//add index to the batch
		}
		else {
			Batch batch = new Batch();//create new batch
			batch.setBatchID(batchID);//set batch id
			batch.addIndex(index);//add index to the batch
			batches.add(batch);//add the batch to the batches list
		}
	}

	public int find(String batchID) {
		int batchIndex = 0;
		for (Batch batch : batches) {
			if (batch.getBatchID().equals(batchID)) {//batch found
				return batchIndex;
			}
			batchIndex++;
		}
		return -1;//no batch found
	}
	
	public List<Batch> getBatches(){
		return batches;
	}

}
