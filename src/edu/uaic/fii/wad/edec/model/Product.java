package edu.uaic.fii.wad.edec.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {

    private String barcode;
    private String name;
    private String image;
    private Company company;
    private List<Ingredient> ingredients;
    private List<Reason> reasons;
    private List<Product> recommended;

    public Product() {
        this.company = new Company();
        this.ingredients = Collections.synchronizedList(new ArrayList<Ingredient>());
        this.reasons = Collections.synchronizedList(new ArrayList<Reason>());
        this.recommended = Collections.synchronizedList(new ArrayList<Product>());
    }

    public Product(String barcode, String name, String image, Company company,
                   List<Ingredient> ingredients, List<Reason> reasons, List<Product> recommended) {
        this.barcode = barcode;
        this.name = name;
        this.image = image;
        this.company = company;
        this.ingredients = ingredients;
        this.reasons = reasons;
        this.recommended = recommended;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }

    public List<Product> getRecommended() {
        return recommended;
    }

    public void setRecommended(List<Product> recommended) {
        this.recommended = recommended;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public Ingredient getIngredient(int i) {
        return this.ingredients.get(i);
    }
}
