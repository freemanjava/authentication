package com.dmitry.authentication.repositories;

import com.dmitry.authentication.models.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
    User findByPassword(String token);
}
