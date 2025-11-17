package com.farrnav3006.aad.repository;

import java.util.List;

public interface CrudRepository<T> {

    T insert(T entity);

    List<T> findAll();

    T findById(T entity);

    T update(T entity);

    boolean delete(T entity);

    //T createEnrollment(T entity, T entity2);

    //T findByStudent(T entity);

    //T countEnrollments(T entity);
}

