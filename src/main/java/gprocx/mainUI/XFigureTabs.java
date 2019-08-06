package gprocx.mainUI;

import gprocx.step.GProcXStep;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.UUID;

public class XFigureTabs extends JTabbedPane {

    private XFrame frame;
    private ArrayList<XPanel> panels;
    private ArrayList<XPanel> tabs;

    public XFigureTabs(int location, XFrame frame, GProcXStep mainPipeline) {
        super(location);
        this.frame = frame;
        this.panels = new ArrayList<XPanel>();
        this.tabs = new ArrayList<XPanel>();

        GProcXStep initPipeline = mainPipeline;
        this.addStep(initPipeline);
        this.openTab(initPipeline.getUUID());

        this.addChangeListener(new ChangeTab());
        //this.addMouseListener(new MouseHandler());
    }

    public void updateInfo() {
        this.getCurrentTab().updateInfo();
    }

    public void addStep(GProcXStep pipeline) {
        panels.add(new XPanel(frame, pipeline));
    }

    public void removeStep(UUID uuid) {
        for (XPanel panel : this.panels) {
            if (panel.getMainPipeline().getUUID() == uuid) {
                this.openTab(uuid);
                this.removeCurrentTab();

                this.panels.remove(panel);
                return;
            }
        }
    }

    public void newProgram(GProcXStep pipeline) {
        this.panels.clear();
        for (int i = 0; i < tabs.size(); i++) {
            this.removeCurrentTab();
        }
        this.tabs.clear();

        this.addStep(pipeline);
        this.openTab(pipeline.getUUID());
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
                this.add("<" + panel.getType() + ">", panel);
                this.tabs.add(panel);
                return;
            }
        }
    }

    public void openTab(UUID uuid) {
        int index = this.findTab(uuid);

        if (index >= 0) {   // if already exist the tab
            this.setSelectedIndex(index);
            this.frame.setSelectedStep(this.getCurrentTab().getMainPipeline());
            this.frame.setMainStep(this.getCurrentTab().getMainPipeline());
        } else {
            this.addTab(uuid);
            this.setSelectedIndex(this.getTabCount() - 1);
            this.frame.setSelectedStep(this.getCurrentTab().getMainPipeline());
            this.frame.setMainStep(this.getCurrentTab().getMainPipeline());
        }
    }

    public void removeCurrentTab() {
        if (this.tabs.size() == 1) {
            frame.showErrorMessage("You cannot close the last tab.");
            return;
        }
        int index = this.getSelectedIndex();
        this.remove(index);
        this.tabs.remove(index);

        this.setSelectedIndex(this.getTabCount() - 1);
        this.frame.setSelectedStep(this.getCurrentTab().getMainPipeline());
        this.frame.setMainStep(this.getCurrentTab().getMainPipeline());
    }

    public XPanel getCurrentTab() {
        return this.tabs.get(this.getSelectedIndex());
    }

    public GProcXStep getCurrentStep() {
        return this.tabs.get(this.getSelectedIndex()).getMainPipeline();
    }

    private class ChangeTab implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            frame.setMainStep(tabs.get(getSelectedIndex()).getMainPipeline());
        }
    }
}
