package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.UserDTO;
import Movie_Reservation_System_App.mapper.UserMapper;
import Movie_Reservation_System_App.model.User;
import Movie_Reservation_System_App.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO.Response> createUser(@RequestBody @Validated UserDTO.Request userRequestDto) {

        User user = userService.createUser(userRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(userMapper.toDTO(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<UserDTO.Response> getUser(@PathVariable Long id) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        User user = userService.getUser(id);
        return ResponseEntity.ok().body(userMapper.toDTO(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO.Response>> getUsersList() {
        List<User> users = userService.getUsersList();
        return ResponseEntity.ok().body(userMapper.toDtoList(users));
    }

    @PutMapping("/{id}")
    @PreAuthorize("principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<UserDTO.Response> updateUser(@PathVariable Long id,
            @RequestBody UserDTO.UpdateRequest userUpdateRequestDto) {
        User updatedUser = userService.updateUser(id, userUpdateRequestDto);
        return ResponseEntity.ok().body(userMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("principal.id == #id or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
