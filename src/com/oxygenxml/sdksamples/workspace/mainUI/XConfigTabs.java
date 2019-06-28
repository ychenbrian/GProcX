package com.oxygenxml.sdksamples.workspace.mainUI;

import com.oxygenxml.sdksamples.workspace.core.Port;
import com.oxygenxml.sdksamples.workspace.core.StepOption;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

public class XConfigTabs extends JTabbedPane {

    private XFrame frame;

    private JPanel specPanel;
    private JPanel configPanel;
    private JPanel ioPanel;

    private JTextArea document;

    private final String[] ioPortCol = {"type", "port", "primary", "sequence", "kind"};
    String[][] ioPortInfo;
    private final String[] optionCol = {"name", "required", "select"};
    String[][] optionInfo;
    JTable optionTable;

    public XConfigTabs(XFrame frame) {
        this.frame = frame;

        //this.setTabPlacement(JTabbedPane.LEFT);

        setSpecPanel();
        setConfigPanel();
        setIOPanel();

        this.addTab("Specification", this.specPanel);
        this.addTab("Configuration", this.configPanel);
        this.addTab("Inputs / Outputs", this.ioPanel);
    }

    public void setSpecPanel() {
        this.specPanel = new JPanel();
        this.specPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/8,
                Toolkit.getDefaultToolkit().getScreenSize().height));

        JPanel basicPanel = new JPanel();
        basicPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/9,
                Toolkit.getDefaultToolkit().getScreenSize().height/15));
        JPanel docPanel = new JPanel();
        docPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/9,
                Toolkit.getDefaultToolkit().getScreenSize().height/4));
        JPanel ioPanel = new JPanel();
        ioPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/9,
                Toolkit.getDefaultToolkit().getScreenSize().height/4));
        JPanel optionPanel = new JPanel();
        optionPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/9,
                Toolkit.getDefaultToolkit().getScreenSize().height/4));

        TitledBorder basicInfo = new TitledBorder("Atomic step type");
        TitledBorder doc = new TitledBorder("Documentation");
        //TitledBorder ioPorts = new TitledBorder("IO ports");
        TitledBorder options = new TitledBorder("Options");

        basicPanel.setBorder(basicInfo);
        docPanel.setBorder(doc);
        //ioPanel.setBorder(ioPorts);
        optionPanel.setBorder(options);

        String[] listData = new String[]{"p:declare-step", "p:filter"};
        final JComboBox<String> comboBox = new JComboBox<String>(listData);
        basicPanel.add(comboBox, BorderLayout.CENTER);

        this.document = new JTextArea();
        this.document.setLineWrap(true);
        this.document.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10,
                Toolkit.getDefaultToolkit().getScreenSize().height/5));
        this.document.setEditable(false);
        this.document.setBackground(null);
        docPanel.add(this.document, BorderLayout.PAGE_START);

        this.optionInfo = new String[20][3];
        this.optionTable = new JTable(this.optionInfo, this.optionCol);
        this.optionTable.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10,
                Toolkit.getDefaultToolkit().getScreenSize().height/5));
        this.optionTable.getTableHeader().setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10,
                Toolkit.getDefaultToolkit().getScreenSize().height/60));
        this.optionTable.setBackground(null);
        this.optionTable.setShowGrid(false);
        optionPanel.add(this.optionTable.getTableHeader(), BorderLayout.PAGE_START);
        optionPanel.add(this.optionTable, BorderLayout.CENTER);

        this.specPanel.add(basicPanel);
        this.specPanel.add(docPanel);
        //this.specPanel.add(ioPanel);
        this.specPanel.add(optionPanel);
    }

    public void setConfigPanel() {
        this.configPanel = new JPanel();
    }

    public void setIOPanel() {
        this.ioPanel = new JPanel();
    }

    public void updateInfo() {
        if (this.frame.getSelectedPipeline() != null) {
            ArrayList<StepOption> stepOptions = this.frame.getSelectedPipeline().getOptions();

            for (int i = 0; i < stepOptions.size(); i++) {
                this.optionTable.getModel().setValueAt(stepOptions.get(i).getName(), i, 0);
                this.optionTable.getModel().setValueAt(String.valueOf(stepOptions.get(i).isRequired()), i, 1);
                this.optionTable.getModel().setValueAt(stepOptions.get(i).getSelect(), i, 2);
            }

            this.document.setText(this.frame.getSelectedPipeline().getDocumentation());

        }
    }
}
