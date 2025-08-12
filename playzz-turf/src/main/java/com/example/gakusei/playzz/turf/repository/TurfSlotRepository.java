package com.example.gakusei.playzz.turf.repository;

import com.example.gakusei.playzz.turf.model.TurfSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurfSlotRepository extends JpaRepository<TurfSlot, Long> {

    @Modifying
    @Query("DELETE FROM TurfSlot t WHERE t.slotDate < :date")
    int deleteBySlotDateBefore(LocalDate date);

    boolean existsBySlotDate(LocalDate date);

    boolean existsBySlotDateAndSlotStartTime(LocalDate date, LocalTime startTime);

    @Query("SELECT s FROM TurfSlot s WHERE (s.slotDate > :today OR (s.slotDate = :today AND s.slotStartTime >= :currentTime)) AND s.slotStatus = 'OPEN'")
    List<TurfSlot> findAvailableSlots(@Param("today") LocalDate today, @Param("currentTime") LocalTime currentTime);

    Optional<TurfSlot> findBySlotDateAndSlotStartTime(LocalDate date, LocalTime slotStartTime);
}
