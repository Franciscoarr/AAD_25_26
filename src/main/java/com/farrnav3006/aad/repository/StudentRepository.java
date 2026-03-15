package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    //Spring genera automáticamente save(), findAll(), findById(), deleteById(), etc

    Optional<Student> findByNif(String nif);

}

