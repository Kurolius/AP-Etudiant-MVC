package ma.enset.etudiantmvc;

import ma.enset.etudiantmvc.entities.Etudiant;
import ma.enset.etudiantmvc.repositories.EtudiantRepository;
import ma.enset.etudiantmvc.security.service.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class EtudiantMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtudiantMvcApplication.class, args);
    }

    @Bean
        //au démarrage crée moi un PasswordEncoder et tu le place dans context
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Bean
    CommandLineRunner commandLineRunner(EtudiantRepository etudiantRepository) {
        return args -> {
            etudiantRepository.save(new Etudiant(null, "Majbar", "Yassine", "admin@admin.ma", new Date(), 'M', true));
            etudiantRepository.save(new Etudiant(null, "Alaa", "Naoufal", "Naoufalalaa@gmail.com", new Date(), 'M', true));
            etudiantRepository.save(new Etudiant(null, "Ouajjani", "Mehdi", "elmehdiouajjani@gmail.com", new Date(), 'M', true));
            etudiantRepository.save(new Etudiant(null, "Ait Elasri", "Aymane", "aitelasriaymane@gmail.com", new Date(), 'M', false));

            etudiantRepository.findAll().forEach(p -> {
                System.out.println(p.getNom());
            });

        };
    }

    //@Bean
    CommandLineRunner saveUsers(SecurityService securityService) {
        return args -> {
            securityService.saveNewUser("admin", "yousk", "yousk");
            securityService.saveNewUser("majbar", "1234", "1234");


            securityService.saveNewRole("USER", "");
            securityService.saveNewRole("ADMIN", "");

            securityService.addRoleToUser("admin", "USER");
            securityService.addRoleToUser("admin", "ADMIN");
            securityService.addRoleToUser("majbar", "USER");
        };
    }
}