package org.zest_owtf.mainclass;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class Filereader {
	
	public String content=null;
	public  List<String>fileLineContent=new ArrayList<String>();
	
	public Filereader(String path){
		
		
		this(path,true);
		
		
		
	}
	public Filereader(String path,boolean val){
		if(val){
			content = readFile(path,Charset.forName("UTF-8"));
		}
		else
		 {
			fileLineContent=fillArray(path);
		 }
		
	}
	
	
	
//all content read
	
	
static String readFile(String path, Charset encoding) {
	  
	  byte[] encoded;
	  try {
		encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	  } 
	  catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "";
	  }
	 }
	  
	  
	
	
	
	
// read line by line 
	
private  List<String>  fillArray(String path)	{
	
	FileInputStream fis = null;
    BufferedReader reader = null;
 
    try {
        fis = new FileInputStream("url.txt");
        reader = new BufferedReader(new InputStreamReader(fis));
     
        String line = reader.readLine();
        while(line != null){
        	fileLineContent.add(line);
            line = reader.readLine();
        }          
     
    } catch (FileNotFoundException ex) {
      
    } catch (IOException ex) {
      
     
    } finally {
        try {
            reader.close();
            fis.close();
        } catch (IOException ex) {
          
        }



	
    }
	return fileLineContent;	
	
	

}
}
