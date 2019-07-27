package gprocx.core;

public class GProcXOption {

    private String name;
    private String select;
    private boolean required;

    public GProcXOption(String name, String select, boolean required) {
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
