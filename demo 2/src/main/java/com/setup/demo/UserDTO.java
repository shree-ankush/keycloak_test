package com.setup.demo;

public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String organization;
    private String password; // ✅ Added password field

    // Constructor
    public UserDTO(String username, String email, String firstName, String lastName, String organization, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
