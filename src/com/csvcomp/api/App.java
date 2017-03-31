package com.csvcomp.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;


/**
 * 
 * @author Hasith Ranasinghe
 *
 */


public class App {
	
	static String getName(Path path) throws FileNotFoundException, NullPointerException{
		String customerName="custName";
		File filePath = new File(path.toString() + "/cmodOut");

		for (String eachFile : filePath.list()) {
			String fileName = eachFile;
			if (customerName.equalsIgnoreCase("custName")) {
				Pattern pattern = Pattern.compile("(?<=DRCi_).*?(?=_[0-9])");
				Matcher matcher = pattern.matcher(fileName);
				matcher.find();
				customerName = matcher.group(0);
				return customerName;
			}
		}
		
		return null;
		
	}

	final static Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) throws Exception {
		
		logger.setLevel(Level.ERROR);
		String customerName = "custName";
		Path path = Paths.get(".");
			 path = Paths.get("C:\\Users\\hmranasinghe\\Desktop\\Alf\\TharangaTool\\csv-compare-tool-0.0.4\\"
			 		+ "csv-compare-tool-0.0.4\\csv-pro-app-0.0.4\\setup\\input\\Test2\\");
//			 path = Paths.get("C:\\Users\\hmranasinghe\\Desktop\\Tharanga Tool\\csv-pro\\setup\\input");
		System.out.println("\n			  -----  CSV Verification Tool version 1.2  -----\n"
				+ "			  ---(Special Edition for 10MIL+ Record Count)---\n");
		
		try {
			customerName = getName(path);
		} catch (Exception e) {
			System.err.println("No Files Found in cmodOut : " + path + "/cmodOut");
			logger.fatal("No Files Found in cmodOut");
			System.exit(0);
		}
		
		logger.info("init");
		System.out.println("Processing Verification Reports for the Customer " + customerName + "\n");
		
		//getting files
		
		File custDataCmod = null, IMRecCmod = null, IMRepCmod = null, userCmod = null,
			custDataAlf = null, IMRecAlf = null, IMRepAlf = null, userAlf = null;
		
		Path cmodPath = Paths.get(path + "\\cmodOut");
		Path alfPath = Paths.get(path + "\\alfOut");
		
		try {
			File[] cmodFiles = cmodPath.toFile().listFiles();
			for (File eachFile : cmodFiles){
				if(eachFile.getName().contains("CustomerDataReport_DRCi"))
					custDataCmod = eachFile;
				else if (eachFile.getName().contains("IMReconciliationDataReport_DRCi"))
					IMRecCmod = eachFile;
				else if (eachFile.getName().contains("IMReportData_DRCi"))
					IMRepCmod = eachFile;
				else if (eachFile.getName().contains("UserReport_DRCi"))
					userCmod = eachFile;
			}
			
			File[] alfFiles = alfPath.toFile().listFiles();
			for (File eachFile : alfFiles){
				if(eachFile.getName().contains("CustomerDataReport_Alfresco"))
					custDataAlf = eachFile;
				else if (eachFile.getName().contains("IMReconciliationDataReport_Alfresco"))
					IMRecAlf = eachFile;
				else if (eachFile.getName().contains("IMReportData_Alfresco"))
					IMRepAlf = eachFile;
				else if (eachFile.getName().contains("UserReport_Alfresco"))
					userAlf = eachFile;
			}
			
		} catch (Exception e) {
			logger.fatal("No Files Found");
		}
		
		logger.info("Parssing started for " + customerName);

		//parse
		Parse parser = new Parse();
		Path output = Paths.get(path + "/" + customerName + "_reports");
		
		if(custDataCmod!=null && custDataAlf!=null){
			System.out.println("Comparing "+customerName+ " Customer Data");
			parser.parseFiles(custDataCmod, custDataAlf, 1); 
			parser.repGen(customerName, output);
		}
		
		if(IMRecCmod!=null && IMRecAlf!=null){
			System.out.println("Comparing " + customerName + " IM Recon Data");
			parser.parseFiles(IMRecCmod, IMRecAlf, 2);
			parser.repGen(customerName, output);
		}
		
		if(IMRepCmod!=null && IMRepAlf!=null){
			System.out.println("Comparing " + customerName + " IM Report Data");
			parser.parseFiles(IMRepCmod, IMRepAlf, 3);
			parser.repGen(customerName, output);
		}
		
		if(userCmod!=null && userAlf!=null){
			System.out.println("Comparing " + customerName + " User Data");
			parser.parseFiles(userCmod, userAlf, 4);
			parser.repGen(customerName, output);
		}
	
		if (parser.isZip()){
//			System.out.println("Zipping");
//			Zip zipper = new Zip();
//			zipper.zipIt(path, customerName);
		}
		
		System.out.println("\nProcessing completed for " + customerName);
		logger.info("Processing completed for " + customerName);
		
	}

}
