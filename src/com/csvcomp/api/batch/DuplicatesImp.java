package com.csvcomp.api.batch;

import com.google.common.collect.Multimap;

public class DuplicatesImp implements Duplicates {

	private String BatchID=null;
	private String DocID=null;
	
	void addDuplicates(String[] record){
		
		this.BatchID=record[1];
		this.DocID=record[2];
		
		
		duplicates.put(BatchID, DocID);
		
	}
	
	Multimap<String,String> getDuplicates() {
		return duplicates;
	}
	
	int getDuplicatesSize(){
		return duplicatesSize;
	}
	
}
