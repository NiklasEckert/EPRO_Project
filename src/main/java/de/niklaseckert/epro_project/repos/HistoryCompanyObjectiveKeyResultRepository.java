package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.HistoryCompanyObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryCompanyObjectiveKeyResultRepository extends JpaRepository<HistoryCompanyObjectiveKeyResult, Long> {
}
