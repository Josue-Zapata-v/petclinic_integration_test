package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @PostMapping
    public ResponseEntity<Visit> create(@RequestBody Visit visit) {
        Visit newVisit = visitService.create(visit);
        return ResponseEntity.ok(newVisit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> findById(@PathVariable Integer id) {
        Visit visit = visitService.findById(id);
        if (visit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visit);
    }

    @GetMapping
    public ResponseEntity<List<Visit>> findAll() {
        return ResponseEntity.ok(visitService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> update(@PathVariable Integer id, @RequestBody Visit visit) {
        Visit updated = visitService.update(id, visit);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
