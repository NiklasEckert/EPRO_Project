package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Class which represents a User.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "okr_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /** Represents the id of a User. */
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "okr_user_id_seq")
    @SequenceGenerator(name = "okr_user_id_seq", sequenceName = "okr_user_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the username of a User. */
    @Column(name = "name", nullable = false)
    private String username;

    /** Represents the password of a User. */
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    /** Represents the {@link CompanyObjective Company Objectives} of a User. */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CompanyObjective> companyObjectives;

    /** Represents the {@link CompanyObjectiveKeyResult Company Objective Key Results} of a User. */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CompanyObjectiveKeyResult> companyObjectiveKeyResults;

    /** Represents the {@link BusinessUnitObjective Business Unit Objectives} of a User. */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BusinessUnitObjective> businessUnitObjectives;

    /** Represents the {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Results} of a User. */
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;

    /** Represents the {@link Role Roles} of a User. */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private List<Role> roles;

    /**
     * Getter for authorities.
     *
     * @return all {@link Role Roles} the User has.
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * Checks if account is expired.
     *
     * @return true when the Account is not expired, otherwise false.
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if account is locked.
     *
     * @return true when the Account is not locked, otherwise false.
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Check if credentials are expired.
     *
     * @return  true when the credentials are not expired, otherwise false.
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Check if account is enabled.
     *
     * @return true when account is enabled, otherwise false.
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
