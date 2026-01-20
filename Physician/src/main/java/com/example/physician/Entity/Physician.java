package com.example.physician.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Physician {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private  String name;
    private String specialization;
    @ManyToOne()
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    }

