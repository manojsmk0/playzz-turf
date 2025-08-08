package com.example.gakusei.playzz.turf.repository;

import com.example.gakusei.playzz.turf.model.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailsRepo extends JpaRepository<BookingDetails,Long> {
}
