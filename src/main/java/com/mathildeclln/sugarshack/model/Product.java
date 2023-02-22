package com.mathildeclln.sugarshack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private MapleType type;

    public Product() {
    }

    @Override
    public boolean equals(Object object){
        if(this == object){
            return true;
        }

        if(object == null){
            return false;
        }

        if(getClass() != object.getClass()){
            return false;
        }
        Product product = (Product) object;

        return (this.id.equals(product.getId()) &&
                this.name.equals(product.getName()) &&
                this.description.equals(product.getDescription()) &&
                this.image.equals(product.getImage()) &&
                this.price == product.getPrice() &&
                this.type == product.getType());
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MapleType getType() {
        return type;
    }

    public void setType(MapleType type) {
        this.type = type;
    }
}
