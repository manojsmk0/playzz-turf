package com.example.gakusei.playzz.turf.repository;

import com.example.gakusei.playzz.turf.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
 Users findByEmail(String email);
}
