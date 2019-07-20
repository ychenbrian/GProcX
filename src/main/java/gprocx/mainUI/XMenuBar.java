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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class XMenuBar extends JMenuBar {

    private XFrame frame;

    public XMenuBar(XFrame frame) {
        this.frame = frame;

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New"); fileMenu.add(newMenuItem);
        JMenuItem openMenuItem = new JMenuItem("Open"); fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        JMenuItem importMenuItem = new JMenuItem("Import"); fileMenu.add(importMenuItem);
        JMenuItem exportMenuItem = new JMenuItem("Export"); fileMenu.add(exportMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit"); fileMenu.add(exitMenuItem);

        JMenu editMenu = new JMenu("Edit");

        JMenu insertMenu = new JMenu("Insert");
        JMenuItem pipelineMenuItem = new JMenuItem("GProcXPipeline"); insertMenu.add(pipelineMenuItem);
        JMenuItem atomicMenuItem = new JMenuItem("Atomic step"); insertMenu.add(atomicMenuItem);
        JMenuItem pipeMenuItem = new JMenuItem("GProcXPipe"); insertMenu.add(pipeMenuItem);

        importMenuItem.addActionListener(new ImportMenu());
        exportMenuItem.addActionListener(new ExportMenu());
        atomicMenuItem.addActionListener(new AtomicMenu());
        pipeMenuItem.addActionListener(new PipeMenu());

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

        JOptionPane.showMessageDialog(
                null,
                "You are going to save the code displayed in the code window.",
                "Save",
                JOptionPane.INFORMATION_MESSAGE
        );

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("untitled.xpl"));

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
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
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            XProcEngine engine = XProcEngine.newXProc();
            XProcCompiler compiler = engine.newXProcCompiler();
            XProcPipeline pipeline = null;
            try {
                XProcSource pipelineSource = new XProcSource(docs);
                pipeline = compiler.compile(pipelineSource);
            } catch (XProcInterfaceException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (XProcSecurityException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (XProcFilesystem.UnsupportedXMLVersionException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (XProcCompiler.XProcCompilerException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Element mainEle = (Element) docs.getChild(0);
            GProcXPipeline newPipeline = new GProcXPipeline(frame);
            GProcXProcessor.setPipeline(null, newPipeline, mainEle);

            frame.setMainPipeline(newPipeline);
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

    private class AtomicMenu implements ActionListener {
    	
    	public void actionPerformed(ActionEvent e) {
            Object[] selectionValues = StepInfo.getStepTypes();

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the atomic step:",
                    "Atomic step",
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

    private class PipeMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            frame.setDrawPipe01Active(true);
        }
    }



}
