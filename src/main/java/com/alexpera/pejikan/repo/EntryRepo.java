package com.alexpera.pejikan.repo;

import com.alexpera.pejikan.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EntryRepo extends JpaRepository<Entry, String> {
    @Query(value = "select * from entries where " +
            "(start_date between :weekStart and :weekEnd) or " +
            "(end_date between :weekStart and :weekEnd)",
            nativeQuery = true)
    List<Entry> getAllInWeek(Date weekStart, Date weekEnd);
}
