package gprocx.mainUI;

import com.xml_project.morganaxproc.XProcInterfaceException;
import gprocx.step.GProcXPipeline;
import gprocx.step.StepInfo;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class XFrame {

    private JFrame frame;
    private JSplitPane splitPane;
    private XFigureTabs figureTabs;
    private XConfigTabs configTabs;
    private XToolbar toolbar;
    private GProcXPipeline mainPipeline = null;
    private GProcXPipeline selectedPipeline = null;
    private String newStep = "p:error";

    //
    private String[] atomicTypes;

    // variables for drawing
    private boolean drawPipelineActive = false;
    private boolean drawPipe01Active = false;
    private boolean drawPipe02Active = false;

    // variables for generate codes
    private JTextArea code;

    // constructor
    public XFrame() throws XProcInterfaceException {

        // setting of the frame
    	this.frame = new JFrame("GProcX - A GUI Tool For XProc");  ////////////////////
    	this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	this.frame.setVisible(true);
    	this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    	this.atomicTypes = StepInfo.getStepTypes();

    	// toolbar
        this.toolbar = new XToolbar(this);
        this.frame.getContentPane().add(this.toolbar, BorderLayout.PAGE_START);

        this.code = new JTextArea("EmbeddedTest code");
        //this.code.setLineWrap(true);

        // set the configuration tabs on the left, more details in mainUI.XConfigTabs
        this.configTabs = new XConfigTabs(this);
        this.frame.getContentPane().add(this.configTabs, BorderLayout.WEST);

        // set init selected pipeline
        this.selectedPipeline = new GProcXPipeline(this, "p:declare-step");
        this.mainPipeline = this.selectedPipeline;

        // set the menu bar, more details in mainUI.XMenuBar
        this.frame.setJMenuBar(new XMenuBar(this));

        // set the code window on the right
        JPanel codePanel = new JPanel();
        codePanel.add(this.code);

        // tab
        this.figureTabs = new XFigureTabs(JTabbedPane.TOP, this, this.mainPipeline);

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
        if (this.mainPipeline != null) {
            this.mainPipeline.updateInfo();
            this.setCode(this.mainPipeline.toString(0));
        }
    }

    // update the sequence of pipelines only
    public void updateSequence() {
        if (this.mainPipeline != null) {
            this.mainPipeline.updateInfo();
            this.setCode(this.mainPipeline.toString(0));
        }
    }

    // return the name of all atomic steps
    public String[] getAtomicTypes() {
        return this.atomicTypes;
    }

    // activate the step drawing
    public void setDrawStepActive(boolean drawStepActive) {
        this.drawPipelineActive = drawStepActive;
    }

    // checking the step drawing status
    public boolean isDrawStepActive() {
        return drawPipelineActive;
    }

    public void setNewStep(String newStep) {
        this.newStep = newStep;
    }

    public void addMainPipeline(GProcXPipeline mainPipeline) {
        this.mainPipeline = mainPipeline;
        this.setSelectedPipeline(this.mainPipeline);

        this.figureTabs.addPipeline(mainPipeline);
        this.figureTabs.openTab(mainPipeline.getUUID());
    }

    public void setMainPipeline(GProcXPipeline mainPipeline) {
        this.mainPipeline = mainPipeline;
        this.setSelectedPipeline(mainPipeline);
    }

    public String getNewStep() {
        return newStep;
    }

    public GProcXPipeline getMainPipeline() {
        return mainPipeline;
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

    public GProcXPipeline getSelectedPipeline() {
        return selectedPipeline;
    }

    public void setSelectedPipeline(GProcXPipeline selectedPipeline) {
        this.selectedPipeline = selectedPipeline;
        updateInfo();
    }

    public void addPipeline(GProcXPipeline pipeline) {
        this.figureTabs.addPipeline(pipeline);
        this.selectedPipeline = pipeline;
    }

    public void openTab(UUID uuid) {
        this.figureTabs.openTab(uuid);
    }

    public void removeCurrentTab() {
        this.figureTabs.removeCurrentTab();
    }

    public GProcXPipeline getCurrentPipeline() {
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
