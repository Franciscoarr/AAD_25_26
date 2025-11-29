package com.farrnav3006.aad.repository;

import java.util.List;

public interface CustomService<T> {

    T insert(T entity);

    List<T> findAll();

    T findById(Integer id);

    T update(T entity);

    boolean delete(Integer id);
}

