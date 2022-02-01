package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.HistoryBusinessUnitObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryBusinessUnitObjectiveKeyResultRepository extends JpaRepository<HistoryBusinessUnitObjectiveKeyResult, Long> {
}
