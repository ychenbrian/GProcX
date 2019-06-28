package com.oxygenxml.sdksamples.workspace.mainUI;

import com.oxygenxml.sdksamples.workspace.step.Pipeline;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;

public class XFigureTabs extends JTabbedPane {

    private XFrame frame;
    private ArrayList<XPanel> panels;
    private ArrayList<XPanel> tabs;

    public XFigureTabs(int location, XFrame frame) {
        super(location);
        this.frame = frame;
        this.panels = new ArrayList<XPanel>();
        this.tabs = new ArrayList<XPanel>();

        Pipeline initStep = this.frame.getSelectedPipeline();
        this.addStep(initStep);
        this.openTab(initStep.getUUID());
        //this.addMouseListener(new MouseHandler());
    }

    public void updateInfo() {
        this.getCurrentTab().updateInfo();
    }

    public void addStep(Pipeline step) {
        panels.add(new XPanel(frame, step));
    }

    public int findTab(UUID uuid) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getUUID() == uuid) {
                return i;
            }
        }
        return -1;
    }

    public void addTab(UUID uuid) {
        for (XPanel panel : panels) {
            if (panel.getUUID() == uuid) {
                this.add(panel.getType(), panel);
                this.tabs.add(panel);
                return;
            }
        }
    }

    public void openTab(UUID uuid) {
        int index = this.findTab(uuid);

        if (index >= 0) {   // if already exist the tab
            this.setSelectedIndex(index);
        } else {
            this.addTab(uuid);
            this.setSelectedIndex(this.getTabCount() - 1);
        }
    }

    public void removeCurrentTab() {
        int index = this.getSelectedIndex();
        this.remove(index);
        this.tabs.remove(index);
    }

    public XPanel getCurrentTab() {
        return this.tabs.get(this.getSelectedIndex());
    }

    public Pipeline getCurrentStep() {
        return this.tabs.get(this.getSelectedIndex()).getMainPipeline();
    }
}
