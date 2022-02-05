package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "company_objective_key_result")
public class CompanyObjectiveKeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_objective_key_result_id_seq")
    @SequenceGenerator(name = "company_objective_key_result_id_seq", sequenceName = "company_objective_key_result_id_seq", allocationSize = 1)
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
    @JoinColumn(name = "company_objective_id", nullable = false)
    private CompanyObjective companyObjective;

    @JsonIgnore
    @OneToMany(mappedBy = "companyObjectiveKeyResult")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;

    @JsonIgnore
    @OneToMany(mappedBy = "companyObjectiveKeyResult")
    private List<HistoryCompanyObjectiveKeyResult> history;
}
