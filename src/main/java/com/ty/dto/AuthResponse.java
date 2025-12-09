package com.ty.dto;

import com.ty.model.Role;

public class AuthResponse {

    private boolean success;
    private String message;
    private Role role;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message, Role role) {
        this.success = success;
        this.message = message;
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
