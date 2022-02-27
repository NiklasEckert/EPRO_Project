package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

/**
 * Class which represents a Role.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "okr_roles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    /** Represents the id of a Role. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "okr_roles_id_seq")
    @SequenceGenerator(name = "okr_roles_id_seq", sequenceName = "okr_roles_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the name of a Role. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Represents the {@link User User} which has this Role. */
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    /**
     * Getter for authority.
     *
     * @return name of the Role.
     */
    @Override
    public String getAuthority() {
        return name;
    }
}
