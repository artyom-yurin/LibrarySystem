package com.example.demo.repository;

import com.example.demo.entity.document.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer>{
    Publisher findById(Integer id);
    Publisher findByPublisherName(String publisherName);
}
