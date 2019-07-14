package gprocx.core;

public class OutPort extends Port {

    public OutPort(String port, boolean primary, boolean sequence, String kind) {
        super(port, primary, sequence, kind);
    }

    public String toString() {
        String code = "";

        code += "<" + "p:output";

        for (QName qname : qnames) {

            if (!qname.getValue().equals("")) {
                code += " " + qname;
            }
        }

        if (!sources.isEmpty()) {
            code += ">\n";

            for (IOSource source : sources) {
                for (int i = 0; i < this.retract + 1; i++) {
                    code += "    ";
                }
                code += source.toString();
            }

            for (int i = 0; i < this.retract; i++) {
                code += "    ";
            }
            code += "</p:output>\n";
        } else {
            code += "/>\n";
        }

        return code;
    }
}
