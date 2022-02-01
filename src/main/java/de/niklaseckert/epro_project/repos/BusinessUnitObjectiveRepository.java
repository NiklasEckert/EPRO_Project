package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.BusinessUnitObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective,Long> {
}
