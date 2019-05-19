package ru.mail.krivonos.al.service.model;

public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String surname;
    private String password;
    private boolean unchangeable;
    private boolean deleted;
    private RoleDTO role;
    private ProfileDTO profile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public boolean getUnchangeable() {
        return unchangeable;
    }

    public void setUnchangeable(boolean unchangeable) {
        this.unchangeable = unchangeable;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }
}
