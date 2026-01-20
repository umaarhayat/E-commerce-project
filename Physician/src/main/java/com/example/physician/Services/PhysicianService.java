package com.example.physician.Services;


import com.example.physician.DTO.PhysicianDTO;
import com.example.physician.Entity.Doctor;
import com.example.physician.Entity.Physician;
import com.example.physician.Repository.DoctorRepo;
import com.example.physician.Repository.PhysicianRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PhysicianService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private PhysicianRepo physicianRepo;
    public Physician savePhysician(PhysicianDTO dto ){

        Physician physician= new Physician();;
        physician.setName(dto.getName());
        physician.setSpecialization(dto.getSpecialization());

        Doctor doctor = (Doctor) this.doctorRepo.findByName(dto.getDoctorName());
        physician.setDoctor( doctor);

      return physicianRepo.save(physician);

    }

    // get all physician
    public List<PhysicianDTO> getAllPhysician( ){

       List<Physician> physician = (List<Physician>) physicianRepo.findAll();

       List<PhysicianDTO> dto= new ArrayList<>();

       for (Physician physician1:physician){

           PhysicianDTO physicianDTO= new PhysicianDTO();
           physicianDTO.setName(physician1.getName());
           physicianDTO.setSpecialization(physician1.getSpecialization());
           physicianDTO.setDoctorName(physician1.getDoctor().getName());

           dto.add(physicianDTO);
       }
       return dto;


    }
    // delete by id
    public void deletePhysician(Long id){
               this.physicianRepo.deleteById(id);

    }


}
