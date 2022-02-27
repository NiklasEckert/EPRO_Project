package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.CompanyObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository which contains elements of {@link CompanyObjective Company Objectives}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface CompanyObjectiveRepository extends JpaRepository<CompanyObjective, Long> {
}
