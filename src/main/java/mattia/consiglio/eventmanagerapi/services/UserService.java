package mattia.consiglio.eventmanagerapi.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import mattia.consiglio.eventmanagerapi.entities.User;
import mattia.consiglio.eventmanagerapi.entities.UserRole;
import mattia.consiglio.eventmanagerapi.exceptions.BadRequestException;
import mattia.consiglio.eventmanagerapi.exceptions.RecordNotFoundException;
import mattia.consiglio.eventmanagerapi.payloads.ChangePasswordDTO;
import mattia.consiglio.eventmanagerapi.payloads.NewUserDTO;
import mattia.consiglio.eventmanagerapi.payloads.UserDTO;
import mattia.consiglio.eventmanagerapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bCrypt;
    @Autowired
    private Cloudinary cloudinary;

    public User createUser(NewUserDTO userDTO) {
        String avatarUrl = "https://ui-avatars.com/api/?name=" + userDTO.firstName().charAt(0) + "+" + userDTO.lastName().charAt(0);
        User user = new User();
        //using a sort of building block pattern
        user.setUsername(userDTO.username());
        user.setPassword(bCrypt.encode(userDTO.password()));
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(User.class.getSimpleName(), id));
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new RecordNotFoundException(User.class.getSimpleName(), usernameOrEmail));
    }

    public User updateUser(UUID id, UserDTO userDTO) {
        User user = this.getUser(id);
        if (userRepository.existsByUsernameOrEmail(userDTO.username(), userDTO.email())) {
            throw new BadRequestException("Username or email already in use");
        } else if (userRepository.existsByUsername(userDTO.username()) && !userRepository.findById(id).get().getUsername().equals(userDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (userRepository.existsByEmail(userDTO.email()) && !userRepository.findById(id).get().getEmail().equals(userDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String avatarUrl = "https://ui-avatars.com/api/?name=" + userDTO.firstName().charAt(0) + "+" + userDTO.lastName().charAt(0);

        if (!user.getAvatarUrl().startsWith("https://ui-avatars.com/api/")) {
            avatarUrl = user.getAvatarUrl();
        }
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAvatarUrl(avatarUrl);
        user.setPassword(bCrypt.encode(userDTO.password()));
        user.setRole(UserRole.valueOf(userDTO.role()));
        return userRepository.save(user);

    }

    public User updateUser(UUID id, NewUserDTO userDTO) {
        User user = this.getUser(id);
        if (userRepository.existsByUsernameOrEmail(userDTO.username(), userDTO.email())) {
            throw new BadRequestException("Username or email already in use");
        } else if (userRepository.existsByUsername(userDTO.username()) && !userRepository.findById(id).get().getUsername().equals(userDTO.username())) {
            throw new BadRequestException("Username already in use");
        } else if (userRepository.existsByEmail(userDTO.email()) && !userRepository.findById(id).get().getEmail().equals(userDTO.email())) {
            throw new BadRequestException("Email already in use");
        }
        String avatarUrl = "https://ui-avatars.com/api/?name=" + userDTO.firstName().charAt(0) + "+" + userDTO.lastName().charAt(0);

        if (!user.getAvatarUrl().startsWith("https://ui-avatars.com/api/")) {
            avatarUrl = user.getAvatarUrl();
        }
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAvatarUrl(avatarUrl);
        user.setPassword(bCrypt.encode(userDTO.password()));
        return userRepository.save(user);
    }


    public void deleteUser(UUID id) {
        User user = this.getUser(id);
        userRepository.delete(user);
    }

    public User updatePassword(UUID id, ChangePasswordDTO changePasswordDTO) {
        User user = this.getUser(id);
        if (!bCrypt.matches(changePasswordDTO.oldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }
        String password = bCrypt.encode(changePasswordDTO.newPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }


    public User updateRole(UUID id, UserRole role) {
        User user = this.getUser(id);
        user.setRole(role);
        return userRepository.save(user);
    }


    public User updateAvatar(UUID id, MultipartFile avatar) throws IOException {
        User user = this.getUser(id);
        String avatarUrl = (String) cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

}
