package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "business_unit_objective")
public class BusinessUnitObjective {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_unit_objective_id_seq")
    @SequenceGenerator(name = "business_unit_objective_id_seq", sequenceName = "business_unit_objective_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "business_unit_id", nullable = false)
    private BusinessUnit businessUnit;

    @JsonIgnore
    @OneToMany(mappedBy = "businessUnitObjective")
    private List<BusinessUnitObjectiveKeyResult> businessUnitObjectiveKeyResults;
}
