package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.BusinessUnitObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusienessUnitObjectiveKeyResultRepository extends JpaRepository<BusinessUnitObjectiveKeyResult,Long> {
}
