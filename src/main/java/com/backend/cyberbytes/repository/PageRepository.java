package com.backend.cyberbytes.repository;

import com.backend.cyberbytes.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {
    Page findBySlug(String slug);
    List<Page> findByTitleContainingIgnoreCase(String query);
    List<Page> findByTopic(String topic);
}
