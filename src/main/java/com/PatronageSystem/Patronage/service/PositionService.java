package com.PatronageSystem.Patronage.service;

import com.PatronageSystem.Patronage.entity.Position;

import java.util.List;

public interface PositionService {
    List<Position> getAllPositions();

    Position getPositionById(Long id);

    List<Position> getPositionsByAccountId(Long accountId);

    Position getPositionByAccountAndFund(Long accountId, Long fundId);

    Position createPosition(Position position);

    Position updatePosition(Position position);

    boolean deletePosition(Long id);

    boolean deletePositionByAccountAndFund(Long accountId, Long fundId);
}
