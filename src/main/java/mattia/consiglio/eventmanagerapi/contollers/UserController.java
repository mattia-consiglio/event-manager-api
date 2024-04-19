package mattia.consiglio.eventmanagerapi.contollers;

import mattia.consiglio.eventmanagerapi.entities.User;
import mattia.consiglio.eventmanagerapi.entities.UserRole;
import mattia.consiglio.eventmanagerapi.payloads.ChangePasswordDTO;
import mattia.consiglio.eventmanagerapi.payloads.UserDTO;
import mattia.consiglio.eventmanagerapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "username") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return userService.getUsers(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User getUser(@PathVariable("id") UUID id) {
        return userService.getUser(id);
    }

    @GetMapping("/me")
    public User getMe(@AuthenticationPrincipal User user) {
        return user;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUser(@PathVariable("id") UUID id, @RequestBody UserDTO user) {
        return userService.updateUser(id, user);
    }

    @PutMapping("/me")
    public User updateMe(@AuthenticationPrincipal User user, @RequestBody UserDTO userDTO) {
        return userService.updateUser(user.getId(), userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public void deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
    }

    @DeleteMapping("/me")
    public void deleteMe(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getId());
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUserPassword(@PathVariable("id") UUID id, @RequestBody ChangePasswordDTO userPassword) {
        return userService.updatePassword(id, userPassword);
    }

    @PutMapping("/me/password")
    public User updateMePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordDTO userPassword) {
        return userService.updatePassword(user.getId(), userPassword);
    }

    @PutMapping("/{id}/avatar")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUserAvatar(@PathVariable("id") UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        return userService.updateAvatar(id, file);
    }

    @PutMapping("/me/avatar")
    public User updateMeAvatar(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) throws IOException {
        return userService.updateAvatar(user.getId(), file);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateUserRole(@PathVariable("id") UUID id, @RequestParam("role") String roleString) {
        UserRole role = UserRole.valueOf(roleString);
        return userService.updateRole(id, role);
    }

    @PutMapping("/me/role")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User updateMeRole(@AuthenticationPrincipal User user, @RequestParam("role") String roleString) {
        UserRole role = UserRole.valueOf(roleString);
        return userService.updateRole(user.getId(), role);
    }
}
