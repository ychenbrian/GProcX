package gprocx.core;

import gprocx.step.Pipe;

import java.util.ArrayList;

public class InPort extends Port {

    public InPort(String port, boolean primary, boolean sequence, String kind) {
        super(port, primary, sequence, kind);
    }

    public String toString() {
        String code = "";

        code += "<" + "p:input";

        for (QName qname : qnames) {

            if (!qname.getValue().equals("")) {
                code += " " + qname.toString();
            }
        }

        if (!sources.isEmpty() || !pipes.isEmpty()) {
            code += ">\n";

            for (Pipe pipe : pipes) {
                for (int i = 0; i < this.retract + 2; i++) {
                    code += "    ";
                }
                code += pipe.toString();
            }

            for (IOSource source : sources) {
                for (int i = 0; i < this.retract + 2; i++) {
                    code += "    ";
                }
                code += source.toString();
            }

            for (int i = 0; i < this.retract + 1; i++) {
                code += "    ";
            }
            code += "</p:input>\n";
        } else {
            code += "/>\n";
        }

        return code;
    }
}
