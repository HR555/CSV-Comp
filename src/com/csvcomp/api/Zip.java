package com.csvcomp.api;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

	void zipIt(Path path, String folderName)
	{
    	byte[] buffer = new byte[1024];

    	try{

    		FileOutputStream fos = new FileOutputStream(path + "\\" + folderName + ".zip");
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		ZipEntry ze= new ZipEntry(path+ "\\alfOut");
    		zos.putNextEntry(ze);
    		ZipEntry ze1= new ZipEntry(path+ "\\cmodOut");
    		zos.putNextEntry(ze1);
    		ZipEntry ze2= new ZipEntry(path+ "\\"+ folderName + "_reports");
    		zos.putNextEntry(ze2);
    		FileInputStream in = new FileInputStream(path + "\\" +"alfOut");
    		FileInputStream in1 = new FileInputStream(path + "\\" +"cmodOut");
    		FileInputStream in2 = new FileInputStream(path + "\\"+ folderName +"_reports");

    		int len;
    		while ((len = in.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}
    		
    		while ((len = in1.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}
    		
    		while ((len = in2.read(buffer)) > 0) {
    			zos.write(buffer, 0, len);
    		}

    		in.close();
    		zos.closeEntry();

    		//remember close it
    		zos.close();

    		System.out.println("Zipped");

    	}catch(IOException ex){
    	   ex.printStackTrace();
    	}
    }
}
