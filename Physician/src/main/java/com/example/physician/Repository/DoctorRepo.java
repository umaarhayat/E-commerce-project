package com.example.physician.Repository;

import com.example.physician.Entity.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends CrudRepository<Doctor,Long> {
    List<Doctor> findByName(String doctorName);
}
