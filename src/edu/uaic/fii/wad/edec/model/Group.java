package edu.uaic.fii.wad.edec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {

    private String id;
    private String name;
    private String description;
    private List<Rule> rules;

    public Group() {
        this.rules = Collections.synchronizedList(new ArrayList<Rule>());
    }

    public Group(String id, String name, ArrayList<Rule> rules, String description) {
        this.id = id;
        this.name = name;
        this.rules = rules;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }
}
