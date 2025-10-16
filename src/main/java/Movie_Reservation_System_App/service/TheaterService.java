package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.model.Role;
import Movie_Reservation_System_App.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new DuplicatedRegisterException("the role " + role.getName() + " already exists");
        }
        roleRepository.save(role);
        return role;
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("role not found for id: " + id));
    }

    public List<Role> getRolesList() {
        return roleRepository.findAll();
    }

    public Role updateRole(Long id, Role newRoleData) {
        Role role = getRole(id);
        role.setName(newRoleData.getName());
        roleRepository.save(role);
        return role;
    }

    public void deleteRole(Long id) {
        Role role = getRole(id);
        roleRepository.delete(role);
    }
}
