package com.csvcomp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.csvcomp.api.batch.Batcher2;
import com.csvcomp.api.batch.Batcher3;
import com.csvcomp.api.compare.Compare2;
import com.csvcomp.api.compare.Compare4;
import com.csvcomp.api.report.ReportGen;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class Parse {
	
	List<String[]> cmodRows = null;
	List<String[]> alfRows = null;
	Set<String[]> cmodDuplicates = null;
	Set<String[]> alfDuplicates = null;
	Set<String[]> Missing = null;
	int dataType = 0;
	boolean zip = false;
	int alfDupCount=0;
	
	public boolean isZip() {
		return zip;
	}

	void parseFiles(File cmod, File alf, int type) throws ParseException{
		
		this.cmodRows = null;
		this.alfRows = null;
		this.cmodDuplicates = null;
		this.alfDuplicates = null;
		this.Missing = null;
		
		long init = System.nanoTime();
		this.dataType = type;
		System.out.print("Parsing the files...");
		///////
		CsvParserSettings settings = new CsvParserSettings();

		settings.getFormat().setLineSeparator("\n");

		CsvParser parser = new CsvParser(settings);
		///////
		if (type!=1){
			cmodRows = parser.parseAll(cmod.getAbsoluteFile());
		}
		
		alfRows = parser.parseAll(alf.getAbsoluteFile());		
		
		System.out.println("....Done");
		int cmodRecordCount=0;
		if (type!=1){
			cmodRecordCount = cmodRows.size();
		}
		int alfRecordCount = alfRows.size();
		
		String[] temp, tempc;

		if (type == 3){
			for (int i = 1; i < alfRows.size(); i++) {
				temp = new String[2];
				String originalDateStr = alfRows.get(i)[1];
				DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	        	DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
	        	Date originalDate = originalFormat.parse(originalDateStr);
	        	String formattedDateStr = targetFormat.format(originalDate);
	        	temp[0] = alfRows.get(i)[0];
	        	temp[1] = formattedDateStr + "," + alfRows.get(i)[2];
	        	alfRows.set(i, temp);
	        	
	        	
			}
			for (int i = 1; i < cmodRows.size(); i++) {
				tempc = new String[4];
	        	tempc[0] = cmodRows.get(i)[0];
	        	tempc[2] = cmodRows.get(i)[1];
	        	tempc[3] = cmodRows.get(i)[2];
	        	tempc[1] = cmodRows.get(i)[1]+","+cmodRows.get(i)[2];
	        	cmodRows.set(i, tempc);
			}
			
		}
		
		
		String temp1;
		if (type == 4 && alfRecordCount > 1) {
			for (int i=0;i<cmodRows.size();i++){
				temp= new String[2];
				temp1 = cmodRows.get(i)[0].split("@")[0].toUpperCase();
				temp[0] = temp1;
				temp[1] = temp1;
				cmodRows.set(i, temp);
			}
			for (int i=0;i<alfRows.size();i++){
				temp= new String[2];
				temp1 = alfRows.get(i)[0].split("@")[0].toUpperCase();
				temp[0] = temp1;
				temp[1] = temp1;
				alfRows.set(i, temp);
			}
		}
		
		if (type!=1){
			System.out.println("cmod record count " + cmodRecordCount);
		}
		System.out.println("alf record count " + alfRecordCount);

		long endTime = System.nanoTime();
		long duration = (endTime - init);
//		System.out.println("Time to parse (seconds) - " + duration / 1000000000.0);
		
		
		// alf

		int alfdup=-1;
		if (alfRecordCount > 1){
			long startTimeBatch = System.nanoTime();
			int indexAlf = 0;
			// walk through the alf list
			System.out.println("Batching Started");


			
			// batcher2
			Batcher2 b = new Batcher2();
			if (type!=1){
				b = new Batcher2();
				for (String[] eachRecord : alfRows) {
					b.addBatch(eachRecord[0], indexAlf);// pass the batch id and the index to the batcher
					indexAlf++;
				}
			}
			////batcher2
			
			//batcher3

			Batcher3 b3 = new Batcher3();
			if(type==1){
				b3 = new Batcher3();
				for (String[] eachRecord : alfRows) {
					b3.addBatch(eachRecord[0], eachRecord[1]);// pass the batch id and the docID to the batcher
					indexAlf++;
				}
				alfRows = null;
//				alfDuplicates = b3.getDuplicates();
//				
//				if(!alfDuplicates.isEmpty()){
//					alfdup = alfDuplicates.size();
//				}
//				
//				System.out.println("\nAlfresco Duplicates count \t"+ alfdup);
			}
			
/*			Batcher3 b4 = null;
			if (type!=1){
				b4 = new Batcher3();
				for (String[] eachRecord : cmodRows) {
					b4.addBatch(eachRecord[0], eachRecord[1]);// pass the batch id and the docID to the batcher
					indexAlf++;
				}
			//cmodRows = null;
			}
*/
			////batcher3
			
		
		
			System.out.println("\nBatching finished");
			endTime = System.nanoTime();
			duration = (endTime - startTimeBatch);
//			System.out.println("Time to alfBatch (seconds) - " + duration / 1000000000.0);
		
			int numOfAlfBatches=0;
			if(type==1){
				numOfAlfBatches = b3.getBatches().size(); // changed b3 to b
			}else{
				numOfAlfBatches = b.getBatches().size(); // changed b3 to b
			}
			System.out.println("Alfresco Batch Count - " + numOfAlfBatches);

			System.out.println("Start Comparing");

			
			
			if (type==1){
				Compare4 comp4 = new Compare4();
				BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader(cmod));
					String line = null;
					String previousLine = "prevLine";
					cmodDuplicates = new HashSet<>();
					while ((line = reader.readLine()) != null) {
						String[] record = line.trim().split(",");
						
						if (line.equals(previousLine)) {
							cmodDuplicates.add(record);
						}
						previousLine = line;
						
						comp4.comparator4(b3, record);
					}
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				comp4.report();

				
				alfDuplicates = b3.getDuplicates();
				
				if(!alfDuplicates.isEmpty()){
					alfdup = alfDuplicates.size();
				}else{
					alfdup = 0;
				}
				
				System.out.println("Alfresco Duplicates count \t"+ alfdup);
				this.alfDuplicates = b3.getDuplicates();
				
//				alfdup = comp4.getAlfDupSize();
				
				b3 = null;
/*
				cmodRows = parser.parseAll(cmod.getAbsoluteFile());

				cmodDuplicates = new HashSet<>();
				String previousRecord = "prevRec";
				for (String[] eachRecord : cmodRows) {
					eachRecord[0] = eachRecord[0] + "," + eachRecord[1];
					if (eachRecord[0].equals(previousRecord)) {
						cmodDuplicates.add(eachRecord);
					}
					previousRecord = eachRecord[0];
				}
*/

				int numOfCMODduplicates = cmodDuplicates.size();
				System.out.println("CMOD Duplicates count \t\t" + numOfCMODduplicates+"\n\n");

//				cmodRows = null;
				
				this.Missing = comp4.getMissingList();
//				this.alfDuplicates = comp4.getDuplicatesList();
				
				
			}
			
			if(type!=1){
		//		Compare3 comp3 = new Compare3();

		//		alfdup = comp3.comparator3(b3, b4);

			// compare 2
			Long startTime3 = System.nanoTime();

			Compare2 comp2 = new Compare2();

			alfdup = comp2.comparator(cmodRows, alfRows, b);

				this.Missing = comp2.getMissingList();
				this.alfDuplicates = comp2.getDuplicatesList();
			}
			} else {
				System.out.println("No Alfresco Records Found");
				System.out.println("Missing count \t\t\tNA");
				System.out.println("Alfresco Duplicates count \t\tNA");
				// to remove duplicates
				// Set<String[]> hs = new HashSet<>();
				// List<String[]> cmodRows1 = new ArrayList<>();
				// hs.addAll(cmodRows);
				// hs.remove(cmodRows.get(0));
				// cmodRows1.clear();
				// cmodRows1.addAll(hs);
				// this.Missing = cmodRows1;
				this.Missing = (Set<String[]>) cmodRows;
			}
		
		//cmod dup
		
		
		if(type!=1){
			cmodDuplicates = new HashSet<>();
			
			
			Batcher3 b4 = null;
			
			b4 = new Batcher3();
			for (String[] eachRecord : cmodRows) {
				b4.addBatch(eachRecord[0], eachRecord[1]);// pass the batch id and the docID to the batcher
			}
			
			cmodRows = null;
			
			cmodDuplicates = b4.getDuplicates();
			
			/*
			String previousRecord = "prevRec";
			for (String[] eachRecord : cmodRows) {
//				eachRecord[0] = eachRecord[0] + "," + eachRecord[1];
				if (previousRecord.equals(eachRecord[0] + "," + eachRecord[1])) {
					cmodDuplicates.add(eachRecord);
				}
				previousRecord = eachRecord[0] + "," + eachRecord[1];
			}
		*/
		int numOfCMODduplicates = cmodDuplicates.size();

		System.out.println("CMOD Duplicates count \t\t" + numOfCMODduplicates);
		}
		if(Missing.size() == 0 && alfdup == 0){
			System.out.println("\n-------------------------------------\nRecord count difference " + (cmodRecordCount - alfRecordCount));
			zip = true;
			System.out.println("-------------------------------------");
		}
		}
	
//	List<String[]> getRows(boolean a){
//		if(a)
//			return cmodRows;
//		else
//			return alfRows;
//	}
	
	void repGen(String customerName, Path output){
		
		ReportGen repGen = new ReportGen(customerName, output);
		if (!Files.exists(output)) {
			try {
				Files.createDirectory(output);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Files not created");
				e.printStackTrace();
			}
		}

		try {
			
			if(alfDuplicates != null && alfDuplicates.size()>0){
				repGen.createCSV(alfDuplicates, 10, output, dataType);}
			if(Missing.size()>0){
				repGen.createCSV(Missing, 00, output, dataType);}
			if(cmodDuplicates.size()>0){
				repGen.createCSV(cmodDuplicates, 11, output, dataType);}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("-------------------------------------\n");
	}
	
	
}
