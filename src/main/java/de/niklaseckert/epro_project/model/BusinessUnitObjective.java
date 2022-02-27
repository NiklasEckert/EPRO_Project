package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Class which represents a Business Unit Objective.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "business_unit_objective")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessUnitObjective {

    /** Represents the id of a Business Unit Objective. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_unit_objective_id_seq")
    @SequenceGenerator(name = "business_unit_objective_id_seq", sequenceName = "business_unit_objective_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the name of a Business Unit Objective. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Represents the description of Business Unit Objective. */
    @Column(name = "description", nullable = false)
    private String description;

    /** Represents the {@link BusinessUnit Business Unit} of a Business Unit Objective. */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "business_unit_id", nullable = false)
    private BusinessUnit businessUnit;

    /** Represents the {@link BusinessUnitObjectiveKeyResult Key Results} of a Business Unit Objective. */
    @JsonIgnore
    @OneToMany(mappedBy = "businessUnitObjective")
    @OrderBy("name")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;

    /** Represents the {@link User User} which created the Business Unit Objective. */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    /**
     * Applies changes to a Business Unit Objective.
     *
     * @param update contains the updates for a Business Unit Objective.
     * @return the updated Business Unit Objective.
     */
    public BusinessUnitObjective applyPatch(Map<String, Object> update) {
        if(update.containsKey("name"))
            name = (String) update.get("name");

        if(update.containsKey("description"))
            description = (String) update.get("description");

        if(update.containsKey("user"))
            user = (User) update.get("user");

        return this;
    }

    /**
     * Calculates the achievement of a Business Unit Objective.
     *
     * @return the achievement of a Business Unit Objective.
     */
    public int getAchievement() {
        if (businessUnitObjectiveKeyResults==null|| businessUnitObjectiveKeyResults.size() <= 0)
            return 0;

        int sum = 0;
        for (BusinessUnitObjectiveKeyResult keyResult : businessUnitObjectiveKeyResults) {
            sum += keyResult.getAchievement();
        }

        return (sum / businessUnitObjectiveKeyResults.size());
    }
}
