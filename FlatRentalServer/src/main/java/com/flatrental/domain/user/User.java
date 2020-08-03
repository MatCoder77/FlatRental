package com.flatrental.domain.user;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.managedobject.ManagedObject;
import com.flatrental.domain.statistics.user.UserStatistics;
import com.flatrental.domain.userrole.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@Table(name = "User")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "userCache")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    public void addAnnouncementToFavourites(Announcement announcement) {
        this.favourites.add(announcement);
    }

    public void removeAnnouncementFromFavourites(Announcement announcement) {
        this.favourites.remove(announcement);
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
