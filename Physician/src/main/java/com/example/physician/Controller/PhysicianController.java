package com.example.physician.Controller;


import com.example.physician.DTO.PhysicianDTO;
import com.example.physician.Entity.Physician;
import com.example.physician.Services.PhysicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/physicians")
public class PhysicianController {

    @Autowired
    private PhysicianService physicianService;
    @PostMapping
 public ResponseEntity<Physician> addPhysician(@RequestBody PhysicianDTO dto) {
        Physician physician1 = physicianService.savePhysician(dto);
        return ResponseEntity.ok(physician1);

    }
 @GetMapping
 public List<PhysicianDTO> getAllPhysician(){
      return physicianService.getAllPhysician();
 }
 @DeleteMapping("/{id}")
 public ResponseEntity<String> deletePhysician(@PathVariable Long id){
       this.physicianService.deletePhysician(id);

       return ResponseEntity.ok("Physician deleted successfully"+id);
 }
}
