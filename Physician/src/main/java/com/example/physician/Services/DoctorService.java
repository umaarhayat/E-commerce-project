package com.example.physician.Services;

import com.example.physician.DTO.DoctorDTO;
import com.example.physician.Entity.Doctor;
import com.example.physician.Repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hibernate.Hibernate.map;
import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepo;

    // add doctor
    public Doctor addDoctor(DoctorDTO dto){

        Doctor doctor= new Doctor();
        doctor.setName(dto.getName());
        doctorRepo.save(doctor);
        return doctor;
    }

    // get all
    public List<DoctorDTO> getAllDoctor() {

        // pehle DB se doctors le aate hain
        Iterable<Doctor> doctors=  doctorRepo.findAll();

        List<DoctorDTO> dtos = new ArrayList<>();
        // empty Dto list hai
        for (Doctor doctor : doctors) {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doctor.getId());
            dto.setName(doctor.getName());
            dtos.add(dto);
        }
        return dtos;

    }
    // delete id

    public void deleteDoctorId(Long id){
        doctorRepo.deleteById(id);

    }

}
