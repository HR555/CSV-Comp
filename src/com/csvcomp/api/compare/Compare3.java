package com.csvcomp.api.compare;

import java.util.ArrayList;
import java.util.List;

import com.csvcomp.api.batch.Batcher3;

public class Compare3 {

	String BatchID;
	String DocID;
	
	List<String[]> Missing;
	List<String[]> Duplicates;
	
	int comparator3 (Batcher3 alfBatches, Batcher3 cmodBatches){
		Missing = new ArrayList<>();
		Duplicates = new ArrayList<>();
		
		boolean foundBatch=false;
		for (String eachBatch : cmodBatches.getBatches().keySet()){
			List<String> alfDocs = alfBatches.getDocs(eachBatch);
			if (alfDocs != null) {
				foundBatch=true;
				
				boolean foundDoc=false;
				for(String eachDoc : cmodBatches.getDocs(eachBatch)){
					int duplicates=-1;
					for (String Doc : alfDocs) {
						if (eachDoc.equals(Doc)) {
							foundDoc = true;
							duplicates++;
						}
					}
					if (!foundDoc){
						String[] missing = {eachBatch, eachDoc};
						Missing.add(missing);
					}
					if(duplicates>0){
						String[] dup = {eachBatch, eachDoc};
						Duplicates.add(dup);
					}
				}
			}
			if (!foundBatch){
				for (String eachDoc : cmodBatches.getDocs(eachBatch)) {
					String[] missing = {eachBatch, eachDoc};
					Missing.add(missing);
				}
			}
		}
		System.out.println("Processed");
		
		int alfMissingCount = Missing.size();
		int alfDuplicatesCount = Duplicates.size();
		System.out.println("\nMissing count \t\t\t"+ alfMissingCount);
		System.out.println("Alfresco Duplicates count \t"+ alfDuplicatesCount);
		return alfDuplicatesCount;
	}
	
	List<String[]> getMissingList(){
		return Missing;
	}
	
	List<String[]> getDuplicatesList(){
		return Duplicates;
	}
	
}
