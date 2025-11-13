package Movie_Reservation_System_App.config;

import Movie_Reservation_System_App.model.Role;
import Movie_Reservation_System_App.model.User;
import Movie_Reservation_System_App.repository.RoleRepository;
import Movie_Reservation_System_App.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (!userRepository.existsByName("admin")) {

            User adminUser = new User();
            adminUser.setName("admin");
            adminUser.setEmail("admin@email.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
            adminUser.setRole(adminRole); // Assumindo um Set de Roles

            userRepository.save(adminUser);
        }
    }
}
