package gprocx.core;

import gprocx.mainUI.XFrame;
import gprocx.step.GProcXDoc;
import gprocx.step.GProcXPipe;
import gprocx.step.GProcXStep;
import gprocx.step.StepInfo;
import nu.xom.Element;
import nu.xom.Elements;

import java.awt.*;
import java.util.ArrayList;

public class GProcXProcessor {

    public static void setPipeline(XFrame frame, GProcXStep parent, GProcXStep pipeline, Element pipelineEle) {
        pipeline.setType(pipelineEle.getQualifiedName());
        StepInfo.setStepInfo(frame, pipeline);
        for (QName qname : getQNames(pipelineEle)) {
            pipeline.addQName(new QName(qname));
        }
        for (int i = 0; i < pipelineEle.getNamespaceDeclarationCount(); i++) {
            pipeline.addNamespace(new QName("xmlns:" + pipelineEle.getNamespacePrefix(i),
                    pipelineEle.getNamespaceURI(pipelineEle.getNamespacePrefix(i))));
        }

        int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 5;
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 16;
        int w = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 15;
        int h = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 18;

        // childEle might be ports or sub-pipelines
        Elements childEles = pipelineEle.getChildElements();
        for (Element childEle : childEles) {
            if (childEle.getQualifiedName().equals("p:input")) {
                GProcXPort input = new InPort(pipeline);
                setPort(parent, pipeline, input, childEle);
                pipeline.addInput(input);
            } else if (childEle.getQualifiedName().equals("p:output")) {
                GProcXPort output = new OutPort(pipeline);
                setPort(parent, pipeline, output, childEle);
                pipeline.addOutput(output);
            }
        }
        for (Element childEle : childEles) {
            if (!childEle.getQualifiedName().equals("p:input") && !childEle.getQualifiedName().equals("p:output")) {

                if (childEle.getQualifiedName().equals("p:documentation")) {
                    pipeline.addDoc(new GProcXDoc("p:documentation", childEle.getChild(0).toXML()));
                    continue;
                }
                if (childEle.getQualifiedName().equals("p:pipeinfo")) {
                    pipeline.addDoc(new GProcXDoc("p:pipeinfo", childEle.getChild(0).toXML()));
                    continue;
                }

                GProcXStep newPipeline = new GProcXStep();
                y += 2*h;
                x += 15;
                if (y >= (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
                    x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 5 + w*2;
                    y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 16;
                }
                newPipeline.setShape(x, y, w, h);

                setPipeline(frame, pipeline, newPipeline, childEle);
                frame.addStep(newPipeline);
                pipeline.addChildren(frame, newPipeline);
            }
        }
        pipeline.updateInfo();
    }

    public static void setPort(GProcXStep parent, GProcXStep pipeline, GProcXPort port, Element portEle) {
        for (QName qname : getQNames(portEle)) {
            port.addQName(new QName(qname));
        }
        for (int i = 0; i < portEle.getNamespaceDeclarationCount(); i++) {
            port.addNamespace(new QName("xmlns:" + portEle.getNamespacePrefix(i),
                    portEle.getNamespaceURI(portEle.getNamespacePrefix(i))));
        }

        Elements sourceEles = portEle.getChildElements();
        for (Element sourceEle : sourceEles) {
            if (sourceEle.getQualifiedName().equals("p:pipe")) {
                GProcXPipe pipe = new GProcXPipe();
                pipe.setParent(port);
                pipe.setToStep(pipeline, parent == null);
                pipe.setToPort(port);

                for (int i = 0; i < sourceEle.getNamespaceDeclarationCount(); i++) {
                    pipe.addNamespace(new QName("xmlns:" + sourceEle.getNamespacePrefix(i),
                            sourceEle.getNamespaceURI(sourceEle.getNamespacePrefix(i))));
                }

                ArrayList<QName> qnames = getQNames(sourceEle);
                GProcXStep from = null;
                for (QName qname : qnames) {
                    if (qname.getLexical().equals("step")) {
                        from = parent.findStep(qname.getValue());
                        pipe.setFromStep(from, from == parent);
                    }
                }
                for (QName qname : qnames) {
                    if (qname.getLexical().equals("port")) {
                        pipe.setFromPort(from.findPort(qname.getValue()));
                    }
                }
                for (QName qname : qnames) {
                    if (!qname.getLexical().equals("port") && !qname.getLexical().equals("step")) {
                        pipe.addQName(new QName(qname));
                    }
                }

                port.addPipe(pipe);
            } else {
                IOSource source = new IOSource();
                source.setParent(port);
                setSource(source, sourceEle);
                port.addSource(source);
            }
        }
    }

    public static void setSource(IOSource source, Element sourceEle) {
        for (int i = 0; i < sourceEle.getNamespaceDeclarationCount(); i++) {
            source.addNamespace(new QName("xmlns:" + sourceEle.getNamespacePrefix(i),
                    sourceEle.getNamespaceURI(sourceEle.getNamespacePrefix(i))));
        }
        for (QName qname : getQNames(sourceEle)) {
            source.addQName(new QName(qname));
        }
        source.setSourceType(sourceEle.getQualifiedName());
        if (sourceEle.getQualifiedName().equals("p:inline")) {
            source.setInline(sourceEle.getChildElements().get(0).toXML());
        }
    }

    public static ArrayList<QName> getQNames(Element element) {
        ArrayList<QName> qnames = new ArrayList<QName>();
        for (int i = 0; i < element.getAttributeCount(); i++) {
            qnames.add(new QName(element.getAttribute(i).getQualifiedName(), element.getAttribute(i).getValue()));
        }
        return qnames;
    }
}
