package com.farrnav3006.aad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.farrnav3006.aad.model.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    public List<User> findByEmail(String email);
    public List<User> findByNameAndEmail(String name,String email);
}
