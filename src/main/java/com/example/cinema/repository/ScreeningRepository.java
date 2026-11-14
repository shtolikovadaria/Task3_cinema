package com.example.cinema.repository;

import com.example.cinema.entity.Screening;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    // Поиск по названию фильма, киностудии и количеству билетов
    @Query("""
            SELECT s FROM Screening s
            WHERE s.productionCompany IS NOT NULL
              AND TRIM(s.productionCompany) <> ''
              AND (LOWER(s.filmTitle) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(s.productionCompany) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR CAST(s.ticketCount AS string) LIKE CONCAT('%', :q, '%'))
            """)
    List<Screening> search(@Param("q") String q, Sort sort);
    // Количество сеансов по дням за период
    @Query("""
            SELECT CAST(s.sessionDateTime AS LocalDate), COUNT(s)
            FROM Screening s
            WHERE s.sessionDateTime >= :from AND s.sessionDateTime < :to
              AND s.productionCompany IS NOT NULL
              AND TRIM(s.productionCompany) <> ''
            GROUP BY CAST(s.sessionDateTime AS LocalDate)
            """)
    List<Object[]> countByDay(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}