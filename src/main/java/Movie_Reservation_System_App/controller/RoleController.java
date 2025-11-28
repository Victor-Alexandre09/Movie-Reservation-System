package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.RoleDTO;
import Movie_Reservation_System_App.mapper.RoleMapper;
import Movie_Reservation_System_App.model.Role;
import Movie_Reservation_System_App.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public RoleController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @PostMapping
    public ResponseEntity<RoleDTO.Response> createRole(@RequestBody @Validated RoleDTO.Request data) {
        Role role = roleMapper.toEntity(data);
        Role createdRole = roleService.createRole(role);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRole.getId())
                .toUri();

        return ResponseEntity.created(location).body(roleMapper.toDTO(createdRole));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO.Response> getRole(@PathVariable Long id) {
        Role role = roleService.getRole(id);
        return ResponseEntity.ok(roleMapper.toDTO(role));
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO.Response>> getRoleList() {
        return ResponseEntity.ok(roleMapper.toDtoList(roleService.getRolesList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO.Response> updateRole(@RequestBody @Validated RoleDTO.Request data,
            @PathVariable Long id) {
        Role newRoleData = roleMapper.toEntity(data);
        var role = roleService.updateRole(id, newRoleData);
        return ResponseEntity.ok(roleMapper.toDTO(role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}