package com.library.dto;
public record RegisterRequest(String email, String password, com.library.entity.Role role) {}