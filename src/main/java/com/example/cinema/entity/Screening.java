package com.example.cinema.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String filmTitle;

    @NotBlank(message = "Киностудия не может быть пустой")
    private String productionCompany;

    @NotNull(message = "Укажите дату и время сеанса")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sessionDateTime;

    @NotNull(message = "Укажите количество билетов")
    @Min(value = 0, message = "Количество билетов не может быть отрицательным")
    private Integer ticketCount;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilmTitle() { return filmTitle; }
    public void setFilmTitle(String filmTitle) { this.filmTitle = filmTitle; }

    public String getProductionCompany() { return productionCompany; }
    public void setProductionCompany(String productionCompany) { this.productionCompany = productionCompany; }

    public LocalDateTime getSessionDateTime() { return sessionDateTime; }
    public void setSessionDateTime(LocalDateTime sessionDateTime) { this.sessionDateTime = sessionDateTime; }

    public Integer getTicketCount() { return ticketCount; }
    public void setTicketCount(Integer ticketCount) { this.ticketCount = ticketCount; }
}