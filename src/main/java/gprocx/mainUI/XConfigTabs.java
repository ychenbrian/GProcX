package gprocx.mainUI;

import gprocx.core.*;
import gprocx.step.GProcXDoc;
import gprocx.step.GProcXPipe;
import gprocx.step.GProcXStep;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class XConfigTabs extends JTabbedPane {

    private XFrame frame;

    private JPanel specPanel;
    private JPanel ioPanel;
    private JPanel pipeChildPanel;

    // variables for specification panel
    private JPanel docPanel;
    private JPanel nsPanel;
    private JPanel requiredElePanel;
    private JPanel otherElePanel;
    private SpringLayout docLayout;
    private SpringLayout nsLayout;
    private SpringLayout reqLayout;
    private SpringLayout othLayout;
    // for io panel
    private JPanel inputPanel;
    private JPanel outputPanel;
    private SpringLayout inLayout;
    private SpringLayout outLayout;
    // for pipe child panel
    private JPanel pipePanel;
    private JPanel childPanel;
    private SpringLayout pipeLayout;
    private SpringLayout childLayout;


    public XConfigTabs(XFrame frame) {
        this.frame = frame;

        setSpecPanel();
        setIOPanel();
        setPipeChildPanel();

        this.addTab("Basic", this.specPanel);
        this.addTab("I/O port", this.ioPanel);
        this.addTab("Pipe & Step", this.pipeChildPanel);
    }

    public void setSpecPanel() {

        // main specific panel
        this.specPanel = new JPanel();
        this.specPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));


        // information panel
        this.docPanel = new JPanel();
        this.docPanel.setBorder(new TitledBorder("Documentation"));
        this.docLayout = new SpringLayout();
        this.docPanel.setLayout(this.docLayout);
        this.docPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/5));

        // namespaces
        this.nsPanel = new JPanel();
        this.nsPanel.setBorder(new TitledBorder("Namespace"));
        this.nsLayout = new SpringLayout();
        this.nsPanel.setLayout(this.nsLayout);
        this.nsPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/10));

        // required QNames
        this.requiredElePanel = new JPanel();
        this.requiredElePanel.setBorder(new TitledBorder("Required attribute"));
        this.reqLayout = new SpringLayout();
        this.requiredElePanel.setLayout(this.reqLayout);
        this.requiredElePanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/10));

        // none required QNames
        this.otherElePanel = new JPanel();
        this.otherElePanel.setBorder(new TitledBorder("Other attribute"));
        this.othLayout = new SpringLayout();
        this.otherElePanel.setLayout(this.othLayout);
        this.otherElePanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height*5/10));


        this.specPanel.add(this.docPanel);
        this.specPanel.add(this.nsPanel);
        this.specPanel.add(this.requiredElePanel);
        this.specPanel.add(this.otherElePanel);
    }

    public void setIOPanel() {
        this.ioPanel = new JPanel();
        this.ioPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));


        // I/O panels of the specific panel
        this.inputPanel = new JPanel();
        this.inputPanel.setBorder(new TitledBorder("Input port"));
        this.inLayout = new SpringLayout();
        this.inputPanel.setLayout(this.inLayout);
        this.inputPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));

        this.outputPanel = new JPanel();
        this.outputPanel.setBorder(new TitledBorder("Output port"));
        this.outLayout = new SpringLayout();
        this.outputPanel.setLayout(this.outLayout);
        this.outputPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));


        this.ioPanel.add(this.inputPanel);
        this.ioPanel.add(this.outputPanel);
    }

    public void setPipeChildPanel() {
        this.pipeChildPanel = new JPanel();
        this.pipeChildPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));

        // panel for pipes
        this.pipePanel = new JPanel();
        this.pipePanel.setBorder(new TitledBorder("Pipe"));
        this.pipeLayout = new SpringLayout();
        this.pipePanel.setLayout(this.pipeLayout);
        this.pipePanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));

        // panel for subpipelines
        this.childPanel = new JPanel();
        this.childPanel.setBorder(new TitledBorder("Step"));
        this.childLayout = new SpringLayout();
        this.childPanel.setLayout(this.childLayout);
        this.childPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));


        this.pipeChildPanel.add(this.pipePanel);
        this.pipeChildPanel.add(this.childPanel);
    }

    public void updateInfo() {
        if (this.frame.getSelectedStep() != null) {
            GProcXStep selected = this.frame.getSelectedStep();

            // set docs
            this.docPanel.removeAll();
            Spring docNorth = Spring.constant(0);
            SpringLayout.Constraints docBoxCons = null;
            SpringLayout.Constraints docTextCons = null;
            for (GProcXDoc doc : selected.getDocs()) {
                Box docBox = this.newDocBox(doc);
                this.docPanel.add(docBox);
                JLabel docText = this.createJLabelWithWrapWidth(Toolkit.getDefaultToolkit().getScreenSize().width*19/100, new JLabel(doc.getContent()));
                this.docPanel.add(docText);

                docBoxCons = docLayout.getConstraints(docBox);
                docTextCons = docLayout.getConstraints(docText);

                docBoxCons.setX(Spring.constant(5));
                docBoxCons.setY(Spring.sum(docNorth, Spring.constant(3)));
                docNorth = docBoxCons.getConstraint(SpringLayout.SOUTH);

                docTextCons.setX(Spring.constant(15));
                docTextCons.setY(Spring.sum(docNorth, Spring.constant(3)));
                docNorth = Spring.sum(docTextCons.getConstraint(SpringLayout.SOUTH), Spring.constant(5));
            }
            if (!selected.isBuildin()) {
                JButton docButtion = new JButton("New");
                docButtion.addActionListener(new AddDocListener());
                this.docPanel.add(docButtion);

                SpringLayout.Constraints docButtonCons = docLayout.getConstraints(docButtion);
                docButtonCons.setX(Spring.constant(5));
                docButtonCons.setY(Spring.sum(docNorth, Spring.constant(15)));
            }

            // set namespaces
            this.nsPanel.removeAll();
            Spring nsNorth = Spring.constant(0);
            SpringLayout.Constraints nsBoxCons = null;
            for (QName ns : selected.getNamespaces()) {
                Box nsBox = this.newNamespaceBox(ns);
                this.nsPanel.add(nsBox);

                nsBoxCons = nsLayout.getConstraints(nsBox);

                nsBoxCons.setX(Spring.constant(5));
                nsBoxCons.setY(Spring.sum(nsNorth, Spring.constant(3)));
                nsNorth = nsBoxCons.getConstraint(SpringLayout.SOUTH);
            }
            JButton nsButton = new JButton("New");
            nsButton.addActionListener(new AddNSListener());
            this.nsPanel.add(nsButton);

            SpringLayout.Constraints nsButtonCons = this.nsLayout.getConstraints(nsButton);
            nsButtonCons.setX(Spring.constant(5));
            nsButtonCons.setY(Spring.sum(nsNorth, Spring.constant(15)));

            // set qnames
            this.requiredElePanel.removeAll();
            Spring reqNorth = Spring.constant(0);
            SpringLayout.Constraints reqBoxCons = null;
            for (QName qname : selected.getQNames()) {
                if (qname.isRequired()) {
                    Box reqBox = this.newElementBox(selected.getQNames(), qname);
                    this.requiredElePanel.add(reqBox);

                    reqBoxCons = reqLayout.getConstraints(reqBox);

                    reqBoxCons.setX(Spring.constant(5));
                    reqBoxCons.setY(Spring.sum(reqNorth, Spring.constant(3)));
                    reqNorth = reqBoxCons.getConstraint(SpringLayout.SOUTH);
                }
            }
            if (!selected.isBuildin()) {
                JButton elementButton = new JButton("New");
                elementButton.addActionListener(new AddElementListener());
                this.requiredElePanel.add(elementButton);

                SpringLayout.Constraints reqButtonCons = this.reqLayout.getConstraints(elementButton);
                reqButtonCons.setX(Spring.constant(5));
                reqButtonCons.setY(Spring.sum(reqNorth, Spring.constant(15)));
            }

            this.otherElePanel.removeAll();
            Spring othNorth = Spring.constant(0);
            SpringLayout.Constraints othBoxCons = null;
            for (QName qname : selected.getQNames()) {
                if (!qname.isRequired()) {
                    Box othBox = this.newElementBox(selected.getQNames(), qname);
                    this.otherElePanel.add(othBox);
                    othBoxCons = othLayout.getConstraints(othBox);

                    othBoxCons.setX(Spring.constant(5));
                    othBoxCons.setY(Spring.sum(othNorth, Spring.constant(3)));
                    othNorth = othBoxCons.getConstraint(SpringLayout.SOUTH);
                }
            }
            if (!selected.isBuildin()) {
                JButton elementButton = new JButton("New");
                elementButton.addActionListener(new AddElementListener());
                this.otherElePanel.add(elementButton);

                SpringLayout.Constraints othButtonCons = this.othLayout.getConstraints(elementButton);
                othButtonCons.setX(Spring.constant(5));
                othButtonCons.setY(Spring.sum(othNorth, Spring.constant(15)));
            }


            // set inputs
            this.inputPanel.removeAll();
            Spring inNorth = Spring.constant(0);
            SpringLayout.Constraints inBoxCons01 = null;
            SpringLayout.Constraints inBoxCons02 = null;
            SpringLayout.Constraints inBoxCons03 = null;
            for (GProcXPort port : selected.getInputs()) {
                Box inBox01 = this.newInBox01(port);
                Box inBox02 = this.newInBox02(port);
                Box inBox03 = this.newInBox03(port);
                this.inputPanel.add(inBox01);
                this.inputPanel.add(inBox02);
                this.inputPanel.add(inBox03);
                inBoxCons01 = inLayout.getConstraints(inBox01);
                inBoxCons02 = inLayout.getConstraints(inBox02);
                inBoxCons03 = inLayout.getConstraints(inBox03);

                inBoxCons01.setX(Spring.constant(5));
                inBoxCons01.setY(Spring.sum(inNorth, Spring.constant(3)));
                inNorth = inBoxCons01.getConstraint(SpringLayout.SOUTH);

                inBoxCons02.setX(Spring.constant(5));
                inBoxCons02.setY(Spring.sum(inNorth, Spring.constant(3)));
                inNorth = inBoxCons02.getConstraint(SpringLayout.SOUTH);

                inBoxCons03.setX(Spring.constant(5));
                inBoxCons03.setY(Spring.sum(inNorth, Spring.constant(3)));
                inNorth = inBoxCons03.getConstraint(SpringLayout.SOUTH);

                SpringLayout.Constraints sourceCons = null;
                for (final IOSource source : port.getSources()) {
                    Box sourceBox = Box.createHorizontalBox();
                    if (source.getSourceType().equals("p:inline")) {
                        JButton inlineButtion = new JButton("Edit inline");
                        inlineButtion.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                new InlineDialog(null, source);
                            }
                        });
                        sourceBox.add(inlineButtion);
                        sourceBox.add(Box.createHorizontalStrut(10));
                    }
                    sourceBox.add(new DeleteSourceButton(port, source, "Delete source"));
                    sourceBox.add(Box.createHorizontalStrut(30));
                    sourceBox.add(new JLabel("Type - " + source.getSourceType()));
                    this.inputPanel.add(sourceBox);
                    sourceCons = inLayout.getConstraints(sourceBox);

                    sourceCons.setX(Spring.constant(5));
                    sourceCons.setY(Spring.sum(inNorth, Spring.constant(3)));
                    inNorth = sourceCons.getConstraint(SpringLayout.SOUTH);

                    SpringLayout.Constraints qnameCons = null;
                    for (QName qname : source.getQNames()) {
                        Box qBox = this.newElementBox(source.getQNames(), qname);
                        this.inputPanel.add(qBox);
                        qnameCons = inLayout.getConstraints(qBox);

                        qnameCons.setX(Spring.constant(5));
                        qnameCons.setY(Spring.sum(inNorth, Spring.constant(3)));
                        inNorth = qnameCons.getConstraint(SpringLayout.SOUTH);
                    }
                    inNorth = Spring.sum(inNorth, Spring.constant(10));
                }

                inNorth = Spring.sum(inNorth, Spring.constant(20));
            }

            if (!selected.isBuildin()) {
                JButton inportButtion = new JButton("New port");
                inportButtion.addActionListener(new AddInPortListener());
                this.inputPanel.add(inportButtion);

                SpringLayout.Constraints inportButtonCons = this.inLayout.getConstraints(inportButtion);
                inportButtonCons.setX(Spring.constant(5));
                inportButtonCons.setY(Spring.sum(inNorth, Spring.constant(-10)));
            }


            // set outputs
            this.outputPanel.removeAll();
            Spring outNorth = Spring.constant(0);
            SpringLayout.Constraints outBoxCons01 = null;
            SpringLayout.Constraints outBoxCons02 = null;
            SpringLayout.Constraints outBoxCons03 = null;
            for (GProcXPort port : selected.getOutputs()) {
                Box outBox01 = this.newOutBox01(port);
                Box outBox02 = this.newInBox02(port);
                Box outBox03 = this.newInBox03(port);
                this.outputPanel.add(outBox01);
                this.outputPanel.add(outBox02);
                this.outputPanel.add(outBox03);
                outBoxCons01 = outLayout.getConstraints(outBox01);
                outBoxCons02 = outLayout.getConstraints(outBox02);
                outBoxCons03 = outLayout.getConstraints(outBox03);

                outBoxCons01.setX(Spring.constant(5));
                outBoxCons01.setY(Spring.sum(outNorth, Spring.constant(3)));
                outNorth = outBoxCons01.getConstraint(SpringLayout.SOUTH);

                outBoxCons02.setX(Spring.constant(5));
                outBoxCons02.setY(Spring.sum(outNorth, Spring.constant(3)));
                outNorth = outBoxCons02.getConstraint(SpringLayout.SOUTH);

                outBoxCons03.setX(Spring.constant(5));
                outBoxCons03.setY(Spring.sum(outNorth, Spring.constant(3)));
                outNorth = outBoxCons03.getConstraint(SpringLayout.SOUTH);

                outNorth = Spring.sum(outNorth, Spring.constant(30));
            }
            if (!selected.isBuildin()) {
                JButton outPortButtion = new JButton("New port");
                outPortButtion.addActionListener(new AddOutPortListener());
                this.outputPanel.add(outPortButtion, BorderLayout.SOUTH);

                SpringLayout.Constraints outportButtonCons = this.outLayout.getConstraints(outPortButtion);
                outportButtonCons.setX(Spring.constant(5));
                outportButtonCons.setY(Spring.sum(outNorth, Spring.constant(-10)));
            }


            // set pipes
            this.pipePanel.removeAll();
            Spring pipeNorth = Spring.constant(0);
            SpringLayout.Constraints pipeBoxCons = null;
            for (GProcXPipe pipe : selected.getPipes()) {
                if (pipe.isValid()) {
                    Box pipeBox = this.newPipeBox(pipe);
                    this.pipePanel.add(pipeBox);
                    pipeBoxCons = pipeLayout.getConstraints(pipeBox);

                    pipeBoxCons.setX(Spring.constant(5));
                    pipeBoxCons.setY(Spring.sum(pipeNorth, Spring.constant(3)));
                    pipeNorth = pipeBoxCons.getConstraint(SpringLayout.SOUTH);
                }
            }
            if (frame.getSelectedStep().getOutPipe() != null) {
                if (frame.getSelectedStep().getOutPipe().isValid()) {
                    Box pipeBox = this.newPipeBox(frame.getSelectedStep().getOutPipe());
                    this.pipePanel.add(pipeBox);
                    pipeBoxCons = pipeLayout.getConstraints(pipeBox);

                    pipeBoxCons.setX(Spring.constant(5));
                    pipeBoxCons.setY(Spring.sum(pipeNorth, Spring.constant(3)));
                    pipeNorth = pipeBoxCons.getConstraint(SpringLayout.SOUTH);
                }
            }
            JButton pipeButton = new JButton("New pipe");
            pipeButton.addActionListener(new XMenuBar.PipeMenu(this.frame));
            this.pipePanel.add(pipeButton);

            SpringLayout.Constraints pipeButtonCons = this.pipeLayout.getConstraints(pipeButton);
            pipeButtonCons.setX(Spring.constant(5));
            pipeButtonCons.setY(Spring.sum(pipeNorth, Spring.constant(15)));


            // set subpipelines
            this.childPanel.removeAll();
            Spring childNorth = Spring.constant(0);
            SpringLayout.Constraints childBoxCons = null;
            for (GProcXStep child : selected.getChildren()) {
                Box childBox = this.newSubpipelineBox(child);
                this.childPanel.add(childBox);
                childBoxCons = childLayout.getConstraints(childBox);

                childBoxCons.setX(Spring.constant(5));
                childBoxCons.setY(Spring.sum(childNorth, Spring.constant(3)));
                childNorth = childBoxCons.getConstraint(SpringLayout.SOUTH);
            }

            this.repaint();
        }
    }

    private Box newDocBox(GProcXDoc doc) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(new EditDocButton(doc, "Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteDocButton(doc, "Delete"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel(doc.getType()));
        return newBox;
    }

    private Box newNamespaceBox(QName qname) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(new EditElementButton(qname,"Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteNSButton(qname,"Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(qname.toString().substring(6)));
        return newBox;
    }

    private Box newElementBox(ArrayList<QName> qnames, QName qname) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(new EditElementButton(qname,"Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteElementButton(qnames, qname,"Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(qname.toString()));
        return newBox;
    }

    private Box newInBox01(GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        newBox.add(new AddSourceButton(port,"Insert source"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteInPortButton(port,"Delete port"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel("port=\"" + port.getPort() + "\""));

        return newBox;
    }

    private Box newOutBox01(GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        newBox.add(new DeleteOutPortButton(port,"Delete port"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel("port=\"" + port.getPort() + "\""));

        return newBox;
    }

    private Box newInBox02(final GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        JRadioButton trueButton = new JRadioButton("True");
        JRadioButton falseButton = new JRadioButton("False");
        ButtonGroup primaryGroup = new ButtonGroup();
        primaryGroup.add(trueButton);
        primaryGroup.add(falseButton);

        // update the status
        if (port.isPrimary()) {
            trueButton.setSelected(true);
            falseButton.setSelected(false);
        } else {
            trueButton.setSelected(false);
            falseButton.setSelected(true);
        }

        if (!frame.getSelectedStep().isBuildin()) {
            trueButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setPrimary(true);
                    frame.updateSequence();
                }
            });
            falseButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setPrimary(false);
                    frame.updateSequence();
                }
            });
        } else {
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        }

        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel("Primary"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(trueButton);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(falseButton);

        return newBox;
    }

    private Box newInBox03(final GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        JRadioButton trueButton = new JRadioButton("True");
        JRadioButton falseButton = new JRadioButton("False");
        ButtonGroup primaryGroup = new ButtonGroup();
        primaryGroup.add(trueButton);
        primaryGroup.add(falseButton);

        // update the selection status
        if (port.isSequence()) {
            trueButton.setSelected(true);
            falseButton.setSelected(false);
        } else {
            trueButton.setSelected(false);
            falseButton.setSelected(true);
        }

        if (!frame.getSelectedStep().isBuildin()) {
            trueButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setSequence(true);
                    frame.updateSequence();
                }
            });
            falseButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setSequence(false);
                    frame.updateSequence();
                }
            });
        } else {
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        }

        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel("Sequence"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(trueButton);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(falseButton);

        return newBox;
    }

    private Box newPipeBox(GProcXPipe pipe) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(new DeletePipeButton(pipe, "Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        if (pipe.isDefault()) {
            newBox.add(new JLabel("*" + pipe.getInfo()));
        } else {
            newBox.add(new JLabel(pipe.getInfo()));
        }
        return newBox;
    }

    private Box newSubpipelineBox(GProcXStep child) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(new DeleteSubpipelineButton(child, "Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(child.getType() + " - name=\"" + child.getName() + "\""));
        return newBox;
    }

    private JLabel createJLabelWithWrapWidth(int width, JLabel label){
        if (width <= 0 || label == null){
            return label;
        }
        String text = label.getText();
        if (!text.startsWith("<html>")){
            StringBuilder strBuilder = new StringBuilder("<html>");
            strBuilder.append(text);
            strBuilder.append("</html>");
            text = strBuilder.toString();
        }
        label.setText(text);
        View labelView = BasicHTML.createHTMLView(label, label.getText());
        labelView.setSize(width, 100);
        label.setPreferredSize(new Dimension(width, (int) labelView.getMinimumSpan(View.Y_AXIS)));
        return label;
    }

    private class EditDocButton extends JButton {
        public EditDocButton(final GProcXDoc doc, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new DocInputDialog(null, doc);
                }
            });
        }
    }

    private class EditElementButton extends JButton {

        public EditElementButton(final QName qname, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            qname.getLexical() + "=",
                            qname.getValue()
                    );
                    if (inputContent != null) {
                        qname.setValue(inputContent);
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class DeleteDocButton extends JButton {
        public DeleteDocButton(final GProcXDoc doc, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (frame.getSelectedStep() != null) {
                        if (frame.getSelectedStep().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a QName of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this doc?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                frame.getSelectedStep().getDocs().remove(doc);
                                frame.updateInfo();
                            }
                        }
                    }
                }
            });
        }
    }

    private class DeleteElementButton extends JButton {

        public DeleteElementButton(final ArrayList<QName> qnames, final QName qname, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedStep() != null) {
                        if (frame.getSelectedStep().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a QName of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this element?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                qnames.remove(qname);
                                frame.updateInfo();
                            }
                        }
                    }
                }
            });
        }
    }

    private class DeleteNSButton extends JButton {

        public DeleteNSButton(final QName ns, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedStep() != null) {

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Remove this namespace?",
                                "Warning",
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        if (result == 0) {
                            frame.getSelectedStep().getNamespaces().remove(ns);
                            frame.updateInfo();
                        }
                    }
                }
            });
        }
    }

    private class DeleteSourceButton extends JButton {

        public DeleteSourceButton(final GProcXPort port, final IOSource source, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(
                            null,
                            "Remove this source?",
                            "Warning",
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );

                    if (result == 0) {
                        port.getSources().remove(source);
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class DeleteInPortButton extends JButton {

        public DeleteInPortButton(final GProcXPort port, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedStep() != null) {
                        if (frame.getSelectedStep().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a port of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this element?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                frame.getSelectedStep().getInputs().remove(port);
                                frame.updateInfo();
                            }
                        }
                    }

                }
            });
        }
    }

    private class DeleteOutPortButton extends JButton {

        public DeleteOutPortButton(final GProcXPort port, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedStep() != null) {
                        if (frame.getSelectedStep().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a QName of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this element?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                frame.getSelectedStep().getOutputs().remove(port);
                                frame.updateInfo();
                            }
                        }
                    }

                }
            });
        }
    }

    private class DeletePipeButton extends JButton {
        public DeletePipeButton(final GProcXPipe pipe, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedStep() != null) {

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Remove this pipe?",
                                "Warning",
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        if (result == 0) {
                            if (pipe == frame.getSelectedStep().getOutPipe()) {
                                frame.getSelectedStep().setOutPipe(null);
                                frame.getSelectedStep().setOutPipeFlag(false);
                            } else {
                                frame.getSelectedStep().deletePipe(pipe);
                            }
                            frame.updateInfo();
                        }
                    }

                }
            });
        }
    }

    private class DeleteSubpipelineButton extends JButton {

        public DeleteSubpipelineButton(final GProcXStep child, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedStep() != null) {

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Remove this subpipeline?",
                                "Warning",
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        if (result == 0) {
                            if (child != null) {
                                frame.getSelectedStep().deleteChild(child);
                            }
                            frame.getCurrentPanel().repaint();
                            frame.setSelectedStep(frame.getMainStep());
                        }
                    }
                }
            });
        }
    }

    private class AddSourceButton extends JButton {

        public AddSourceButton(final GProcXPort port, String text) {
            super(text);

            final Object[] selectionValues = new Object[]{"p:document", "p:data", "p:inline", "p:empty"};
            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Object inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please select the type of source:",
                            "Source",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            selectionValues,
                            selectionValues[0]
                    );

                    if (inputContent != null) {
                        port.addSource(new IOSource((String)inputContent));
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class AddDocListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {

                if (frame.getSelectedStep().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined info to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the type:",
                            "p:documentation"
                    );

                    if (inputContent != null) {
                        frame.getSelectedStep().addDoc(new GProcXDoc(inputContent, "empty"));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class AddElementListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {

                if (frame.getSelectedStep().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined QName to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the lexical:",
                            "untitled"
                    );

                    if (inputContent != null) {
                        frame.getSelectedStep().addQName(new QName(inputContent, ""));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class AddNSListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {
                String inputContent = JOptionPane.showInputDialog(
                        null,
                        "Please enter the namespace:",
                        "xmlns:"
                );

                if (inputContent != null) {
                    frame.getSelectedStep().addNamespace(new QName(inputContent, ""));
                    frame.updateInfo();
                }
            }
        }
    }

    private class AddInPortListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {

                if (frame.getSelectedStep().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined port to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the port:",
                            "untitled"
                    );

                    if (inputContent != null) {
                        frame.getSelectedStep().getInputs().add(new InPort(frame.getSelectedStep(), inputContent, false, false, "not specified"));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class AddOutPortListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {

                if (frame.getSelectedStep().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined port to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the port:",
                            "untitled"
                    );

                    if (inputContent != null) {
                        frame.getSelectedStep().getOutputs().add(new InPort(frame.getSelectedStep(), inputContent, false, false, "not specified"));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class InlineDialog extends JDialog {
        public InlineDialog(JFrame f, final IOSource source) {
            super(f, source.getSourceType());
            final JTextArea text = new JTextArea(source.getInline());
            this.add(text);

            this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/3,Toolkit.getDefaultToolkit().getScreenSize().height/3,
                    Toolkit.getDefaultToolkit().getScreenSize().width/4,Toolkit.getDefaultToolkit().getScreenSize().height/4);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setVisible(true);

            this.addWindowListener(new WindowAdapter()
            {
                public void windowClosed(WindowEvent e)
                {
                    source.setInline(text.getText());
                    frame.updateInfo();
                }
            });
        }
    }

    private class DocInputDialog extends JDialog {

        public DocInputDialog(JFrame f, final GProcXDoc doc) {
            super(f, doc.getType());
            final JTextArea text = new JTextArea(doc.getContent());
            this.add(text);

            this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width/3,Toolkit.getDefaultToolkit().getScreenSize().height/3,
                    Toolkit.getDefaultToolkit().getScreenSize().width/4,Toolkit.getDefaultToolkit().getScreenSize().height/4);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setVisible(true);

            this.addWindowListener(new WindowAdapter()
            {
                public void windowClosed(WindowEvent e)
                {
                    doc.setContent(text.getText());
                    frame.updateInfo();
                }
            });
        }
    }
}
