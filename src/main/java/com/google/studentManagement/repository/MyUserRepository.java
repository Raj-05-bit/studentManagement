package com.google.studentManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.studentManagement.dto.MyUser;



public interface MyUserRepository extends JpaRepository<MyUser,Integer> {


boolean existsByEmail(String email);

MyUser findByEmail(String email);



}
