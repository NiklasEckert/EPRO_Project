package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.List;
import java.util.Map;

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

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    public BusinessUnitObjective applyPatch(Map<String, Object> update) {
        if(update.containsKey("name"))
            name = (String) update.get("name");

        if(update.containsKey("description"))
            description = (String) update.get("description");

        if(update.containsKey("user"))
            user = (User) update.get("user");

        return this;
    }

    public int getAchievement() {
        if (businessUnitObjectiveKeyResults.size() <= 0)
            return 0;

        int sum = 0;
        for (BusinessUnitObjectiveKeyResult keyResult : businessUnitObjectiveKeyResults) {
            sum += keyResult.getAchievement();
        }

        return (sum / businessUnitObjectiveKeyResults.size());
    }
}
