package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.services.VisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<Visit> create(@RequestBody Visit visit) {
        return ResponseEntity.status(201).body(visitService.create(visit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visit> findById(@PathVariable Integer id) {
        Visit result = visitService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Visit> findAll() {
        return visitService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visit> update(@PathVariable Integer id, @RequestBody Visit visit) {
        return ResponseEntity.ok(visitService.update(id, visit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
