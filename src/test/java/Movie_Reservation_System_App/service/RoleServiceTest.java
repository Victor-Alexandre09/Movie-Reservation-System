package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.model.Role;
import Movie_Reservation_System_App.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleService(roleRepository);
    }

    @Test
    void testCreateRole_WhenRoleDoesNotExist_ShouldSaveAndReturnRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        when(roleRepository.save(role)).thenReturn(role);

        Role createdRole = roleService.createRole(role);

        assertNotNull(createdRole);
        assertEquals(1L, createdRole.getId());
        assertEquals("ROLE_USER", createdRole.getName());
        verify(roleRepository).findByName("ROLE_USER");
        verify(roleRepository).save(role);
    }

    @Test
    void testCreateRole_WhenRoleAlreadyExists_ShouldThrowDuplicatedRegisterException() {
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("ROLE_ADMIN");

        Role roleToCreate = new Role();
        roleToCreate.setName("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(existingRole));

        DuplicatedRegisterException exception = assertThrows(
                DuplicatedRegisterException.class,
                () -> roleService.createRole(roleToCreate)
        );

        assertEquals("the role ROLE_ADMIN already exists", exception.getMessage());
        verify(roleRepository).findByName("ROLE_ADMIN");
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testGetRole_WhenRoleExists_ShouldReturnRole() {
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("ROLE_USER");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));

        Role foundRole = roleService.getRole(1L);

        assertNotNull(foundRole);
        assertEquals(1L, foundRole.getId());
        verify(roleRepository).findById(1L);
    }

    @Test
    void testGetRole_WhenRoleDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long roleId = 99L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.getRole(roleId)
        );

        assertEquals("role not found for id: " + roleId, exception.getMessage());
        verify(roleRepository).findById(roleId);
    }

    @Test
    void testGetRoleByname_WhenRoleExists_ShouldReturnRole() {
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(existingRole));

        Role foundRole = roleService.getRoleByName("ROLE_USER");

        assertNotNull(foundRole);
        assertEquals(1L, foundRole.getId());
        verify(roleRepository).findByName("ROLE_USER");
    }

    @Test
    void testGetRoleByname_WhenRoleDoesNotExist_ShouldThrowEntityNotFoundException() {
        String roleName = "NON_EXISTING_ROLE";
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.getRoleByName(roleName)
        );

        assertEquals("role not found for name: " + roleName, exception.getMessage());
        verify(roleRepository).findByName(roleName);
    }

    @Test
    void testGetRolesList_WhenRolesExist_ShouldReturnListOfRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");

        List<Role> roleList = List.of(role1, role2);

        when(roleRepository.findAll()).thenReturn(roleList);

        List<Role> result = roleService.getRolesList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ROLE_USER", result.get(0).getName());
        assertEquals("ROLE_ADMIN", result.get(1).getName());
        verify(roleRepository).findAll();
    }

    @Test
    void testGetRolesList_WhenNoRolesExist_ShouldReturnEmptyList() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Role> result = roleService.getRolesList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepository).findAll();
    }

    @Test
    void testUpdateRole_WhenRoleExists_ShouldUpdateAndReturnRole() {
        Long roleId = 1L;

        Role existingRole = new Role();
        existingRole.setId(roleId);
        existingRole.setName("Old Name");

        Role newRoleData = new Role();
        newRoleData.setName("New Name");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(existingRole)).thenReturn(existingRole);

        Role updatedRole = roleService.updateRole(roleId, newRoleData);

        assertNotNull(updatedRole);
        assertEquals("New Name", updatedRole.getName());
        verify(roleRepository).findById(roleId);
        verify(roleRepository).save(existingRole);
    }

    @Test
    void testUpdateRole_WhenRoleDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long roleId = 99L;
        Role newRoleData = new Role();
        newRoleData.setName("New Name");

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.updateRole(roleId, newRoleData)
        );

        assertEquals("role not found for id: 99", exception.getMessage());
        verify(roleRepository).findById(roleId);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testDeleteRole_WhenRoleExists_ShouldDeleteRole() {
        Long roleId = 1L;
        Role existingRole = new Role();
        existingRole.setId(roleId);
        existingRole.setName("To Be Deleted");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(existingRole));
        doNothing().when(roleRepository).delete(existingRole);

        roleService.deleteRole(roleId);

        verify(roleRepository).findById(roleId);
        verify(roleRepository).delete(existingRole);
    }

    @Test
    void testDeleteRole_WhenRoleDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long roleId = 99L;

        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> roleService.deleteRole(roleId)
        );

        assertEquals("role not found for id: " + roleId, exception.getMessage());
        verify(roleRepository).findById(roleId);
        verify(roleRepository, never()).delete(any(Role.class));
    }
}