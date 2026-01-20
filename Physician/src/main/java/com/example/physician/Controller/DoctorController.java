package com.example.physician.Controller;


import com.example.physician.DTO.DoctorDTO;
import com.example.physician.Entity.Doctor;
import com.example.physician.Services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody DoctorDTO dto){
       Doctor doctor1 = doctorService.addDoctor(dto);
       return ResponseEntity.ok(doctor1);
    }

    @GetMapping
    public List<DoctorDTO> getAllDoctor(){
       return this.doctorService.getAllDoctor();


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctorId(@PathVariable Long id){

        doctorService.deleteDoctorId(id);
        return ResponseEntity.ok("doctor deleted successfully"+id);
    }

}
