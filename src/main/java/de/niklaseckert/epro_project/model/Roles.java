package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "okr_roles")
public class Roles implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "okr_roles_id_seq")
    @SequenceGenerator(name = "okr_roles_id_seq", sequenceName = "okr_roles_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(
            mappedBy = "roles"
    )
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
