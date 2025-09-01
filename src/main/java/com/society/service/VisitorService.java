package com.society.service;

import com.society.entity.Visitor;
import com.society.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public Visitor addVisitor(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public Visitor updateVisitor(Long id, Visitor updatedVisitor) {
        return visitorRepository.findById(id)
                .map(visitor -> {
                    visitor.setVisitorName(updatedVisitor.getVisitorName());
                    visitor.setBikeNumber(updatedVisitor.getBikeNumber());
                    visitor.setFlatNo(updatedVisitor.getFlatNo());
                    visitor.setInTime(updatedVisitor.getInTime());
                    visitor.setOutTime(updatedVisitor.getOutTime());
                    visitor.setAddedByStaff(updatedVisitor.getAddedByStaff());
                    return visitorRepository.save(visitor);
                })
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
    }

    public void deleteVisitor(Long id) {
        visitorRepository.deleteById(id);
    }
}
