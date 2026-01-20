package com.example.physician.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private  String name;


    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Physician> physicians =new ArrayList<>();

}
