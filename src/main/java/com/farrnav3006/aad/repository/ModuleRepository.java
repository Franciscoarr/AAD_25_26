package com.farrnav3006.aad.repository;

import com.farrnav3006.aad.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

}