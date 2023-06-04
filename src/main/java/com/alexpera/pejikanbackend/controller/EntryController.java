package com.alexpera.pejikanbackend.controller;

import com.alexpera.pejikanbackend.model.Category;
import com.alexpera.pejikanbackend.model.Entry;
import com.alexpera.pejikanbackend.repo.CategoryRepo;
import com.alexpera.pejikanbackend.repo.EntryRepo;
import com.alexpera.pejikanbackend.service.DateCalculator;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        List<Category> allCategories = categoryRepo.findAll();
        model.addAttribute("categories", allCategories);

        Time total = new Time(0, 0, 0);
        entries.forEach(entry -> total.setTime(total.getTime() + entry.getTotal().getTime()));
        model.addAttribute("total", total);
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

        Time correctionConv = new Time(0, 0, 0);
        if (!StringUtils.isBlank(correction)) {
            Date correctionDate = timeFormat.parse(correction);
            correctionConv = new Time(correctionDate.getHours(), correctionDate.getMinutes(), 0);
        }

        Time total = new Time(startDate.getTime() - endDate.getTime() - correctionConv.getTime());
        entriesRepo.save(new Entry(linkedId, title, description, new Category(category), startDate, endDate, correctionConv, total));
        return getEntries(null, model);
    }
}
