package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "h_company_objective_key_result")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryCompanyObjectiveKeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "h_company_objective_key_result_id_seq")
    @SequenceGenerator(name = "h_company_objective_key_result_id_seq", sequenceName = "h_company_objective_key_result_id_seq", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "co_kr_id", nullable = false, updatable = false)
    private CompanyObjectiveKeyResult companyObjectiveKeyResult;

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
    @JoinColumn(name = "company_objective_id", nullable = false, updatable = false)
    private CompanyObjective companyObjective;

    @JsonIgnore
    @OneToMany(mappedBy = "companyObjectiveKeyResult")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;
}
