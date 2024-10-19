package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findById(long id);

    void delete(long id);

    void update(User user);

    List<User> findAll();
}
