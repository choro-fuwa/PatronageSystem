package org.example.fundproject.repository;

import org.example.fundproject.entity.FundCombination;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FundCombinationRepository {

    private final Map<String, FundCombination> combinations = new ConcurrentHashMap<>();

    public FundCombination save(FundCombination combination) {
        combinations.put(combination.getId(), combination);
        return combination;
    }

    public Optional<FundCombination> findById(String id) {
        return Optional.ofNullable(combinations.get(id));
    }

    public List<FundCombination> findAll() {
        return new ArrayList<>(combinations.values());
    }

    public void deleteById(String id) {
        combinations.remove(id);
    }
}
