package com.example.cinema.controller;

import com.example.cinema.entity.Screening;
import com.example.cinema.repository.ScreeningRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Sort;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ScreeningController {

    private final ScreeningRepository repository;

    public ScreeningController(ScreeningRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/screenings";
    }

    // Список сеансов + форма добавления
    @GetMapping("/screenings")
    public String list(@RequestParam(defaultValue = "") String q,
                       @RequestParam(required = false) String sort,
                       Model model) {
        fillModel(model, q, sort);
        model.addAttribute("screening", new Screening());
        return "index";
    }

    // Добавление
    @PostMapping("/screenings/add")
    public String add(@Valid @ModelAttribute("screening") Screening screening,
                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            fillModel(model, "", null);
            return "index";
        }
        repository.save(screening);
        return "redirect:/screenings";
    }

    // Страница редактирования
    @GetMapping("/screenings/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Screening screening = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс не найден: " + id));
        model.addAttribute("screening", screening);
        return "edit";
    }

    // Сохранение изменений
    @PostMapping("/screenings/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("screening") Screening screening,
                         BindingResult result) {
        screening.setId(id);
        if (result.hasErrors()) {
            return "edit";
        }
        repository.save(screening);
        return "redirect:/screenings";
    }

    // Удаление
    @PostMapping("/screenings/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/screenings";
    }

    private void fillModel(Model model, String q, String sort) {
        Sort order = "date".equals(sort)
                ? Sort.by("sessionDateTime").ascending()
                : Sort.unsorted();
        List<Screening> list = repository.search(q.trim(), order);
        model.addAttribute("screenings", list);
        model.addAttribute("count", list.size());
        model.addAttribute("q", q);
        model.addAttribute("sort", sort);
        fillChart(model);
    }
    //данные для диаграммы, количество сеансов по дням за последние 60 дней
    private void fillChart(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(59);

        List<Object[]> rows = repository.countByDay(
                start.atStartOfDay(), today.plusDays(1).atStartOfDay());

        Map<LocalDate, Long> counts = new HashMap<>();
        for (Object[] row : rows) {
            counts.put(LocalDate.parse(String.valueOf(row[0])), ((Number) row[1]).longValue());
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM");
        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(today); d = d.plusDays(1)) {
            labels.add(d.format(fmt));
            values.add(counts.getOrDefault(d, 0L)); // дни без сеансов = 0
        }
        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartValues", values);
    }
}