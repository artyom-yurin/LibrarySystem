package com.example.demo.service;


import com.example.demo.entity.user.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name.toLowerCase());
    }

    public Role findByPosition(String position)
    {
        return roleRepository.findByPosition(position.toLowerCase());
    }
}
