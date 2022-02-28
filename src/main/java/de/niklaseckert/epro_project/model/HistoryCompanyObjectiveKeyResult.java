package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Class which represents a History Company Objective Key Result.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Data
@Entity
@Table(name = "h_company_objective_key_result")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCompanyObjectiveKeyResult {

    /** Represents the id of a History Company Objective Key Result. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "h_company_objective_key_result_id_seq")
    @SequenceGenerator(name = "h_company_objective_key_result_id_seq", sequenceName = "h_company_objective_key_result_id_seq", allocationSize = 1)
    private Long id;

    /** Represents the {@link CompanyObjectiveKeyResult Company Objective Key Result} of a History Company Objective Key Result. */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "co_kr_id", nullable = false, updatable = false)
    private CompanyObjectiveKeyResult companyObjectiveKeyResult;

    /** Represents the name of a History Company Objective Key Result. */
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    /** Represents the description of a History Company Objective Key Result. */
    @Column(name = "description", nullable = false, updatable = false)
    private String description;

    /** Represents the currently reached achievement of a History Company Objective Key Result. */
    @Column(name = "current", nullable = false, updatable = false)
    private double current;

    /** Represents the goal of a History Company Objective Key Result. */
    @Column(name = "goal", nullable = false, updatable = false)
    private double goal;

    /** Represents the confidence level of a History Company Objective Key Result. */
    @Column(name = "confidence_level", nullable = false, updatable = false)
    private int confidenceLevel;

    /** Represents the comment of a History Company Objective Key Result. */
    @Column(name = "comment", nullable = false, updatable = false)
    private String comment;

    /** Represents the {@link CompanyObjective Company Objective} of a History Company Objective Key Result. */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_objective_id", nullable = false, updatable = false)
    private CompanyObjective companyObjective;

    /** Represents the {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Results} of a History Company Objective Key Result. */
    @JsonIgnore
    @OneToMany(mappedBy = "companyObjectiveKeyResult")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;

    /** Represents the {@link User User} which created the Company Objective Key Result. */
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;
}
