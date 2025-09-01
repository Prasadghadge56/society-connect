package com.society.controller;

import com.society.entity.Visitor;
import com.society.service.VisitorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    // Add Visitor
    @PostMapping
    public Visitor addVisitor(@RequestBody Visitor visitor) {
        return visitorService.addVisitor(visitor);
    }

    // Get All Visitors
    @GetMapping
    public List<Visitor> getAllVisitors() {
        return visitorService.getAllVisitors();
    }

    // Update Visitor (outTime etc.)
    @PutMapping("/{id}")
    public Visitor updateVisitor(@PathVariable Long id, @RequestBody Visitor visitor) {
        return visitorService.updateVisitor(id, visitor);
    }

    // Delete Visitor
    @DeleteMapping("/{id}")
    public String deleteVisitor(@PathVariable Long id) {
        visitorService.deleteVisitor(id);
        return "Visitor deleted successfully!";
    }
}
