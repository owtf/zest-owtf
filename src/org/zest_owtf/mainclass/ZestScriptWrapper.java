package org.zest_owtf.mainclass;


import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestScript;




public class ZestScriptWrapper extends ScriptWrapper {
	
	public static final String ZAP_BREAK_VARIABLE_NAME = "zap.break";
	public static final String ZAP_BREAK_VARIABLE_VALUE = "set";

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
/*		if (zestScript == null) {
			// new script
			zestScript = new ZestScript();
			Type ztype;
			switch (script.getType().getName()) {
			case ExtensionActiveScan.SCRIPT_TYPE_ACTIVE:
				ztype = Type.Active;
				break;
			case ExtensionPassiveScan.SCRIPT_TYPE_PASSIVE:
				ztype = Type.Passive;
				break;
			case ExtensionScript.TYPE_TARGETED:
				ztype = Type.Targeted;
				break;
			case ExtensionScript.TYPE_PROXY:
				ztype = Type.Targeted;
				break;
			case ExtensionScript.TYPE_STANDALONE:
			case ScriptBasedAuthenticationMethodType.SCRIPT_TYPE_AUTH:
			default:
				ztype = Type.StandAlone;
				break;
			}
			zestScript.setType(ztype);
			zestScript.setDescription(script.getDescription());
		}*/
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
	
/*Not working yet :P
 
 * 	@SuppressWarnings("unchecked")
	public <T> T getInterface(Class<T> class1) throws ScriptException, IOException {
		// Clone the wrapper so that we get a new instance every time
		if (class1.isAssignableFrom(ZestPassiveRunner.class)) {
			return (T) new ZestPassiveRunner(this.getExtension(), this.clone());
			
		} else if (class1.isAssignableFrom(ZestActiveRunner.class)) {
			return (T) new ZestActiveRunner(this.getExtension(), this.clone());
			
		} else if (class1.isAssignableFrom(ZestTargetedRunner.class)) {
			return (T) new ZestTargetedRunner(this.getExtension(), this.clone());
			
		} else if (class1.isAssignableFrom(ZestProxyRunner.class)) {
			return (T) new ZestProxyRunner(this.getExtension(), this.clone());
			
		} else if (class1.isAssignableFrom(ZestAuthenticationRunner.class)) {
			return (T) new ZestAuthenticationRunner(this.getExtension(), this.clone());
		}
		return null;
	}

	private ExtensionZest getExtension() {
		if (extension == null) {
			extension = (ExtensionZest) Control.getSingleton().getExtensionLoader().getExtension(ExtensionZest.NAME);
		}
		return extension;
	}
	
	protected ZestScriptWrapper clone() {
		ZestScriptWrapper clone = new ZestScriptWrapper(this.original);
		clone.setWriter(this.getWriter());
		clone.setDebug(this.isDebug());
		clone.setRecording(this.isRecording());
		return clone;
	}*/

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
