package com.example.selectametalsclad;

public class Product {
    private String id, material,article,name,param,location,total,unit,indiTotal;
    public Product(){}

    public String getUnit() {
        return unit;
    }

    public String getIndiTotal() {
        return indiTotal;
    }

    public void setIndiTotal(String indiTotal) {
        this.indiTotal = indiTotal;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Product(String id, String material, String article, String name, String param, String location, String total, String unit, String indiTotal) {
        this.id = id;
        this.material = material;
        this.article = article;
        this.name = name;
        this.param = param;
        this.location = location;
        this.total = total;
        this.unit = unit;
        this.indiTotal = indiTotal;


    }
}
