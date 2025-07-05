package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PositionMapper {
    List<Position> selectAll();

    Position selectById(@Param("id") Long id);

    List<Position> selectByAccountId(@Param("accountId") Long accountId);

    Position selectByAccountAndFund(@Param("accountId") Long accountId, @Param("fundId") Long fundId);

    int insert(Position position);

    int update(Position position);

    int deleteById(@Param("id") Long id);

    int deleteByAccountAndFund(@Param("accountId") Long accountId, @Param("fundId") Long fundId);
}
