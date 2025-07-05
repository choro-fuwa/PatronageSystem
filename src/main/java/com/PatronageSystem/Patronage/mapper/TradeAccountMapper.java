package com.PatronageSystem.Patronage.mapper;

import com.PatronageSystem.Patronage.entity.TradeAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeAccountMapper {
    List<TradeAccount> selectAll();

    TradeAccount selectById(@Param("id") Long id);

    TradeAccount selectByAccountCode(@Param("accountCode") String accountCode);

    List<TradeAccount> selectByUserId(@Param("userId") Long userId);

    List<TradeAccount> selectByStatus(@Param("accountStatus") String accountStatus);

    int insert(TradeAccount tradeAccount);

    int update(TradeAccount tradeAccount);

    int deleteById(@Param("id") Long id);

    int updateAccountStatus(@Param("id") Long id, @Param("accountStatus") String accountStatus);

    List<TradeAccount> searchAccounts(
        @Param("accountStatus") String accountStatus,
        @Param("accountType") String accountType
    );
}
