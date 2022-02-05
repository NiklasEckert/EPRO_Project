package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyObjectiveKeyResultRepository extends JpaRepository<CompanyObjectiveKeyResult, Long> {
}
