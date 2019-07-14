package gprocx.core;

import gprocx.step.Pipe;

import java.util.ArrayList;

public class Port {

    protected String port;
    protected boolean primary;
    protected boolean sequence;
    protected String kind;
    protected int retract = 0;

    protected ArrayList<IOSource> sources;
    protected ArrayList<Pipe> pipes;
    protected ArrayList<QName> qnames;

    public Port(String port, boolean primary, boolean sequence, String kind) {
        this.sources = new ArrayList<IOSource>();
        this.qnames = new ArrayList<QName>();
        this.pipes = new ArrayList<Pipe>();

        this.port = port;
        this.primary = primary;
        this.sequence = sequence;
        this.kind = kind;

        this.qnames = new ArrayList<QName>();
        this.addQName(new QName("", "port", this.port));
    }

    public void addQName(QName qname) {
        this.qnames.add(qname);
    }

    public void addSource(IOSource source) {
        this.sources.add(source);
    }

    public void addPipes(Pipe pipe) {
        this.pipes.add(pipe);
    }

    public String getPort() {
        return port;
    }

    public void setRetract(int retract) {
        this.retract = retract;
    }

    public int getRetract() {
        return retract;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isSequence() {
        return sequence;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setSequence(boolean sequence) {
        this.sequence = sequence;
    }

    public String getKind() {
        return kind;
    }

    public ArrayList<IOSource> getSources() {
        return this.sources;
    }

    public String toStringO(ArrayList<Pipe> pipes) { return ""; }
}
