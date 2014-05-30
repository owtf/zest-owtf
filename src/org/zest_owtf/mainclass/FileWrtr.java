package org.zest_owtf.mainclass;


//Writes zest script content to the given file

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWrtr {
	
	public String file_name;
	public String content;
	
	public FileWrtr(String str,String op_file){
		
		file_name=op_file;
		content=str;
		file_write();
	}
	
	private void file_write(){	
		try{
		 
			File file = new File(file_name);
			// if file doesn't exist, then create it
			if (!file.exists()) {
			file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			System.out.println("Written to "+file_name);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
   }	
	
}
