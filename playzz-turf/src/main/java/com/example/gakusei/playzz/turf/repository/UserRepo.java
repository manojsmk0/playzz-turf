package com.example.gakusei.playzz.turf.repository;

import com.example.gakusei.playzz.turf.model.Role;
import com.example.gakusei.playzz.turf.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
 Users findByEmail(String email);
 Optional<Users> findByUserName(String userName);
 boolean existsByEmail(String email);
 boolean existsByUserName(String userName);
 boolean existsByRole(Role role);


}
