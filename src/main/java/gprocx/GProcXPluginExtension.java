package gprocx;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;

import com.xml_project.morganaxproc.XProcCompiler;
import com.xml_project.morganaxproc.XProcEngine;
import com.xml_project.morganaxproc.XProcInterfaceException;
import com.xml_project.morganaxproc.XProcOutput;
import com.xml_project.morganaxproc.XProcPipeline;
import com.xml_project.morganaxproc.XProcResult;
import com.xml_project.morganaxproc.XProcSource;
import com.xml_project.morganaxproc.filesystem.XProcFilesystem;
import com.xml_project.morganaxproc.security.XProcSecurityException;

import gprocx.mainUI.XFrame;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;
import ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer;
import ro.sync.exml.workspace.api.standalone.ToolbarInfo;
import ro.sync.exml.workspace.api.standalone.ui.ToolbarButton;

/**
 * Plugin extension - workspace access extension.
 */
@SuppressWarnings("restriction")
public class GProcXPluginExtension implements WorkspaceAccessPluginExtension {
  /**
   * The custom messages area. A sample component added to your custom view.
   */
  private JTextArea customMessagesArea;
  
  /**
   * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationStarted(ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace)
   */
  public void applicationStarted(final StandalonePluginWorkspace pluginWorkspaceAccess) {
	  
      
	  String pipeline_xml = "<p:declare-step xmlns:p=\"http://www.w3.org/ns/xproc\" version=\"1.0\"\n" +
              "                xmlns:c=\"http://www.w3.org/ns/xproc-step\"\n" +
              "                xmlns:cx=\"http://xmlcalabash.com/ns/extensions\"\n" +
              "                xmlns:exf=\"http://exproc.org/standard/functions\"\n" +
              "                exclude-inline-prefixes=\"cx exf\"\n" +
              "                name=\"main\">\n" +
              "<p:input port=\"source\"/>\n" +
              "<p:output port=\"result\"/>\n" +
              "\n" +
              "<p:identity>\n" +
              "  <p:input port=\"source\">\n" +
              "    <p:inline><doc/></p:inline>\n" +
              "  </p:input>\n" +
              "</p:identity>\n" +
              "\n" +
              "</p:declare-step>\n";
      
	  
	  
	  
	  


      //InputStream stream = new ByteArrayInputStream(pipeline_xml.getBytes());
      //DocumentBuilder builder = saxon.newDocumentBuilder();
      //XdmNode pipeline_doc = builder.build(new SAXSource(new InputSource(stream)));

      //XPipeline pipeline = runtime.use(pipeline_doc);
      
      
	  //You can set or read global options.
	  //The "ro.sync.exml.options.APIAccessibleOptionTags" contains all accessible keys.
	  //		  pluginWorkspaceAccess.setGlobalObjectProperty("can.edit.read.only.files", Boolean.FALSE);
	  // Check In action

	  //You can access the content inside each opened WSEditor depending on the current editing page (Text/Grid or Author).  
	  // A sample action which will be mounted on the main menu, toolbar and contextual menu.
	final Action selectionSourceAction = createShowSelectionAction(pluginWorkspaceAccess);
	//Mount the action on the contextual menus for the Text and Author modes.
	
	
	  //You can use this callback to populate your custom toolbar (defined in the plugin.xml) or to modify an existing Oxygen toolbar 
	  // (add components to it or remove them) 
	  pluginWorkspaceAccess.addToolbarComponentsCustomizer(new ToolbarComponentsCustomizer() {
		  /**
		   * @see ro.sync.exml.workspace.api.standalone.ToolbarComponentsCustomizer#customizeToolbar(ro.sync.exml.workspace.api.standalone.ToolbarInfo)
		   */
		  public void customizeToolbar(ToolbarInfo toolbarInfo) {
			  //The toolbar ID is defined in the "plugin.xml"
			  if("SampleWorkspaceAccessToolbarID".equals(toolbarInfo.getToolbarID())) {
				  List<JComponent> comps = new ArrayList<JComponent>(); 
				  JComponent[] initialComponents = toolbarInfo.getComponents();
				  boolean hasInitialComponents = initialComponents != null && initialComponents.length > 0; 
				  if (hasInitialComponents) {
					  // Add initial toolbar components
					  for (JComponent toolbarItem : initialComponents) {
						  comps.add(toolbarItem);
					  }
				  }
				  
				  //Add your own toolbar button using our "ro.sync.exml.workspace.api.standalone.ui.ToolbarButton" API component
				  ToolbarButton customButton = new ToolbarButton(selectionSourceAction, true);
				  comps.add(customButton);
				  toolbarInfo.setComponents(comps.toArray(new JComponent[0]));
			  } 
		  }
	  });
  }


	/**
	 * Create the Swing action which shows the current selection.
	 * 
	 * @param pluginWorkspaceAccess The plugin workspace access.
	 * @return The "Show Selection" action
	 */
	@SuppressWarnings("serial")
	private AbstractAction createShowSelectionAction(final StandalonePluginWorkspace pluginWorkspaceAccess) {
		
		AbstractAction abstractAction = new AbstractAction("GProcX") {
			
			// Function that need to implement
			public void actionPerformed(ActionEvent actionevent) {
				//Get the current opened XML document
				  WSEditor editorAccess = pluginWorkspaceAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
				  // The action is available only in Author mode.
				  if(editorAccess != null){

                      try {
						new XFrame();
					} catch (XProcInterfaceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                  }
			}
		};
		
		return abstractAction;
	}
  
  /**
   * @see ro.sync.exml.plugin.workspace.WorkspaceAccessPluginExtension#applicationClosing()
   */
  public boolean applicationClosing() {
	  //You can reject the application closing here
    return true;
  }
}