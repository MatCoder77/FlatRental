package com.flatrental.domain.user;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.managedobject.ManagedObject;
import com.flatrental.domain.statistics.UserStatistics;
import com.flatrental.domain.userrole.UserRole;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "userCache")
public class User extends ManagedObject {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    @Size(max = 40)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Column(unique=true)
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    private String avatar;

    @Size(max = 10000)
    private String about;

    private UserStatistics userStatistics;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "userRolesCollectionCache")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Users_X_UserRoles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> roles = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_X_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "announcement_id"))
    private Set<Announcement> favourites = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Announcement> getFavourites() {
        return favourites;
    }

    public void addAnnouncementToFavourites(Announcement announcement) {
        this.favourites.add(announcement);
    }

    public void removeAnnouncementFromFavourites(Announcement announcement) {
        this.favourites.remove(announcement);
    }

    public UserStatistics getUserStatistics() {
        return userStatistics;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User otherUser = (User) obj;
        return Objects.equals(otherUser.id, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
