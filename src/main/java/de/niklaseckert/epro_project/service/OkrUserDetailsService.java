package de.niklaseckert.epro_project.service;

import de.niklaseckert.epro_project.model.User;
import de.niklaseckert.epro_project.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom User Details Service.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@AllArgsConstructor
@Service
public class OkrUserDetailsService implements UserDetailsService {
    /** Repository which contains {@link User users}. */
    private final UserRepository repository;

    /**
     * Returns a {@link User user} by its username.
     *
     * @param username contains name of the {@link User user}.
     * @return a {@link User user} object.
     * @throws UsernameNotFoundException if the username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return user;
    }
}
