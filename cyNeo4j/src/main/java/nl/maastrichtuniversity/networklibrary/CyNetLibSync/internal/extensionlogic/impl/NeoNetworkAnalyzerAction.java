package nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.impl;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.Plugin;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.Extension;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionCall;
import nl.maastrichtuniversity.networklibrary.CyNetLibSync.internal.extensionlogic.ExtensionExecutor;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;

public class NeoNetworkAnalyzerAction extends AbstractCyAction {
	public final static String MENU_TITLE = "NeoNetworkAnalyzer";
	public final static String MENU_LOC = "Apps.cyNeo4j.Statistics";

	private Plugin plugin;

	public NeoNetworkAnalyzerAction(CyApplicationManager cyApplicationManager, Plugin plugin){
		super(MENU_TITLE, cyApplicationManager, null, null);
		setPreferredMenu(MENU_LOC);
		setEnabled(false);
		this.plugin = plugin;

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Extension neoAnalyzer = getPlugin().getInteractor().supportsExtension("neonetworkanalyzer");
		
		ExtensionExecutor exec = new NeoNetworkAnalyzerExec();
		
		exec.setPlugin(plugin);
		exec.setExtension(neoAnalyzer);
		
		if(!exec.collectParameters()){
			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Failed to collect parameters for " + neoAnalyzer.getName());
			return;
		}
	
		List<ExtensionCall> calls = exec.buildExtensionCalls();
		
		for(ExtensionCall call : calls){
			Object callRetValue = plugin.getInteractor().executeExtensionCall(call);
			exec.processCallResponse(call,callRetValue);
		}
	}

	protected Plugin getPlugin() {
		return plugin;
	}

	
}
