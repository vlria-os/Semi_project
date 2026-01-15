package com.example.demo.dto;

public enum ProductStatus {
    ROOM,
    COLD,
    FROZEN;

    public static ProductStatus fromString(String status){
        for(ProductStatus ps : ProductStatus.values()){
            if(ps.name().equalsIgnoreCase(status)){
                return ps;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
