package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
//    public Role findByName(RoleName roleName) {
//        return roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//    }
    public void createDefaultRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(RoleName.ROLE_SELLER));
            roleRepository.save(new Role(RoleName.ROLE_BUYER));
        }
    }
}
