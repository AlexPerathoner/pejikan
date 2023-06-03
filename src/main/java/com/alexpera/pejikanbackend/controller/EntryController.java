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

    @GetMapping("/entries")
    public String getEntries(Model model) {
        List<Entry> entries = entriesRepo.findAll();
        model.addAttribute("entries", entries);

        Date weekStart = DateCalculator.getWeekStartDate(new Date());
        Date weekEnd = DateCalculator.getWeekEndDate(new Date());
        model.addAttribute("weekStart", weekFormat.format(weekStart));
        model.addAttribute("weekEnd", weekFormat.format(weekEnd));

        List<Category> allCategories = categoryRepo.findAll();
        model.addAttribute("categories", allCategories);
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
        // parse html form date and time to java.util.Date
        Date startDate = dateFormat.parse(start);
        Date endDate = dateFormat.parse(end);

        Time correctionConv = new Time(0, 0, 0);
        if (!StringUtils.isBlank(correction)) {
            Date correctionDate = timeFormat.parse(correction);
            correctionConv = new Time(correctionDate.getHours(), correctionDate.getMinutes(), 0);
        }

        Time total = new Time(startDate.getTime() - endDate.getTime() - correctionConv.getTime());
        entriesRepo.save(new Entry(linkedId, title, description, new Category(category), startDate, endDate, correctionConv, total));
        return getEntries(model);
    }
}
