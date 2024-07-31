package com.google.studentManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.studentManagement.dto.Student;

public interface StudentRepository extends JpaRepository<Student,Integer>
{


}
