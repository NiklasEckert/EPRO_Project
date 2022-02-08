package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "business_unit_objective_key_result")
public class BusinessUnitObjectiveKeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_unit_objective_key_result_id_seq")
    @SequenceGenerator(name = "business_unit_objective_key_result_id_seq", sequenceName = "business_unit_objective_key_result_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "current", nullable = false)
    private double current;

    @Column(name = "goal", nullable = false)
    private double goal;

    @Column(name = "confidence_level", nullable = false)
    private int confidenceLevel;

    @Column(name = "comment", nullable = false)
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "business_unit_objective_id", nullable = false)
    private BusinessUnitObjective businessUnitObjective;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "co_key_result_id")
    private CompanyObjectiveKeyResult companyObjectiveKeyResult;

    @JsonIgnore
    @OneToMany(mappedBy = "businessUnitObjectiveKeyResult")
    private List<HistoryBusinessUnitObjectiveKeyResult> history;

    public BusinessUnitObjectiveKeyResult applyPatch(BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult) {
        if(businessUnitObjectiveKeyResult.getName() != null)
            name = businessUnitObjectiveKeyResult.getName();

        if(businessUnitObjectiveKeyResult.getDescription() != null)
            description = businessUnitObjectiveKeyResult.getDescription();

        /**
         * ToDo: ZeroCheck
         */
        if(businessUnitObjectiveKeyResult.getCurrent() >= 0.0)
            current = businessUnitObjectiveKeyResult.getCurrent();

        if(businessUnitObjectiveKeyResult.getGoal() >= 0.0)
            goal = businessUnitObjectiveKeyResult.getGoal();

        if(businessUnitObjectiveKeyResult.getConfidenceLevel() >= 0.0 && businessUnitObjectiveKeyResult.getConfidenceLevel() <= 100.0)
            confidenceLevel = businessUnitObjectiveKeyResult.getConfidenceLevel();

        if(businessUnitObjectiveKeyResult.getComment() != null)
            comment = businessUnitObjectiveKeyResult.getComment();

        return this;
    }
}
