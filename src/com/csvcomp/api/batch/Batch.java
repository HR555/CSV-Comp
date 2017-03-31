package com.csvcomp.api.batch;

import java.util.ArrayList;
import java.util.List;

public class Batch {

	private List<Integer> indexes=new ArrayList<>();
	private String batchID;
	
	public String getBatchID() {
		return batchID;
	}
	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}
	
	public void addIndex(int index){
		indexes.add(index);
	}
	
	public List<Integer> getIndexes(){
		return indexes;
	}
}
