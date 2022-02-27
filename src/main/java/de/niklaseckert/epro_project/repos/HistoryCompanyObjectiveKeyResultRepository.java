package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.HistoryCompanyObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository which contains elements of {@link HistoryCompanyObjectiveKeyResult History Company Objective Key Results}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface HistoryCompanyObjectiveKeyResultRepository extends JpaRepository<HistoryCompanyObjectiveKeyResult, Long> {
}
