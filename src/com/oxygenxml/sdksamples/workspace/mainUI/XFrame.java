package com.oxygenxml.sdksamples.workspace.mainUI;

import com.oxygenxml.sdksamples.workspace.step.AtomicInfo;
import com.oxygenxml.sdksamples.workspace.step.Pipeline;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class XFrame {

    private JFrame frame;
    private JSplitPane splitPane;
    private XFigureTabs tabManager;
    private XConfigTabs configTabs;
    private Pipeline selectedPipeline;

    // variables to store basic information
    private AtomicInfo atomicInfo;

    // variables for drawing
    private boolean drawPipelineActive = false;
    private boolean drawPipe01Active = false;
    private boolean drawPipe02Active = false;

    // variables for generate codes
    private JTextArea code;

    // constructor
    public XFrame() {

        // setting of the frame
    	this.frame = new JFrame("Simple flowchart test.");  ////////////////////
    	this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	this.frame.setVisible(true);
    	this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.code = new JTextArea("Test code");
        //this.code.setLineWrap(true);

        // XProc related settings
        this.atomicInfo = new AtomicInfo();

        // set the configuration tabs on the left, more details in mainUI.XConfigTabs
        this.configTabs = new XConfigTabs(this);
        this.frame.getContentPane().add(this.configTabs, BorderLayout.WEST);

        // set init selected pipeline
        this.selectedPipeline = new Pipeline(this, "p:declare-step");

        // set the menu bar, more details in mainUI.XMenuBar
        this.frame.setJMenuBar(new XMenuBar(this));


        // set the code window on the right
        JPanel codePanel = new JPanel();
        codePanel.add(this.code);

        // tab
        this.tabManager = new XFigureTabs(JTabbedPane.TOP, this);

        this.splitPane = new JSplitPane();
        this.splitPane.setLeftComponent(this.tabManager);
        this.splitPane.setRightComponent(this.code);

        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setContinuousLayout(true);
        this.splitPane.setDividerLocation(Toolkit.getDefaultToolkit().getScreenSize().width*7/10);

        this.frame.getContentPane().add(this.splitPane);
    }

    // core function of the program, update the listed information
    public void updateInfo() {
        //this.tabManager.updateInfo();
        this.configTabs.updateInfo();
    }

    // return the information of an atomic step
    public Node getAtomicInfo(String type) {
        return this.atomicInfo.getElement(type);
    }

    // activate the step drawing
    public void setDrawStepActive(boolean drawStepActive) {
        this.drawPipelineActive = drawStepActive;
    }

    // checking the step drawing status
    public boolean isDrawStepActive() {
        return drawPipelineActive;
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

    public Pipeline getSelectedPipeline() {
        return selectedPipeline;
    }

    public void setSelectedPipeline(Pipeline selectedPipeline) {
        this.selectedPipeline = selectedPipeline;
        this.setCode(this.selectedPipeline.getType());
    }

    public void addPipeline(Pipeline pipeline) {
        this.tabManager.addStep(pipeline);
        this.selectedPipeline = pipeline;
    }

    public void openTab(UUID uuid) {
        this.tabManager.openTab(uuid);
    }

    public void removeCurrentTab() {
        this.tabManager.removeCurrentTab();
    }
}
