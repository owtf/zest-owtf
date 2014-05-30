package org.zest_owtf.mainclass;

import org.mozilla.zest.core.v1.ZestScript;
import org.zaproxy.zap.extension.script.ScriptType;

public class ScriptPrepare {
	
	
	
	public Filereader rdr= null;
	
	public ScriptType sa=new ScriptType("stand_alone","script.type.stand_alone",null,true);
	
	//scriptwrapper created
	public ScriptWrapper script= new ScriptWrapper();
	public ZestScript scr=null;
	
	public ScriptPrepare(String Root_dir){
		
		rdr=new Filereader(Root_dir+"/zest/test.zst");
		script.setType(sa);
		script.setContents(rdr.content);
		script.setEngine(null);
		script.setName("test_script");
		script.setDescription(null);
		script.setLoadOnStart(false);
		
		//zest-scriptwrapper 
		ZestScriptWrapper zsw = new ZestScriptWrapper(script);
		
		scr= zsw.getZestScript();
	}
	
	

}
