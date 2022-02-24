package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "h_business_unit_objective_key_result")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryBusinessUnitObjectiveKeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "h_business_unit_objective_key_result_id_seq")
    @SequenceGenerator(name = "h_business_unit_objective_key_result_id_seq", sequenceName = "h_business_unit_objective_key_result_id_seq", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bo_kr_id", nullable = false, updatable = false)
    private BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "description", nullable = false, updatable = false)
    private String description;

    @Column(name = "current", nullable = false, updatable = false)
    private double current;

    @Column(name = "goal", nullable = false, updatable = false)
    private double goal;

    @Column(name = "confidence_level", nullable = false, updatable = false)
    private int confidenceLevel;

    @Column(name = "comment", nullable = false, updatable = false)
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "business_unit_objective_id", nullable = false, updatable = false)
    private BusinessUnitObjective businessUnitObjective;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "co_key_result_id", updatable = false)
    private CompanyObjectiveKeyResult companyObjectiveKeyResult;
}
