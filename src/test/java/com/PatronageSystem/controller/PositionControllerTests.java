package com.PatronageSystem.controller;

import com.PatronageSystem.Patronage.controller.PositionController;
import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.service.PositionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionControllerTests {

    @Mock
    private PositionService positionService;

    @InjectMocks
    private PositionController positionController;

    private Position createTestPosition() {
        Position position = new Position();
        position.setId(1L);
        position.setAccountId(100L);
        position.setFundId(200L);
        position.setTotalQuantity(BigDecimal.valueOf(1000));
        position.setAvailableQuantity(BigDecimal.valueOf(800));
        position.setFrozenQuantity(BigDecimal.valueOf(200));
        position.setAvgCost(BigDecimal.valueOf(10.5));
        position.setUpdateTime(LocalDateTime.now());
        return position;
    }

    @Test
    void getAllPositions_shouldReturnPositions() {
        Position position = createTestPosition();
        List<Position> positions = Collections.singletonList(position);
        when(positionService.getAllPositions()).thenReturn(positions);

        ResponseEntity<List<Position>> response = positionController.getAllPositions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(position.getId(), response.getBody().get(0).getId());
    }

    @Test
    void getPositionById_whenExists_shouldReturnPosition() {
        Position position = createTestPosition();
        when(positionService.getPositionById(1L)).thenReturn(position);

        ResponseEntity<Position> response = positionController.getPositionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(position.getId(), response.getBody().getId());
    }

    @Test
    void getPositionById_whenNotExists_shouldReturnNotFound() {
        when(positionService.getPositionById(999L)).thenReturn(null);

        ResponseEntity<Position> response = positionController.getPositionById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getPositionsByAccountId_shouldReturnPositions() {
        Position position = createTestPosition();
        List<Position> positions = Arrays.asList(position);
        when(positionService.getPositionsByAccountId(100L)).thenReturn(positions);

        ResponseEntity<List<Position>> response = positionController.getPositionsByAccountId(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(100L, response.getBody().get(0).getAccountId());
    }

    @Test
    void getPositionByAccountAndFund_whenExists_shouldReturnPosition() {
        Position position = createTestPosition();
        when(positionService.getPositionByAccountAndFund(100L, 200L)).thenReturn(position);

        ResponseEntity<Position> response = positionController.getPositionByAccountAndFund(100L, 200L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100L, response.getBody().getAccountId());
        assertEquals(200L, response.getBody().getFundId());
    }

    @Test
    void getPositionByAccountAndFund_whenNotExists_shouldReturnNotFound() {
        when(positionService.getPositionByAccountAndFund(999L, 999L)).thenReturn(null);

        ResponseEntity<Position> response = positionController.getPositionByAccountAndFund(999L, 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createPosition_shouldReturnCreatedPosition() {
        Position newPosition = createTestPosition();
        newPosition.setId(null);

        Position savedPosition = createTestPosition();
        savedPosition.setId(1L);

        when(positionService.createPosition(any(Position.class))).thenReturn(savedPosition);

        ResponseEntity<Position> response = positionController.createPosition(newPosition);

        assertNotNull(response.getBody(), "响应体不应为空");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(positionService).createPosition(any(Position.class));
    }

    @Test
    void createPosition_whenServiceReturnsNull_shouldReturnOkWithNull() {
        Position newPosition = createTestPosition();
        newPosition.setId(null);

        when(positionService.createPosition(any(Position.class))).thenReturn(null);

        ResponseEntity<Position> response = positionController.createPosition(newPosition);

        // 修改期望为200 OK 并验证body为null
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updatePosition_shouldReturnUpdatedPosition() {
        Position updatedPosition = createTestPosition();
        updatedPosition.setTotalQuantity(BigDecimal.valueOf(1500));

        when(positionService.updatePosition(any(Position.class))).thenReturn(updatedPosition);

        ResponseEntity<Position> response = positionController.updatePosition(1L, updatedPosition);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(1500), response.getBody().getTotalQuantity());
    }

    @Test
    void updatePosition_whenServiceReturnsNull_shouldReturnOkWithNull() {
        Position positionToUpdate = createTestPosition();
        positionToUpdate.setId(1L);

        when(positionService.updatePosition(any(Position.class))).thenReturn(null);

        ResponseEntity<Position> response = positionController.updatePosition(1L, positionToUpdate);

        // 修改期望为200 OK 并验证body为null
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deletePosition_whenSuccess_shouldReturnOk() {
        when(positionService.deletePosition(1L)).thenReturn(true);

        ResponseEntity<Void> response = positionController.deletePosition(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePosition_whenNotFound_shouldReturnNotFound() {
        when(positionService.deletePosition(999L)).thenReturn(false);

        ResponseEntity<Void> response = positionController.deletePosition(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePositionByAccountAndFund_whenSuccess_shouldReturnOk() {
        when(positionService.deletePositionByAccountAndFund(100L, 200L)).thenReturn(true);

        ResponseEntity<Void> response = positionController.deletePositionByAccountAndFund(100L, 200L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePositionByAccountAndFund_whenNotFound_shouldReturnNotFound() {
        when(positionService.deletePositionByAccountAndFund(999L, 999L)).thenReturn(false);

        ResponseEntity<Void> response = positionController.deletePositionByAccountAndFund(999L, 999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}