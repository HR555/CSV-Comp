package com.csvcomp.api;

import java.io.File;

import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class comp {

	public static void main(String[] args) {

		CsvParserSettings settings = new CsvParserSettings();
		// the file used in the example uses '\n' as the line separator
		// sequence.
		// the line separator sequence is defined here to ensure systems such as
		// MacOS and Windows
		// are able to process this file correctly (MacOS uses '\r'; and Windows
		// uses '\r\n').
		settings.getFormat().setLineSeparator("\n");

		// creates a CSV parser
		CsvParser parser = new CsvParser(settings);

		// parses all rows in one go.
		// List<String[]> allRows = parser.parseAll(new
		// File("C:\\Users\\hmranasinghe\\Desktop\\Tharanga
		// Tool\\csv-pro\\setup\\input\\cmodOutb\\CustomerDataReport_DRCi_DELTADENTAL_30-9-2016.csv"));
		List<String[]> cmodRows = parser.parseAll(new File(
				"C:\\Users\\hmranasinghe\\Desktop\\Tharanga Tool\\csv-pro\\setup\\input\\cmodOut\\CustomerDataReport_DRCi_PRD_BIOMET_24-1-2017.csv"));
		List<String[]> alfRows = parser.parseAll(new File(
				"C:\\Users\\hmranasinghe\\Desktop\\Tharanga Tool\\csv-pro\\setup\\input\\alfout\\CustomerDataReport_Alfresco_biomet.com_24-1-2017.csv"));
		long startTime = System.nanoTime();
		int i = 0;
		// for (String[] strings : allRows) {
		// for (String s : strings) {
		// System.out.print(i +" : "+ s + " ");
		//
		// }
		// i++;
		// System.out.println();
		// }

		// System.out.println(allRows.size());
		System.out.println("start");
		// duplicates
		// String temp = "batchid";
		// for (String[] s : allRows) {
		// s[i] = s[i]+","+s[i+1];
		// if(s[i].equals(temp)){
		// System.out.println(s[i]);
		// }
		// temp=s[i];
		// }

		int k = 0, p = 0;
		// batchid
		// String x = "X";
		// for (String[] s : alfRows){
		// System.out.println(s[0]);
		// if (!s[0].equals(x))
		// k++;
		// x=s[0];
		// }
		// System.out.println(k);

		String batchID = "";
		List<String[]> Missing = new ArrayList<>();
		for (String[] batchIDs : cmodRows) {
			if (!batchID.equals(batchIDs[0])) {
				batchID = batchIDs[0];

				List<String> CMODdocID = new ArrayList<>();
				for (String[] cmodBatchID : cmodRows) {
					if (cmodBatchID[0].equals(batchID)) {
						CMODdocID.add(cmodBatchID[1]);
					}
				}

				List<String> alfDocID = new ArrayList<>();
				for (String[] alfBatchID : alfRows) {
					if (alfBatchID[0].equals(batchID)) {
						alfDocID.add(alfBatchID[1]);
					}
				}

				outerloop:
				for (String docIDcmod : CMODdocID) {
					for (String docIDalf : alfDocID) {
						if (docIDcmod.equals(docIDalf)) {//found
							break outerloop;
						}
					}
					String[] record = { batchID, docIDcmod };
					Missing.add(record);
//					System.out.println("Missing found    " + record[0] + "," + record[1]);
					System.out.println(Missing.size());
				}

				
			}
			System.out.println(Missing.size());
		}
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);

		System.out.println(duration / 1000000000.0);
	}
	
}
