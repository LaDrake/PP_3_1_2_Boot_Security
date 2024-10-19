package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRep;
import ru.kata.spring.boot_security.demo.repositories.UserRep;
import ru.kata.spring.boot_security.demo.security.UserDTLS;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRep userRep;
    @Autowired
    private RoleRep roleRep;
    @Autowired
    private UserDao userDao;


    @Transactional()
    public void init() {
        User user = new User();
        user.setUsername("Test_user");
        user.setPassword(("test"));
        user.setSurname("Test_surname");
        user.setAge(12);
        HashSet<Role> set = new HashSet<>();
        set.add(getOrCreateRole("ROLE_USER", 1L));
        user.setRoles(set);
        save(user);
        User user2 = new User();
        user2.setUsername("Test_admin");
        user2.setPassword(("test"));
        user2.setSurname("Test_admin");
        user2.setAge(15);
        HashSet<Role> set2 = new HashSet<>();
        set2.add(getOrCreateRole("ROLE_ADMIN", 2L));
        user2.setRoles(set2);
        save(user2);
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);

    }

    @Override
    @Transactional
    public User findById(long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userDao.delete(id);

    }

    @Override
    @Transactional
    public void update(User user) {
        userDao.update(user);

    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRep.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDTLS(user.get());
    }

    private Role getOrCreateRole(String roleName, long id) {
        Role role = roleRep.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            role.setId(id);
            roleRep.save(role);
        }
        return role;
    }
}
