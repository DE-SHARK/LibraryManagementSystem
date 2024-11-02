package me.deshark.lms.server.model.entity;

import lombok.Data;

@Data
public class BookInfo {
    private String isbn;
    private String title;
    private String author;
    private String type;
    private String location;
    private int totalQuantity;
    private int availableQuantity;
} 