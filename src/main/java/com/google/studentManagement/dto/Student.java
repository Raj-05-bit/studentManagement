package com.google.studentManagement.dto;



import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity
public class Student {
@Id
@GeneratedValue(generator ="x")
@SequenceGenerator(name = "x",initialValue = 101,allocationSize = 1)//start from 101,difference is 1 means add 1
private int id;
private String name;
private LocalDate dob;
private String standerd;
private long phno;
private int subject1;
private int subject2;
private int subject3;
private int subject4;
private int subject5;
private int subject6;
private String picture;

}
