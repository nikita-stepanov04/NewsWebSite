package com.example.newswebsite.initialization;

import com.example.newswebsite.model.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AdminsContainer {
    @JsonProperty("admins")
    private List<User> adminsList;

    public List<User> getAdminsList() {
        return adminsList;
    }
}
