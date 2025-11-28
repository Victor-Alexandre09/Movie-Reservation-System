package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.UserDTO;
import Movie_Reservation_System_App.exception.ValidationException;
import Movie_Reservation_System_App.mapper.UserMapper;
import Movie_Reservation_System_App.model.Role;
import Movie_Reservation_System_App.model.User;
import Movie_Reservation_System_App.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper,
            RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(UserDTO.Request userRequestDto) {
        validateNameAndEmail(userRequestDto.name(), userRequestDto.email());

        User user = userMapper.toEntity(userRequestDto);
        Role defaultRole = roleService.getRoleByName("ROLE_USER");
        user.setRole(defaultRole);
        String encodedPassword = passwordEncoder.encode(userRequestDto.password());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("user not found for id " + id));
    }

    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, UserDTO.UpdateRequest newData) {
        User userToUpdate = getUser(id);
        Map<String, String> validationErrors = new HashMap<>();

        if (StringUtils.hasText(newData.name()) && !newData.name().equals(userToUpdate.getName())) {
            if (userRepository.findByName(newData.name()).isPresent()) {
                validationErrors.put("name", "the name " + newData.name() + " already exists");
            } else {
                userToUpdate.setName(newData.name());
            }
        }

        if (StringUtils.hasText(newData.email()) && !newData.email().equals(userToUpdate.getEmail())) {
            if (userRepository.findByEmail(newData.email()).isPresent()) {
                validationErrors.put("email", "the email " + newData.email() + " already exists");
            } else {
                userToUpdate.setEmail(newData.email());
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        if (StringUtils.hasText(newData.password())) {
            userToUpdate.setPassword(passwordEncoder.encode(newData.password()));
        }

        return userRepository.save(userToUpdate);
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("user not found for id"));
    }

    private void validateNameAndEmail(String name, String email) {
        Map<String, String> validationErrors = new HashMap<>();

        if (StringUtils.hasText(name)) {
            if (userRepository.findByName(name).isPresent()) {
                validationErrors.put("name", "the name " + name + " already exists");
            }
        }

        if (StringUtils.hasText(email)) {
            if (userRepository.findByEmail(email).isPresent()) {
                validationErrors.put("email", "the email " + email + " already exists");
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }
    }
}
