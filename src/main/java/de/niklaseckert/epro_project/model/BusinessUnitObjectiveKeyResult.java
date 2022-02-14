package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    public BusinessUnitObjectiveKeyResult applyPatch(Map<String, Object> updates) {
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

        return this;
    }
}
