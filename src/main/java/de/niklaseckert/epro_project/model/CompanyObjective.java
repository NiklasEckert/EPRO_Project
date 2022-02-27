package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Class which represents a Company Objective.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "company_objective")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyObjective {

    /** Represents the id of a Company Objective. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_objective_id_seq")
    @SequenceGenerator(name = "company_objective_id_seq", sequenceName = "company_objective_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the name of a Company Objective. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Represents the description of a Company Objective. */
    @Column(name = "description", nullable = false)
    private String description;

    /** Represents the {@link CompanyObjectiveKeyResult Key Results} of a Company Objective. */
    @JsonIgnore
    @OneToMany(mappedBy = "companyObjective")
    private List<CompanyObjectiveKeyResult> keyResults;

    /** Represents the {@link User User} which created Company Objective. */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    /**
     * Applies changes to a Company Objective.
     *
     * @param update contains the updates for a Company Objective.
     * @return the updated Company Objective.
     */
    public CompanyObjective applyPatch(Map<String, Object> update) {
        if(update.containsKey("name"))
            name = (String) update.get("name");

        if(update.containsKey("description"))
            description = (String) update.get("description");

        if(update.containsKey("user"))
            user = (User) update.get("user");

        return this;
    }

    /**
     * Calculates the achievement of a Company Objective.
     *
     * @return the achievement of a Company Objective.
     */
    public int getAchievement() {
        if (keyResults == null || keyResults.size() <= 0)
            return 0;

        int sum = 0;
        for (CompanyObjectiveKeyResult keyResult : keyResults) {
            sum += keyResult.getAchievement();
        }

        return (sum / keyResults.size());
    }
}
