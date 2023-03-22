package com.elearn.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elearn.blog.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}
