package com.alexpera.pejikan.controller;

import com.alexpera.pejikan.model.Category;
import com.alexpera.pejikan.model.Entry;
import com.alexpera.pejikan.repo.CategoryRepo;
import com.alexpera.pejikan.repo.EntryRepo;
import com.alexpera.pejikan.service.DateCalculator;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller("/entries")
public class EntryController {
    @Autowired
    private EntryRepo entriesRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    DateFormat weekFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    DateFormat dateInQueryFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/entries")
    public String getEntries(@RequestParam(value = "weekStart", required = false) String weekStartStr, Model model) throws ParseException {
        Date weekStart = DateCalculator.getWeekStartDate(new Date());
        if (!StringUtils.isEmpty(weekStartStr)) {
            weekStart = DateCalculator.getWeekStartDate(dateInQueryFormat.parse(weekStartStr));
        }
        Date weekEnd = DateCalculator.getWeekEndDate(weekStart);

        model.addAttribute("weekStart", weekFormat.format(weekStart));
        model.addAttribute("weekEnd", weekFormat.format(weekEnd));

        List<Entry> entries = entriesRepo.getAllInWeek(weekStart, weekEnd);
        model.addAttribute("entries", entries);

        entries.sort(Comparator.comparing(Entry::getStart));

        List<Category> allCategories = categoryRepo.findAll();
        model.addAttribute("categories", allCategories);

        AtomicReference<Duration> total = new AtomicReference<>(Duration.ZERO);
        entries.stream().map(Entry::getTotal).forEach(d -> total.set(total.get().plus(d)));
        model.addAttribute("total", total.get());

        model.addAttribute("today", dateFormat.format(new Date()));
        return "entries";
    }

    @PostMapping("/entries/add")
    public String addEntry(@RequestParam String linkedId,
                           @RequestParam String title,
                           @RequestParam String description,
                           @RequestParam String category,
                           @RequestParam String start,
                           @RequestParam String end,
                           @RequestParam String correction,
                           Model model) throws ParseException {
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);

        Duration correctionConv = Duration.ZERO;
        if (!StringUtils.isBlank(correction)) {
            Date correctionDate = timeFormat.parse(correction);
            correctionConv = Duration.ofHours(correctionDate.getHours()).plusMinutes(correctionDate.getMinutes());
        }
        Duration total = Duration.between(startDate.toInstant(), endDate.toInstant()).minus(correctionConv).abs();
        if (endDate.before(startDate)) {
            Date tmp = startDate;
            startDate = endDate;
            endDate = tmp;
        }
        entriesRepo.save(new Entry(linkedId, title, description, new Category(category), startDate, endDate, correctionConv, total));
        return "redirect:/entries";
    }
    @PostMapping("/entries/delete")
    public String deleteEntry(@RequestParam Integer id, Model model) {
        entriesRepo.deleteById(String.valueOf(id));
        return "redirect:/entries";
    }
}
