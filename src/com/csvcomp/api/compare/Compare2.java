package com.csvcomp.api.compare;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.csvcomp.api.batch.Batcher2;

public class Compare2 {

	String BatchID;
	Set<String[]> Missing;
	Set<String[]> alfDuplicates;
	int i=0;

	// take batchid
	// take index list
	// go one by one in the list
	// for each index get the docid from the cmod
	// send batchid and doc id to the alf comparator

	// alf comp
	// receive the docid and the batch id
	// get the indexes using the batchid
	// compare the docid/appid with the relevant indexes in the alfRows

	public int comparator(List<String[]> cmod, List<String[]> alf, Batcher2 b) {
		Missing = new HashSet<>();
		alfDuplicates = new HashSet<>();
		
		for (String[] eachRecord : cmod) {
//			BatchID = eachRecord[0];
//			String DocID = eachRecord[1];
			
			checkInAlf(eachRecord, alf, b);
		}
		int alfMissingCount = Missing.size();
		int alfDuplicatesCount = alfDuplicates.size();
		System.out.println("\nMissing count \t\t\t"+ alfMissingCount);
		System.out.println("Alfresco Duplicates count \t"+ alfDuplicatesCount);
		return alfDuplicatesCount;
	}

	private void checkInAlf(String[] eachRecord, List<String[]> alf, Batcher2 b) {
		
		BatchID = eachRecord[0];
		String DocID = eachRecord[1];
		
		List<Integer> indexList = b.getIndexes(BatchID);
		boolean match;
		int duplicates;
		if (indexList==null){
			Missing.add(eachRecord);
		} else {
			match = false;
			duplicates = -1;
			for (int eachIndex : indexList){
				if (DocID.equals(alf.get(eachIndex)[1])) {
					match = true;
					duplicates++;
				}
			}
			if (match && duplicates>0){
				alfDuplicates.add(eachRecord);
			}
			else if(!match)
			{
				Missing.add(eachRecord);
			}
		}
	}
	
	public Set<String[]> getMissingList(){
		return Missing;
	}
	
	public Set<String[]> getDuplicatesList(){
		return alfDuplicates;
	}
}
