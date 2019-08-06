package gprocx.core;

import gprocx.step.GProcXPipe;
import gprocx.step.GProcXStep;

import java.io.Serializable;
import java.util.ArrayList;

public class GProcXPort implements Serializable {
    protected GProcXStep parent;
    protected ArrayList<IOSource> sources = new ArrayList<IOSource>();
    protected ArrayList<GProcXPipe> pipes = new ArrayList<GProcXPipe>();
    protected ArrayList<QName> qnames = new ArrayList<QName>();
    protected ArrayList<QName> namespaces = new ArrayList<QName>();


    public GProcXPort(GProcXStep parent, String port, boolean primary, boolean sequence, String kind) {
        this.sources = new ArrayList<IOSource>();
        this.qnames = new ArrayList<QName>();
        this.pipes = new ArrayList<GProcXPipe>();

        this.parent = parent;
        this.addQName(new QName("port", port));
        this.addQName(new QName("primary", String.valueOf(primary)));
        this.addQName(new QName("sequence", String.valueOf(sequence)));
    }

    public GProcXPort(GProcXStep parent) {
        this.parent = parent;
    }

    public GProcXPort(GProcXPort port) {
        this.parent = port.getParent();
        for (IOSource source : port.getSources()) {
            this.sources.add(new IOSource(source));
        }
        for (QName q : port.getQNames()) {
            this.qnames.add(new QName(q));
        }
        for (QName ns : port.getNamespaces()) {
            this.namespaces.add(new QName(ns));
        }
    }

    public void addQName(QName qname) {
        this.qnames.add(qname);
    }

    public ArrayList<QName> getQNames() {
        return this.qnames;
    }

    public void setParent(GProcXStep parent) {
        this.parent = parent;
    }

    public GProcXStep getParent() {
        return parent;
    }

    public ArrayList<QName> getNamespaces() {
        return namespaces;
    }

    public void addNamespace(QName ns) {
        this.namespaces.add(ns);
    }

    public ArrayList<GProcXPipe> getPipes() {
        return pipes;
    }

    public GProcXPipe getDefaultPipe() {
        for (GProcXPipe pipe : this.getPipes()) {
            if (pipe.isDefault()) {
                return pipe;
            }
        }
        return null;
    }

    public boolean hasDefineNS(QName ns) {
        if (this.getParent() != null) {
           // if (this.getParent().getType().equals("p:declare-step") || this.getParent().getType().equals("p:pipeline")) {
            //    for ()
           // }
            if (this.getParent().hasDefineNS(ns)) {
                return true;
            }
            for (QName namespace : this.getParent().getNamespaces()) {
                if (namespace.getLexical().equals(ns.getLexical())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addSource(IOSource source) {
        this.sources.add(source);
    }

    public void addPipe(GProcXPipe pipe) {
        this.pipes.add(pipe);
    }

    public void setPort(String port) {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("port")) {
                qname.setValue(port);
                return;
            }
        }
        this.addQName(new QName("port", port));
    }

    public String getPort() {
        return null;
    }

    public boolean isPrimary() { return false; }

    public boolean isSequence() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("sequence")) {
                return Boolean.valueOf(qname.getValue());
            }
        }
        return false;
    }

    public boolean isBasic() {
        if (sources.isEmpty() && pipes.isEmpty()) {
            return true;
        } else if (sources.isEmpty() && !pipes.isEmpty()) {
            for (GProcXPipe pipe : pipes) {
                if (!pipe.isDefault()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setPrimary(boolean primary) {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("primary")) {
                qname.setValue(String.valueOf(primary));
                return;
            }
        }
        this.addQName(new QName("primary", String.valueOf(primary)));
    }

    public void setSequence(boolean sequence) {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("sequence")) {
                qname.setValue(String.valueOf(sequence));
                return;
            }
        }
        this.addQName(new QName("sequence", String.valueOf(sequence)));
    }

    public ArrayList<IOSource> getSources() {
        return this.sources;
    }

    public String toString(int retract) {
        return "";
    }
}
