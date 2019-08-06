package gprocx.mainUI;

import com.xml_project.morganaxproc.XProcInterfaceException;
import gprocx.step.GProcXStep;
import gprocx.step.StepInfo;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.text.WSTextEditorPage;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import java.awt.*;
import java.util.UUID;

public class XFrame {

    private JFrame frame;
    private JSplitPane splitPane;
    private XFigureTabs figureTabs;
    private XConfigTabs configTabs;
    //private XToolbar toolbar;
    private GProcXStep mainStep = null;
    private GProcXStep selectedStep = null;
    private GProcXStep newStep = null;

    private String[] atomicTypes;
    private StandalonePluginWorkspace WSAccess;

    // variables for drawing
    private boolean drawStepActive = false;
    private boolean drawPipe01Active = false;
    private boolean drawPipe02Active = false;

    // variables for generate codes
    private JTextArea code;

    // constructor
    public XFrame(StandalonePluginWorkspace pluginWorkspaceAccess) throws XProcInterfaceException {

        // setting of the frame
    	this.WSAccess = pluginWorkspaceAccess;
    	this.frame = new JFrame("GProcX - A GUI Tool For XProc");  ////////////////////
    	this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	this.frame.setVisible(true);
    	this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    	this.atomicTypes = StepInfo.getAtomicTypes();

    	// toolbar
        //this.toolbar = new XToolbar(this);
        //this.frame.getContentPane().add(this.toolbar, BorderLayout.PAGE_START);

        this.code = new JTextArea("");
        this.code.setEditable(false);
        //this.code.setLineWrap(true);

        // set the configuration tabs on the left, more details in mainUI.XConfigTabs
        this.configTabs = new XConfigTabs(this);
        this.frame.getContentPane().add(this.configTabs, BorderLayout.WEST);

        // set init selected step
        this.selectedStep = new GProcXStep(this, "p:declare-step");
        this.mainStep = this.selectedStep;

        // set the menu bar, more details in mainUI.XMenuBar
        this.frame.setJMenuBar(new XMenuBar(this));

        // set the code window on the right
        JPanel codePanel = new JPanel();
        codePanel.add(this.code);

        // tab
        this.figureTabs = new XFigureTabs(JTabbedPane.TOP, this, this.mainStep);

        this.splitPane = new JSplitPane();
        this.splitPane.setLeftComponent(this.figureTabs);
        this.splitPane.setRightComponent(this.code);

        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setContinuousLayout(true);
        this.splitPane.setDividerLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2);

        this.frame.getContentPane().add(this.splitPane);
        this.updateInfo();
    }

    // core function of the program, update the listed information
    public void updateInfo() {
        //this.figureTabs.updateInfo();
        this.configTabs.updateInfo();
        if (this.mainStep != null) {
            this.mainStep.updateInfo();
            this.setCode(this.mainStep.toString(0));
        }
    }

    // update the sequence of steps only
    public void updateSequence() {
        if (this.mainStep != null) {
            this.mainStep.updateInfo();
            this.getCurrentPanel().repaint();
            this.setCode(this.mainStep.toString(0));
        }
    }

    // return the name of all atomic steps
    public String[] getAtomicTypes() {
        return this.atomicTypes;
    }

    // activate the step drawing
    public void setDrawStepActive(boolean drawStepActive) {
        this.drawStepActive = drawStepActive;
    }

    // checking the step drawing status
    public boolean isDrawStepActive() {
        return drawStepActive;
    }

    public void addMainStep(GProcXStep mainStep) {
        this.mainStep = mainStep;
        this.setSelectedStep(mainStep);

        this.figureTabs.addStep(mainStep);
        this.figureTabs.openTab(mainStep.getUUID());
    }

    public void setMainStep(GProcXStep mainStep) {
        this.mainStep = mainStep;
        this.setSelectedStep(mainStep);
    }

    public GProcXStep getMainStep() {
        return mainStep;
    }

    public void setDrawPipe01Active(boolean drawPipe01Active) {
        this.drawPipe01Active = drawPipe01Active;
    }

    public void setDrawPipe02Active(boolean drawPipe02Active) {
        this.drawPipe02Active = drawPipe02Active;
    }

    public boolean isDrawPipe01Active() {
        return drawPipe01Active;
    }

    public boolean isDrawPipe02Active() {
        return drawPipe02Active;
    }

    public static Dimension SCREENSIZE() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public void setCode(String code) {
        this.code.setText(code);
    }

    public String getCode() {
        return this.code.getText();
    }

    public GProcXStep getSelectedStep() {
        return selectedStep;
    }

    public void setSelectedStep(GProcXStep selectedStep) {
        this.selectedStep = selectedStep;
        updateInfo();
    }

    public String getOxygenCode() {
    	WSEditor editorAccess = this.WSAccess.getCurrentEditorAccess(StandalonePluginWorkspace.MAIN_EDITING_AREA);
    	if (editorAccess != null) {
    		WSTextEditorPage textPage = (WSTextEditorPage) editorAccess.getCurrentPage();
      	    Document doc = textPage.getDocument();
      	    
      	    try {
				return doc.getText(0, doc.getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
    	}
    	return "";
    }
    
    public XPanel getCurrentPanel() {
        return this.figureTabs.getCurrentTab();
    }

    public void addStep(GProcXStep step) {
        this.figureTabs.addStep(step);
        this.selectedStep = step;
    }

    public XFigureTabs getFigureTabs() {
        return figureTabs;
    }

    public GProcXStep getNewStep() {
        return newStep;
    }

    public void setNewStep(GProcXStep newStep) {
        this.newStep = newStep;
    }

    public void openTab(UUID uuid) {
        this.figureTabs.openTab(uuid);
    }

    public void removeCurrentTab() {
        this.figureTabs.removeCurrentTab();
    }

    public GProcXStep getCurrentStep() {
        return this.figureTabs.getCurrentStep();
    }

    public static void showErrorMessage(String info) {
        JOptionPane.showMessageDialog(
                null,
                info,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showWarningMessage(String info) {
        JOptionPane.showMessageDialog(
                null,
                info,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void showInformationMessage(String info) {
        JOptionPane.showMessageDialog(
                null,
                info,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
