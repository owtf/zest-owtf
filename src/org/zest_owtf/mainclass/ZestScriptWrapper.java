package org.zest_owtf.mainclass;


import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestScript;




public class ZestScriptWrapper extends ScriptWrapper {
	


	private boolean incStatusCodeAssertion = true;
	private boolean incLengthAssertion = true;
	private int lengthApprox = 1;
	private ZestScript zestScript = null;
	//private ExtensionZest extension = null;
	private ScriptWrapper original = null;
	private boolean debug = false;
	private boolean recording = false;

	public ZestScriptWrapper  (ScriptWrapper script) {
		this.original = script;
		zestScript = (ZestScript) ZestJSON.fromString(script.getContents());

		// Override the title in case its taken from a template
		zestScript.setTitle(script.getName());

		this.setName(script.getName());
		this.setDescription(script.getDescription());
		this.setEngine(script.getEngine());
		this.setEngineName(script.getEngineName());
		this.setType(script.getType());
		this.setEnabled(script.isEnabled());
		this.setFile(script.getFile());
		this.setContents(script.getContents());
		this.setLoadOnStart(script.isLoadOnStart());
		this.setChanged(false);
	}

	public boolean isIncStatusCodeAssertion() {
		return incStatusCodeAssertion;
	}

	public void setIncStatusCodeAssertion(boolean incStatusCodeAssertion) {
		this.incStatusCodeAssertion = incStatusCodeAssertion;
	}

	public boolean isIncLengthAssertion() {
		return incLengthAssertion;
	}

	public void setIncLengthAssertion(boolean incLengthAssertion) {
		this.incLengthAssertion = incLengthAssertion;
	}
	
	public int getLengthApprox() {
		return lengthApprox;
	}

	public void setLengthApprox(int lengthApprox) {
		this.lengthApprox = lengthApprox;
	}

	public ZestScript getZestScript() {
		return zestScript;
	}
	


	public String getContents() {
		return ZestJSON.toString(this.zestScript);
	}

	@Override
	public boolean equals (Object script) {
		return super.equals(script) || this.original.equals(script);
	}
	
	@Override
	public int hashCode() {
		return this.original.hashCode();
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}
	
	@Override
	public boolean isRunableStandalone() {
		// We can always prompt for parameters :)
		return true;
	}
}
