package com.cegeka.cnads.simpleproject;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class HelloUser {

    @Id
    private String id;
    private String username;

    public HelloUser() {
    }

    public HelloUser(String username) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }
}
