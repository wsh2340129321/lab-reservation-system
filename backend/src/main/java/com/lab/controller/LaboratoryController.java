package com.lab.controller;

import com.lab.entity.Laboratory;
import com.lab.service.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/laboratories")
public class LaboratoryController {
    @Autowired
    private LaboratoryService laboratoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Laboratory laboratory) {
        Laboratory createdLaboratory = laboratoryService.create(laboratory);
        return ResponseEntity.ok(createdLaboratory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Laboratory laboratory) {
        laboratory.setId(id);
        Laboratory updatedLaboratory = laboratoryService.update(laboratory);
        return ResponseEntity.ok(updatedLaboratory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        laboratoryService.delete(id);
        return ResponseEntity.ok("删除成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Laboratory laboratory = laboratoryService.findById(id);
        return ResponseEntity.ok(laboratory);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Laboratory> laboratories = laboratoryService.findAll();
        return ResponseEntity.ok(laboratories);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String keyword) {
        List<Laboratory> laboratories = laboratoryService.findByCondition(name, type, location, keyword);
        return ResponseEntity.ok(laboratories);
    }

    @GetMapping("/{id}/available-times")
    public ResponseEntity<?> getAvailableTimes(@PathVariable Long id, @RequestParam String date) {
        List<Map<String, Object>> availableTimes = laboratoryService.getAvailableTimes(id, date);
        return ResponseEntity.ok(availableTimes);
    }
}