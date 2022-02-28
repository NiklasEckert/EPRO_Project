package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Class which represents a Company Objective Key Result
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "company_objective_key_result")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyObjectiveKeyResult {

    /** Represents the id of a Company Objective Key Result. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_objective_key_result_id_seq")
    @SequenceGenerator(name = "company_objective_key_result_id_seq", sequenceName = "company_objective_key_result_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the name of a Company Objective Key Result. */
    @Column(name = "name", nullable = false)
    private String name;

    /** Represents the description of a Company Objective Key Result. */
    @Column(name = "description", nullable = false)
    private String description;

    /** Represents the currently reached achievement of a Company Objective Key Result. */
    @Column(name = "current", nullable = false)
    private double current;

    /** Represents the goal of a Company Objective Key Result. */
    @Column(name = "goal", nullable = false)
    private double goal;

    /** Represents the confidence level of a Company Objective Key Result. */
    @Column(name = "confidence_level", nullable = false)
    private int confidenceLevel;

    /** Represents the comment of a Company Objective Key Result. */
    @Column(name = "comment", nullable = false)
    private String comment;

    /** Represents the {@link CompanyObjective Company Objective} of a Company Objective Key Result. */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_objective_id", nullable = false)
    private CompanyObjective companyObjective;

    /** Represents the {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Results} of a Company Objective Key Result. */
    @JsonIgnore
    @OneToMany(mappedBy = "companyObjectiveKeyResult")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;

    /** Represents the {@link HistoryCompanyObjectiveKeyResult history} of a Company Objective Key Result. */
    @JsonIgnore
    @OneToMany(mappedBy = "companyObjectiveKeyResult")
    @OrderBy("timestamp DESC")
    private List<HistoryCompanyObjectiveKeyResult> history;

    /** Represents the {@link User User} which created the Company Objective Key Result. */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    /**
     * Applies changes to a Company Objective Key Result.
     *
     * @param updates contains the updates for a Company Objective Key Result.
     * @return the updated Company Objective Key Result.
     */
    public CompanyObjectiveKeyResult applyPatch(Map<String, Object> updates) {
        if(updates.containsKey("current"))
            current = (double) updates.get("current");

        if(updates.containsKey("confidence_level")) {
            int tmp = (int) updates.get("confidence_level");
            if(tmp < 0 || tmp > 100)
                throw new IllegalArgumentException("Confidence Level to high or to low!");
            confidenceLevel = tmp;
        }

        if(updates.containsKey("comment"))
            comment = (String) updates.get("comment");

        if(updates.containsKey("user"))
            user = (User) updates.get("user");

        return this;
    }

    /**
     * Calculates the achievement of a Company Objective Key Result.
     *
     * @return the achievement of a Company Objective Key Result.
     */
    public int getAchievement() {
        // TODO: check epsilon
        if (this.goal == 0) {
            return 0;
        }
        return (int) ((this.current / this.goal) * 100);
    }
}
