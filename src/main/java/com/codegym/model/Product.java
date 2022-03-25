package com.codegym.model;

import javax.persistence.*;

@Entity//Tạo bảng
@Table(name = "products")
public class Product {
    @Id//Khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Tự tăng
    private Long id;

    @Column(columnDefinition = "Varchar(50)", nullable = false)
    private String name; //khi tạo bảng string => varchar(255)

    @Column(nullable = false)
    private double price;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String description;

    private String image;

    @ManyToOne //Tạo khóa ngoại và thêm 1 cột category_id
    private Category category;

    public Product() {
    }

    public Product(Long id, String name, double price, String description, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}