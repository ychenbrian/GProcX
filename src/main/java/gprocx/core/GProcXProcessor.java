package gprocx.core;

import gprocx.step.GProcXPipe;
import gprocx.step.GProcXPipeline;
import gprocx.step.StepInfo;
import nu.xom.Element;
import nu.xom.Elements;

import java.awt.*;
import java.util.ArrayList;

public class GProcXProcessor {

    public static void setPipeline(GProcXPipeline parent, GProcXPipeline pipeline, Element pipelineEle) {
        pipeline.setType(pipelineEle.getQualifiedName());
        pipeline.setIsAtomic(StepInfo.isAtomic(pipeline.getType()));
        StepInfo.setPipelineInfo(pipeline);
        for (QName qname : getQNames(pipelineEle)) {
            pipeline.addQName(new QName(qname));
        }
        for (int i = 0; i < pipelineEle.getNamespaceDeclarationCount(); i++) {
            pipeline.addNamespace(new QName("xmlns", pipelineEle.getNamespacePrefix(i),
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
                GProcXPort input = new InPort();
                setPort(parent, pipeline, input, childEle);
                pipeline.addInput(input);
            } else if (childEle.getQualifiedName().equals("p:output")) {
                GProcXPort output = new OutPort();
                setPort(parent, pipeline, output, childEle);
                pipeline.addOutput(output);
            }
        }
        for (Element childEle : childEles) {
            if (!childEle.getQualifiedName().equals("p:input") && !childEle.getQualifiedName().equals("p:output")) {
                GProcXPipeline newPipeline = new GProcXPipeline(pipeline.getFrame());
                y += 2*h;
                x += 15;
                if (y >= (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
                    x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 5 + w*2;
                    y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 16;
                }
                newPipeline.setShape(x, y, w, h);

                setPipeline(pipeline, newPipeline, childEle);
                pipeline.addChildren(newPipeline);
            }
        }
        pipeline.updateInfo();
    }

    public static void setPort(GProcXPipeline parent, GProcXPipeline pipeline, GProcXPort port, Element portEle) {
        for (QName qname : getQNames(portEle)) {
            port.addQName(new QName(qname));
        }
        for (int i = 0; i < portEle.getNamespaceDeclarationCount(); i++) {
            port.addNamespace(new QName("xmlns", portEle.getNamespacePrefix(i),
                    portEle.getNamespaceURI(portEle.getNamespacePrefix(i))));
        }

        Elements sourceEles = portEle.getChildElements();
        for (Element sourceEle : sourceEles) {
            if (sourceEle.getQualifiedName().equals("p:pipe")) {
                GProcXPipe pipe = new GProcXPipe(parent.getFrame());
                pipe.setParent(port);
                pipe.setToPipeline(pipeline, parent == null);
                pipe.setToPort(port);

                for (int i = 0; i < sourceEle.getNamespaceDeclarationCount(); i++) {
                    pipe.addNamespace(new QName("xmlns", sourceEle.getNamespacePrefix(i),
                            sourceEle.getNamespaceURI(sourceEle.getNamespacePrefix(i))));
                }

                ArrayList<QName> qnames = getQNames(sourceEle);
                GProcXPipeline from = null;
                for (QName qname : qnames) {
                    if (qname.getUriLexical().equals("step")) {
                        from = parent.findPipeline(qname.getValue());
                        pipe.setFromPipeline(from, from == parent);
                    }
                }
                for (QName qname : qnames) {
                    if (qname.getUriLexical().equals("port")) {
                        pipe.setFromPort(from.findPort(qname.getValue()));
                    }
                }
                for (QName qname : qnames) {
                    if (!qname.getUriLexical().equals("port") && !qname.getUriLexical().equals("step")) {
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
            source.addNamespace(new QName("xmlns", sourceEle.getNamespacePrefix(i),
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

    public static ArrayList<Element> findElements(Elements elements, String qualifiedName) {
        ArrayList<Element> select = new ArrayList<Element>();
        for (Element element : elements) {
            if (element.getQualifiedName().equals(qualifiedName)) {
                select.add(element);
            }
        }
        return select;
    }

    public static ArrayList<Element> getElementArray(Elements elements) {
        ArrayList<Element> select = new ArrayList<Element>();
        for (Element element : elements) {
            select.add(element);
        }
        return select;
    }

    public static ArrayList<QName> getQNames(Element element) {
        ArrayList<QName> qnames = new ArrayList<QName>();
        for (int i = 0; i < element.getAttributeCount(); i++) {
            qnames.add(new QName("", element.getAttribute(i).getQualifiedName(), element.getAttribute(i).getValue()));
        }
        return qnames;
    }
}
