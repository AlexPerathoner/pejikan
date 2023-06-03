package com.alexpera.pejikanbackend.repo;

import com.alexpera.pejikanbackend.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepo extends JpaRepository<Entry, String> {
}
