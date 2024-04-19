package mattia.consiglio.eventmanagerapi.services;

import mattia.consiglio.eventmanagerapi.entities.User;
import mattia.consiglio.eventmanagerapi.exceptions.RecordNotFoundException;
import mattia.consiglio.eventmanagerapi.payloads.NewUserDTO;
import mattia.consiglio.eventmanagerapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bCrypt;

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


}
