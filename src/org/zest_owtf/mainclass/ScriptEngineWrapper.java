package org.zest_owtf.mainclass;

import java.util.List;

import javax.script.ScriptEngine;
import javax.swing.ImageIcon;

public abstract class ScriptEngineWrapper {

	private ScriptEngine engine;
	private String languageName;
	private String engineName;
	
	
	public ScriptEngineWrapper(ScriptEngine engine) {
		this.engine = engine;
		this.engineName = engine.getFactory().getEngineName();
		this.languageName = engine.getFactory().getLanguageName();
	}
	
	public String getLanguageName() {
		return languageName;
	}

	public String getEngineName() {
		return engineName;
	}
	
	public ScriptEngine getEngine() {
		// TODO Nasty hack!
		return engine.getFactory().getScriptEngine();
	}
	
	public abstract boolean isTextBased();
	
	public abstract String getTemplate(String type);
	
	public abstract String getSyntaxStyle();
	
	public abstract ImageIcon getIcon();

	public abstract List<String> getExtensions();
	
	public abstract boolean isRawEngine();
	
	/**
	 * Returns true if this engine supported script types without defined templates.
	 * @param type
	 * @return
	 */
	public abstract boolean isSupportsMissingTemplates();
	
}
