package edu.felipebuso.model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String city;

    public User(String name, String email, String city) {
        this.name = name;
        this.email = email;
        this.city = city;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Usuário: " + name + " | Email: " + email + " | Cidade: " + city;
    }
}
