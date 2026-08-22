package com.utkarsh2573.backend.visit.repository;

import com.utkarsh2573.backend.common.enums.QueueStatus;
import com.utkarsh2573.backend.visit.entity.VisitQueue;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitQueueRepository extends JpaRepository<VisitQueue, Long> {

    Optional<VisitQueue> findByVisitId(Long visitId);

    List<VisitQueue> findByDoctorIdAndQueueDateOrderByQueueNumberAsc(
            Long doctorId,
            LocalDate queueDate
    );

    @Query("""
        select coalesce(max(q.queueNumber), 0)
        from VisitQueue q
        where q.doctor.id = :doctorId
        and q.queueDate = :queueDate
    """)
    Integer findMaxQueueNumber(
            @Param("doctorId") Long doctorId,
            @Param("queueDate") LocalDate queueDate
    );

    long countByDoctorIdAndQueueDateAndStatus(
            Long doctorId,
            LocalDate queueDate,
            QueueStatus status
    );
}
