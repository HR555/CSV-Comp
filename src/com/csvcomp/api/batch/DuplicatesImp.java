package com.csvcomp.api.batch;

import java.util.Set;

public class DuplicatesImp implements Duplicates {

	private String BatchID=null;
	private String DocID=null;
	private String[] docs = null;
	
	void addDuplicates(String[] record){
		
		this.BatchID=record[1];
		this.DocID=record[2];
		
		docs = duplicates.get(BatchID);
		
		
	}
	
	Set<String[]> getDuplicates() {
		return duplicates;
	}
	
	int getDuplicatesSize(){
		return duplicatesSize;
	}
	
}
