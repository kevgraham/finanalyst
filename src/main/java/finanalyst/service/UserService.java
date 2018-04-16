package finanalyst.service;

import finanalyst.model.Role;
import finanalyst.model.User;
import finanalyst.repository.RoleRepository;
import finanalyst.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void createUser(@Valid User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public boolean updateUser(@Valid User user) {
        // get user info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = userRepository.findByUsername(auth.getName());

        // update info
        if (existingUser != null) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(existingUser);
            return true;
        }

        return false;
    }

    public boolean deleteUser() {
        // get user info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User existingUser = userRepository.findByUsername(auth.getName());

        // update info
        if (existingUser != null) {
            userRepository.delete(existingUser);
            return true;
        }

        return false;
    }
}
