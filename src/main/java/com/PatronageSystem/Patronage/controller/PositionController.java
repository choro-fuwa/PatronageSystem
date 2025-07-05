package com.PatronageSystem.Patronage.controller;

import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade/positions")
@CrossOrigin(origins = "*")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping
    public ResponseEntity<List<Position>> getAllPositions() {
        List<Position> positions = positionService.getAllPositions();
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable Long id) {
        Position position = positionService.getPositionById(id);
        if (position != null) {
            return ResponseEntity.ok(position);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Position>> getPositionsByAccountId(@PathVariable Long accountId) {
        List<Position> positions = positionService.getPositionsByAccountId(accountId);
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/account/{accountId}/fund/{fundId}")
    public ResponseEntity<Position> getPositionByAccountAndFund(@PathVariable Long accountId, @PathVariable Long fundId) {
        Position position = positionService.getPositionByAccountAndFund(accountId, fundId);
        if (position != null) {
            return ResponseEntity.ok(position);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Position> createPosition(@RequestBody Position position) {
        Position createdPosition = positionService.createPosition(position);
        return ResponseEntity.ok(createdPosition);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable Long id, @RequestBody Position position) {
        position.setId(id);
        Position updatedPosition = positionService.updatePosition(position);
        return ResponseEntity.ok(updatedPosition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        boolean deleted = positionService.deletePosition(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/account/{accountId}/fund/{fundId}")
    public ResponseEntity<Void> deletePositionByAccountAndFund(@PathVariable Long accountId, @PathVariable Long fundId) {
        boolean deleted = positionService.deletePositionByAccountAndFund(accountId, fundId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
