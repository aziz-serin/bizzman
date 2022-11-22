package com.bizzman.dao.repos;

import com.bizzman.entities.Expense;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    @Override
    long count();
}
