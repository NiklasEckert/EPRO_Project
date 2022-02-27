package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository which contains elements of {@link BusinessUnit Business Units}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit,Long> {

}
