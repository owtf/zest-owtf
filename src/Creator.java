
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;








public class Creator {

	public static void main(String[] args) {
		
		String content = readFile("test.zst",StandardCharsets.UTF_8);
		ScriptType sa=new ScriptType("stand_alone","script.type.stand_alone",null,true);
		ScriptWrapper script= new ScriptWrapper();
		script.setType(sa);
		script.setContents(content);
		script.setEngine(null);
		script.setName("test_script");
		script.setDescription(null);
		script.setLoadOnStart(false);
		
		ZestScriptWrapper zsw = new ZestScriptWrapper(script);
		
		
		
		
		
	};
	
	
	



static String readFile(String path, Charset encoding) 
		 
		{
		  byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		  
		}



}















































