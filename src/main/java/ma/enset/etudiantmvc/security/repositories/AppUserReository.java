package ma.enset.etudiantmvc.security.repositories;

import ma.enset.etudiantmvc.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserReository extends JpaRepository<AppUser,String> {
    AppUser findByUsername(String username);
}
