package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "okr_user")
public class User implements UserDetails {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "okr_user_id_seq")
    @SequenceGenerator(name = "okr_user_id_seq", sequenceName = "okr_user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CompanyObjective> companyObjectives;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CompanyObjectiveKeyResult> companyObjectiveKeyResults;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BusinessUnitObjective> businessUnitObjectives;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private List<Roles> roles;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
