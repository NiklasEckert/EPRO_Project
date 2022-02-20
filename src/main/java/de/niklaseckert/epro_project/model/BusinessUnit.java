package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "business_unit")
public class BusinessUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_unit_id_seq")
    @SequenceGenerator(name = "business_unit_id_seq", sequenceName = "business_unit_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "businessUnit")
    @OrderBy("name")
    private List<BusinessUnitObjective> businessUnitObjectives;

    public BusinessUnit applyPatch(Map<String, Object> updates) {
        if(updates.containsKey("name"))
            name = (String) updates.get("name");

        return this;
    }
}
