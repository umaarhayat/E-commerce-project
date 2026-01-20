package com.example.physician.DTO;

import com.example.physician.Entity.Doctor;
import lombok.Data;

@Data
public class PhysicianDTO {


    private  Long doctorId;
    private  String name;
    private String specialization;
     private  String  doctorName;


}
