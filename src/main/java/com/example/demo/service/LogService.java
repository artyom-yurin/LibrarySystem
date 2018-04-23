package com.example.demo.service;

import com.example.demo.entity.information.Log;
import com.example.demo.entity.user.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {
    private UserRepository userRepository;
    private LogRepository logRepository;

    public LogService(LogRepository logRepository, UserRepository userRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    public List<Log> findAll() {
        return logRepository.findAll();
    }

    public void save(Log log) {
        logRepository.save(log);
    }

    public void newLog(Integer userId, String message) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Log log = new Log(user, new Date(System.currentTimeMillis()), message);
        logRepository.save(log);
    }
}
