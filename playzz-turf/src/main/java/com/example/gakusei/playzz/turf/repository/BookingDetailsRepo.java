package com.example.gakusei.playzz.turf.repository;

import com.example.gakusei.playzz.turf.model.BookingDetails;
import com.example.gakusei.playzz.turf.model.BookingStatus;
import com.example.gakusei.playzz.turf.model.SlotStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingDetailsRepo extends JpaRepository<BookingDetails,Long> {
    boolean existsBySlotIdAndUserIdAndBookingStatus(Integer slotId, Long userId, BookingStatus status);
    Optional<BookingDetails> findBySlotIdAndUserIdAndBookingStatus(
            Integer slotId,
            Long userId,
            BookingStatus bookingStatus
    );
    @Query("SELECT b FROM BookingDetails b WHERE " +
            " (b.slotDate = :date) AND " +
            " (b.slotStartTime BETWEEN :startTime AND :endTime) AND " +
            " b.bookingStatus = :status")
    List<BookingDetails> findBySlotDateAndStartTimeBetweenAndStatus(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("status") BookingStatus status);


}
