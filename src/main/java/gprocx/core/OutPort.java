package gprocx.core;

import gprocx.step.GProcXPipe;
import gprocx.step.GProcXStep;

import java.io.Serializable;

public class OutPort extends GProcXPort implements Serializable {

    public OutPort(GProcXStep parent, String port, boolean primary, boolean sequence, String kind) {
        super(parent, port, primary, sequence, kind);
    }

    public OutPort(GProcXStep parent) {
        super(parent);
    }

    public OutPort(GProcXPort out) {
        super(out);
    }

    @Override
    public String getPort() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("port")) {
                return qname.getValue();
            }
        }
        return "result";
    }

    @Override
    public boolean isPrimary() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("primary")) {
                return Boolean.valueOf(qname.getValue());
            }
        }

        if (this.parent.getInputs().size() <= 1) {
            return true;
        }

        return false;
    }

    public String toString(int retract) {
        String code = "";

        for (int i = 0; i < retract; i++) {
            code += "    ";
        }
        code += "<" + "p:output";

        for (QName namespace : this.namespaces) {
            if (!hasDefineNS(namespace)) {
                if (!namespace.getValue().equals("")) {
                    code += " " + namespace;
                }
            }
        }
        for (QName qname : qnames) {
            if (qname.getLexical().equals("primary")) {
                if (this.getPort().equals("result") && qname.getValue().equals("true")) {
                    continue;
                }
            }
            if (qname.getLexical().equals("sequence") && qname.getValue().equals("false")) {
                continue;
            }
            if (!qname.getValue().equals("")) {
                code += " " + qname;
            }
        }

        if (!sources.isEmpty()) {
            code += ">\n";

            for (GProcXPipe pipe : pipes) {
                if (!pipe.isValid()) {
                    continue;
                }
                if (!pipe.isDefault()) {
                    code += pipe.toString(retract + 1);
                }
            }

            for (int i = 0; i < retract; i++) {
                code += "    ";
            }
            code += "</p:output>\n";
        } else {
            code += "/>\n";
        }

        return code;
    }
}
