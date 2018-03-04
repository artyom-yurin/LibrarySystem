package com.example.demo.service;


import com.example.demo.entity.user.Role;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findById(Integer id) {
        return roleRepository.findById(id);
    }

    public Integer getIdByName(String name)
    {
        Integer id;
        name = name.toLowerCase();
        switch (name) {
            case "admin":
                id = 1;
                break;
            case "patron":
                id = 2;
                break;
            case "faculty":
                id = 3;
                break;
            default:
                throw new RoleNotFoundException();
        }
        return id;
    }
}
