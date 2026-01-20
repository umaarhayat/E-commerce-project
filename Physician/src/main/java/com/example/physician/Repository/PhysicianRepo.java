package com.example.physician.Repository;

import com.example.physician.Entity.Physician;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicianRepo extends CrudRepository<Physician,Long> {
}
