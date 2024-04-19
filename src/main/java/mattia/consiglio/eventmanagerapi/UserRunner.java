package mattia.consiglio.eventmanagerapi;

import mattia.consiglio.eventmanagerapi.entities.User;
import mattia.consiglio.eventmanagerapi.entities.UserRole;
import mattia.consiglio.eventmanagerapi.payloads.NewUserDTO;
import mattia.consiglio.eventmanagerapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Add an event manager if none exists
        if (userService.getEventManagersCount() == 0) {
            User user = userService.createUser(new NewUserDTO("mattia", "info@consitech.it", "f1ropT43$XbIPYGAJkEU", "Mattia", "Consiglio"));
            userService.updateRole(user.getId(), UserRole.EVENT_MANAGER);
        }
    }
}
