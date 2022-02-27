package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Class which represents a Business Unit.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "business_unit")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessUnit {
    /** Represents the id of a Business Unit. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_unit_id_seq")
    @SequenceGenerator(name = "business_unit_id_seq", sequenceName = "business_unit_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the name of a Business Unit. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Represents the {@link BusinessUnitObjective Business Unit Objectives} of a Business Unit. */
    @JsonIgnore
    @OneToMany(mappedBy = "businessUnit")
    @OrderBy("name")
    private List<BusinessUnitObjective> businessUnitObjectives;

    /**
     *
     * Applies changes to a Business Unit.
     *
     * @param updates contains the updates for a Business Unit.
     * @return the updated Business Unit.
     */
    public BusinessUnit applyPatch(Map<String, Object> updates) {
        if(updates.containsKey("name"))
            name = (String) updates.get("name");

        return this;
    }
}
