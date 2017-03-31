package com.csvcomp.api.compare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.csvcomp.api.batch.Batcher3;

public class Compare4 {
	
	String BatchID;
	String DocID;
	
	Set<String[]> Missing = new HashSet<>();
	Set<String[]> Duplicates = new HashSet<>();

	public void comparator4(Batcher3 alfBatches, String[] record) {
		
		String batchID = record[0];
		String docID = record[1];
		
		boolean foundBatch=false;
		
		List<String> alfDocs = alfBatches.getDocs(batchID);
		
		if (alfDocs != null) {
			foundBatch=true;
			int duplicates=-1;
			boolean foundDoc=false;
			for (String Doc : alfDocs) {
				if (docID.equals(Doc)) {
					foundDoc = true;
					duplicates++;
				}
				
			}
			if (!foundDoc){
//				record[0] = record[0]+","+record[1];
				Missing.add(record);
			}
			if(foundDoc && duplicates>0){
//				record[0] = record[0]+","+record[1];
				Duplicates.add(record);
			}

		}
		if (!foundBatch){
			Missing.add(record);
		}
		
		
	}
	
	public void report(){
		System.out.println("Processed");
		
		int alfMissingCount = Missing.size();
		int alfDuplicatesCount = Duplicates.size();
		System.out.println("\nMissing count \t\t\t"+ alfMissingCount);
//		System.out.println("Alfresco Duplicates count \t"+ alfDuplicatesCount);
		
	/*	for (int i=0;i<alfMissingCount;i++){
			System.out.println(Missing.get(i)[0] + "," + Missing.get(i)[1]);
		}*/
		
	}
	
	public Set<String[]> getMissingList(){
		return Missing;
	}
	
	Set<String[]> getDuplicatesList(){
		return Duplicates;
	}

	int getAlfDupSize(){
		return Duplicates.size();
	}
}
