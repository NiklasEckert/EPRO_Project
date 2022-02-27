package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository which contains elements of {@link CompanyObjectiveKeyResult Company Objective Key Results}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface CompanyObjectiveKeyResultRepository extends JpaRepository<CompanyObjectiveKeyResult, Long> {
}
