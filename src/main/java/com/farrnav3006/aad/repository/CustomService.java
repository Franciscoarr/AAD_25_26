package com.farrnav3006.aad.repository;

import java.util.List;

public interface CustomService<T> {

    T insert(T entity);

    List<T> findAll();

    T findById(int id);

    T update(T entity);

    boolean delete(int id);

    T createEnrollment(T entity, T entity2);

    T findByStudent(T entity);

    T countEnrollments(T entity);
}

