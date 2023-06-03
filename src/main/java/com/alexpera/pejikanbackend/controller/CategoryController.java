package com.alexpera.pejikanbackend.controller;

import com.alexpera.pejikanbackend.model.Category;
import com.alexpera.pejikanbackend.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "categories";
    }
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name, Model model) {
        categoryRepo.save(new Category(name));
        return getCategories(model);
    }
}
