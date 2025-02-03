package org.Proiect.Servicii.Repository;

import org.Proiect.Domain.App.Status;
import org.Proiect.Domain.Proiect.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    // Găsire task-uri după proiect
    List<Task> findByProiectId(Integer proiectId);

    // Găsire task-uri după deadline apropiat
    @Query("SELECT t FROM Task t WHERE t.deadline BETWEEN :start AND :end")
    List<Task> findByDeadlineBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Găsire task-uri după status și membru
    List<Task> findByStatusAndMembruUserId(Status status, Integer membruId);
}
