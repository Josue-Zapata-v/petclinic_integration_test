package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.services.SpecialtyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @PostMapping
    public ResponseEntity<Specialty> create(@RequestBody Specialty specialty) {
        return ResponseEntity.status(201).body(specialtyService.create(specialty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Integer id) {
        Specialty result = specialtyService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Specialty> findAll() {
        return specialtyService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@PathVariable Integer id, @RequestBody Specialty specialty) {
        return ResponseEntity.ok(specialtyService.update(id, specialty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        specialtyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
