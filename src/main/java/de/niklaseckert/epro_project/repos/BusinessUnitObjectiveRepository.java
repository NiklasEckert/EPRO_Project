package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.BusinessUnitObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository which contains elements of {@link BusinessUnitObjective Business Unit Objectives}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface BusinessUnitObjectiveRepository extends JpaRepository<BusinessUnitObjective,Long> {
}
