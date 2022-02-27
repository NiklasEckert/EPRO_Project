package de.niklaseckert.epro_project.repos;

import de.niklaseckert.epro_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository which contains elements of {@link User Users}.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Method to search a {@link User User} by his username.
     *
     * @param name contains the username which should be searched.
     * @return the {@link User User} when he is in the repository.
     */
    Optional<User> findByUsername(String name);
}
