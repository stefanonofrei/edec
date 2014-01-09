package edu.uaic.fii.wad.edec.model;

public class Rule {

    /**
     * 0 - company
     * 1 - product
     * 2 - ingredient
     */
    private int type;
    private String name;
    private int reason;

    public Rule(int type, String name, int reason) {
        this.type = type;
        this.name = name;
        this.reason = reason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (name.equals(rule.name) && type == rule.type) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + name.hashCode();
        result = 31 * result + reason;
        return result;
    }
}
