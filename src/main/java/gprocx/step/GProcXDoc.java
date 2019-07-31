package gprocx.step;

public class GProcXDoc {

    private String type;
    private String content = "";

    public GProcXDoc(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String toString(int retract) {
        // if the content is empty
        if (this.getContent().equals("empty")) {
            return "";
        }

        String code = "";

        // print the retract
        for (int i = 0; i < retract; i++) {
            code += "    ";
        }
        code += "<" + this.getType() + ">\n";
        for (int i = 0; i < retract + 1; i++) {
            code += "    ";
        }
        code += this.getContent() + "\n";
        for (int i = 0; i < retract; i++) {
            code += "    ";
        }
        code += "</" + this.getType() + ">\n\n";


        return code;
    }
}
