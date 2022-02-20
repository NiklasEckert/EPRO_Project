package de.niklaseckert.epro_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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

    @JsonIgnore
    @OneToMany(mappedBy = "companyObjective")
    private List<CompanyObjectiveKeyResult> keyResults;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    public CompanyObjective applyPatch(Map<String, Object> update) {
        if(update.containsKey("name"))
            name = (String) update.get("name");

        if(update.containsKey("description"))
            description = (String) update.get("description");
        if(update.containsKey("user"))
            user = (User) update.get("user");
        return this;
    }

    public int getAchievement() {
        if (keyResults.size() <= 0)
            return 0;

        int sum = 0;
        for (CompanyObjectiveKeyResult keyResult : keyResults) {
            sum += keyResult.getAchievement();
        }

        return (sum / keyResults.size());
    }
}
