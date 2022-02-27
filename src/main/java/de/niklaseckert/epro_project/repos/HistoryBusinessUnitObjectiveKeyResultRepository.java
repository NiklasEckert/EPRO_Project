package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.HistoryBusinessUnitObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository which contains elements of {@link HistoryBusinessUnitObjectiveKeyResult History Business Unit Objective Key Results}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface HistoryBusinessUnitObjectiveKeyResultRepository extends JpaRepository<HistoryBusinessUnitObjectiveKeyResult, Long> {
}
