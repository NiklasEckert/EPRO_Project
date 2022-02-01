package de.niklaseckert.epro_project.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "company_objective")
public class CompanyObjective {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_objective_id_seq")
    @SequenceGenerator(name = "company_objective_id_seq", sequenceName = "company_objective_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "companyObjective")
    private List<CompanyObjectiveKeyResult> keyResults;
}
