package com.example.demo.repository;

import com.example.demo.entity.information.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer>{
}
