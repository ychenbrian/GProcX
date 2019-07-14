package gprocx.core;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StepOption {

    private String name;
    private String select;
    private boolean required;

    public StepOption(String name, String select, boolean required) {
        this.name = name;
        this.select = select;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String getSelect() {
        return select;
    }
}
