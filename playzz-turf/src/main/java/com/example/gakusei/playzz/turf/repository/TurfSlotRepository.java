package com.example.gakusei.playzz.turf.repository;

import com.example.gakusei.playzz.turf.model.SlotStatus;
import com.example.gakusei.playzz.turf.model.TurfSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurfSlotRepository extends JpaRepository<TurfSlot, Long> {
    @Modifying
    @Query("DELETE FROM TurfSlot t WHERE t.slotDate < :date")
    int deleteBySlotDateBefore(LocalDate date);

    boolean existsBySlotDate(LocalDate date);


    boolean existsBySlotDateAndStartTime(LocalDate date, LocalDateTime startTime);

    @Query("SELECT t FROM TurfSlot t " +
            "WHERE t.slotStatus = com.example.gakusei.playzz.turf.model.SlotStatus.OPEN " +
            "AND t.endTime > :currentTime " +
            "AND t.availableSlots > 0 " +
            "ORDER BY t.slotDate ASC, t.startTime ASC")
    List<TurfSlot> findAvailableSlots(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT COUNT(t) > 0 FROM TurfSlot t " +
            "WHERE t.slotDate = :date AND t.startTime = :startTime")
    boolean slotExists(@Param("date") LocalDate date,
                       @Param("startTime") LocalDateTime startTime);
}