package gprocx.core;

import java.util.ArrayList;

public class IOSource {

    private GProcXPort parent;
    private ArrayList<QName> qnames = new ArrayList<QName>();
    private ArrayList<QName> namespaces = new ArrayList<QName>();
    private String inline = null;
    private String sourceType;

    public IOSource(String sourceType) {
        this.sourceType = sourceType;
        this.qnames = new ArrayList<QName>();

        if (this.sourceType.equals("p:document")) {
            this.qnames.add(new QName("", "href", ""));
        } else if (sourceType.equals("p:data")) {
            this.qnames.add(new QName("", "href", ""));
            this.qnames.add(new QName("", "wrapper", ""));
            this.qnames.add(new QName("", "wrapper-prefix", ""));
            this.qnames.add(new QName("", "wrapper-namespace", ""));
            this.qnames.add(new QName("", "content-type", ""));
        } else if (sourceType.equals("p:inline")) {
            this.qnames.add(new QName("", "exclude-inline-prefixes", ""));
        }
    }

    public IOSource() {}

    public void addQName(QName qname) {
        this.qnames.add(qname);
    }

    public ArrayList<QName> getQNames() {
        return this.qnames;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public void setParent(GProcXPort parent) {
        this.parent = parent;
    }

    public GProcXPort getParent() {
        return parent;
    }

    public boolean hasDefineNS(QName ns) {
        if (this.getParent() != null) {
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

    public ArrayList<QName> getNamespaces() {
        return namespaces;
    }

    public void addNamespace(QName ns) {
        this.namespaces.add(ns);
    }

    public void setInline(String inline) {
        this.inline = inline;
    }

    public String getInline() {
        return inline;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public String toString(int retract) {
        String code = "";
        for (int i = 0; i < retract; i++) {
            code += "    ";
        }
        code += "<" + this.getSourceType();

        for (QName namespace : this.getNamespaces()) {
            if (!hasDefineNS(namespace)) {
                if (!namespace.getValue().equals("")) {
                    code += " " + namespace;
                }
            }
        }
        for (QName qname : getQNames()) {
            if (!qname.getValue().equals("")) {
                code += " " + qname.toString();
            }
        }

        if (this.getSourceType().equals("p:inline")) {
            code += ">\n";
            for (int i = 0; i < retract + 1; i++) {
                code += "    ";
            }
            code += this.getInline() + "\n";
            for (int i = 0; i < retract; i++) {
                code += "    ";
            }
            code += "</p:inline>\n";
        } else {
            code += "/>\n";
        }
        return code;
    }
}
