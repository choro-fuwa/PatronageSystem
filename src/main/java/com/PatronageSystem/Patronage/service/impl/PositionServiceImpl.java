package com.PatronageSystem.Patronage.service.impl;

import com.PatronageSystem.Patronage.entity.Position;
import com.PatronageSystem.Patronage.mapper.PositionMapper;
import com.PatronageSystem.Patronage.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Override
    public List<Position> getAllPositions() {
        return positionMapper.selectAll();
    }

    @Override
    public Position getPositionById(Long id) {
        return positionMapper.selectById(id);
    }

    @Override
    public List<Position> getPositionsByAccountId(Long accountId) {
        return positionMapper.selectByAccountId(accountId);
    }

    @Override
    public Position getPositionByAccountAndFund(Long accountId, Long fundId) {
        return positionMapper.selectByAccountAndFund(accountId, fundId);
    }

    @Override
    public Position createPosition(Position position) {
        positionMapper.insert(position);
        return position;
    }

    @Override
    public Position updatePosition(Position position) {
        positionMapper.update(position);
        return position;
    }

    @Override
    public boolean deletePosition(Long id) {
        return positionMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deletePositionByAccountAndFund(Long accountId, Long fundId) {
        return positionMapper.deleteByAccountAndFund(accountId, fundId) > 0;
    }
}
