package com.csvcomp.api.report;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReportGen {

	String custName;
	Path path;
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public ReportGen(String customerName, Path path) {
		this.custName = customerName;
		this.path = path;
	}

	public void createReport(String report) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(path + "\\" + custName + "report.txt")) {
			out.println(report);
		}
	}

	public void createCSV(Set<String[]> set, int reportType, Path path, int dataType) throws Exception {
		
		List<String[]> list = new ArrayList<>(set);
		
		Date dateNow = new Date();
		String date = "_" + dateFormat.format(dateNow);
		
		String type2 = "_Missing";
		if (reportType > 0) {
			type2 = "_Alfresco_Duplicates";
			if(reportType > 10)
				type2 = "_CMOD_Duplicates";
		}

		switch (dataType) {
		case 1: {//customer data
			
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + "\\" + custName + date  + "_CustomerDataReport" + type2 + ".csv"), ENCODING)){
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("BATCH ID,DOC ID" + System.getProperty("line.separator"));
				for (int j = 0; j < list.size(); j++) {
					stringBuilder.append(list.get(j)[0]+","+list.get(j)[1]+System.getProperty("line.separator"));
				}//added the list.get(j)[0] to get the DocID
				writer.write(stringBuilder.toString());
				writer.newLine();
			}catch (Exception e) {
				throw new Exception(" File Write Error " + e.getMessage());
			}
			
			break;
		}
		case 2: {//recon
			
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + "\\" + custName + date + "_IMReconciliationDataReport" + type2 + ".csv"), ENCODING)){
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("BATCH ID,APP ID" + System.getProperty("line.separator"));
				for (int j = 0; j < list.size(); j++) {
					stringBuilder.append(list.get(j)[0]+","+list.get(j)[1]+System.getProperty("line.separator"));
				}
				writer.write(stringBuilder.toString());
				writer.newLine();
			}catch (Exception e) {
				throw new Exception(" File Write Error " + e.getMessage());
			}

			break;
		}
		case 3: {//report
			
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + "\\" + custName + date + "_IMReportsDataReport" + type2 + ".csv"), ENCODING)){
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("REPT ID,REPT DATE,DOC ID" + System.getProperty("line.separator"));
				for (int j = 0; j < list.size(); j++) {
					stringBuilder.append(list.get(j)[0]+","+list.get(j)[1]+System.getProperty("line.separator"));
				}
				writer.write(stringBuilder.toString());
				writer.newLine();
			}catch (Exception e) {
				throw new Exception(" File Write Error " + e.getMessage());
			}
			
			break;
		}
		case 4: {//user
			
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + "\\" + custName + date  + "_UserDataReport" + type2 + ".csv"), ENCODING)){
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("USER ID" + System.getProperty("line.separator"));
				for (int j = 0; j < list.size(); j++) {
					stringBuilder.append(list.get(j)[1]+System.getProperty("line.separator"));
				}
				writer.write(stringBuilder.toString());
				writer.newLine();
			}catch (Exception e) {
				throw new Exception(" File Write Error " + e.getMessage());
			}

			break;
		}
		}
	}
}
