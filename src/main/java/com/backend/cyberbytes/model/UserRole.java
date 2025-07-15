package com.backend.cyberbytes.model;

public enum UserRole {
    //Autoridades
    ADMIN("admin"),
    USER("user");

    private String role;

    //Construtor
    UserRole(String role){
        this.role = role;
    }

    //Método get
    public String getRole(){
        return role;
    }
}
