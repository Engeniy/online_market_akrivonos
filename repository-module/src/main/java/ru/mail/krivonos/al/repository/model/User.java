package ru.mail.krivonos.al.repository.model;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "t_user")
@SQLDelete(sql =
        "UPDATE t_user " +
                "SET deleted = 1 " +
                "WHERE id = ? AND unchangeable = 0")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "unchangeable", nullable = false)
    private boolean isUnchangeable;
    @Column(name = "deleted", nullable = false)
    private boolean isDeleted = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Profile profile;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUnchangeable() {
        return isUnchangeable;
    }

    public void setUnchangeable(boolean unchangeable) {
        this.isUnchangeable = unchangeable;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                email.equals(user.email) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                password.equals(user.password) &&
                Objects.equals(isUnchangeable, user.isUnchangeable) &&
                Objects.equals(isDeleted, user.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, surname, password, isUnchangeable, isDeleted);
    }
}
