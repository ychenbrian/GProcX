package gprocx.mainUI;

import com.xml_project.morganaxproc.*;
import com.xml_project.morganaxproc.filesystem.XProcFilesystem;
import com.xml_project.morganaxproc.security.XProcSecurityException;
import gprocx.core.GProcXProcessor;
import gprocx.step.GProcXPipeline;
import gprocx.step.StepInfo;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class XMenuBar extends JMenuBar {

    private XFrame frame;

    public XMenuBar(XFrame frame) {
        this.frame = frame;

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New"); fileMenu.add(newMenuItem);
        //JMenuItem openMenuItem = new JMenuItem("Open"); fileMenu.add(openMenuItem);
        //JMenuItem saveMenuItem = new JMenuItem("Save"); fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        JMenuItem importMenuItem = new JMenuItem("Import"); fileMenu.add(importMenuItem);
        JMenuItem exportMenuItem = new JMenuItem("Export"); fileMenu.add(exportMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit"); fileMenu.add(exitMenuItem);

        JMenu editMenu = new JMenu("Edit");

        JMenu insertMenu = new JMenu("Insert");
        JMenuItem pipelineMenuItem = new JMenuItem("Pipeline"); insertMenu.add(pipelineMenuItem);
        JMenuItem atomicMenuItem = new JMenuItem("Atomic step"); insertMenu.add(atomicMenuItem);
        JMenuItem otherStepMenuItem = new JMenuItem("Other step"); insertMenu.add(otherStepMenuItem);
        JMenuItem pipeMenuItem = new JMenuItem("Pipe"); insertMenu.add(pipeMenuItem);

        newMenuItem.addActionListener(new NewMenu());
        importMenuItem.addActionListener(new ImportMenu());
        exportMenuItem.addActionListener(new ExportMenu());
        pipelineMenuItem.addActionListener(new PipelineMenu(this.frame));
        atomicMenuItem.addActionListener(new AtomicMenu(this.frame));
        otherStepMenuItem.addActionListener(new OtherStepMenu(this.frame));
        pipeMenuItem.addActionListener(new PipeMenu(this.frame));

        this.add(fileMenu);
        this.add(editMenu);
        this.add(insertMenu);
    }

    private File showFileOpenDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("xpl(*.xpl)", "xpl"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
    }

    private File showFileSaveDialog() {

        frame.showInformationMessage("You are going to save the current pipeline.");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("untitled.xpl"));

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
    }

    private class NewMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            final Object[] selectionValues = new Object[]{"p:declare-step", "p:pipeline", "p:library"};

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
                GProcXPipeline newPipeline = new GProcXPipeline(frame);

                newPipeline.setType((String)inputContent);
                StepInfo.setPipelineInfo(frame, newPipeline);

                frame.addMainPipeline(newPipeline);
            }
        }
    }

    private class ImportMenu implements ActionListener {

        String test2 = "<p:declare-step xmlns:p=\"http://www.w3.org/ns/xproc\" xmlns:c=\"http://www.w3.org/ns/xproc-step\" name=\"myPipeline\" version=\"1.0\">" +
                "<p:input port=\"source\" primary=\"true\">\n" +
                "        <p:document href=\"BookStore.xml\" xmlns:c=\"http://www.w3.org/ns/xproc-step\"/>" +
                "    </p:input>\n" +
                "    <p:output port=\"result\"/>\n" +
                "    \n" +
                "    <p:delete match=\"/BookStore/Book/Title\" name=\"step1\" xmlns:exf=\"http://exproc.org/standard/functions\"/>\n" +
                "</p:declare-step9>";


        public void actionPerformed(ActionEvent event) {

            File file = showFileOpenDialog();
            if (file == null) {
                return;
            }

            Builder parser = new Builder();
            Document docs = null;
            try {
                docs = parser.build(file);
                //docs = parser.build(new StringReader(test2));
            } catch (ParsingException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            } catch (IOException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            }

            XProcEngine engine = XProcEngine.newXProc();
            XProcCompiler compiler = engine.newXProcCompiler();
            XProcPipeline pipeline = null;
            try {
                XProcSource pipelineSource = new XProcSource(docs);
                pipeline = compiler.compile(pipelineSource);
            } catch (XProcInterfaceException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            } catch (XProcSecurityException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            } catch (XProcFilesystem.UnsupportedXMLVersionException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            } catch (IOException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            } catch (XProcCompiler.XProcCompilerException e) {
                frame.showErrorMessage(e.getMessage());
                return;
            } catch (NullPointerException e) {
                frame.showErrorMessage("This is not a runnable XProc program.");
                return;
            }

            Element mainEle = (Element) docs.getChild(0);
            GProcXPipeline newPipeline = new GProcXPipeline(frame);
            GProcXProcessor.setPipeline(frame, null, newPipeline, mainEle);

            frame.addMainPipeline(newPipeline);
        }
    }

    private class ExportMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            File file = showFileSaveDialog();

            if (file == null) {
                return;
            }
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(file));
                ps.println(frame.getCode());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static class PipelineMenu implements ActionListener {

        XFrame frame;

        public PipelineMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {

            if (!frame.getMainPipeline().getType().equals("p:library")) {
                XFrame.showErrorMessage("You can only insert pipelines into p:library.");
                return;
            }
            Object[] selectionValues = new String[]{"p:declare-step", "p:pipeline"};

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the type:",
                    "Pipeline",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                frame.setNewStep((String) inputContent);
                frame.setDrawStepActive(true);
            }

        }
    }

    public static class AtomicMenu implements ActionListener {

        XFrame frame;

        public AtomicMenu(XFrame frame) {
            this.frame = frame;
        }

    	public void actionPerformed(ActionEvent e) {

            if (frame.getMainPipeline().getType().equals("p:library")) {
                XFrame.showErrorMessage("You cannot insert steps into p:library.");
                return;
            }
            Object[] selectionValues = StepInfo.getAtomicTypes();

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the atomic step:",
                    "Atomic steps",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                frame.setNewStep((String) inputContent);
                frame.setDrawStepActive(true);
            }

        }
    }

    public static class OtherStepMenu implements ActionListener {

        XFrame frame;

        public OtherStepMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {

            if (frame.getMainPipeline().getType().equals("p:library")) {
                XFrame.showErrorMessage("You cannot insert steps into p:library.");
                return;
            }
            Object[] selectionValues = StepInfo.getOtherTypes();

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the step:",
                    "Other steps",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                frame.setNewStep((String) inputContent);
                frame.setDrawStepActive(true);
            }

        }
    }

    public static class PipeMenu implements ActionListener {

        XFrame frame;

        public PipeMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            if (frame.getMainPipeline().getType().equals("p:library")) {
                XFrame.showErrorMessage("You cannot insert pips into p:library.");
                return;
            }
            frame.setDrawPipe01Active(true);
            frame.showInformationMessage("Select the FROM step or port.");
        }
    }

}
