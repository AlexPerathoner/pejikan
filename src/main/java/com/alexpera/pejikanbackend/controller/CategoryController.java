package com.alexpera.pejikanbackend.controller;

import com.alexpera.pejikanbackend.model.Category;
import com.alexpera.pejikanbackend.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller("/categories")
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryRepo.findAll();
        categories.sort(Comparator.comparing(Category::getName));
        model.addAttribute("categories", categories);
        return "categories";
    }
    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name, @RequestParam(required = false) String color, Model model) {
        categoryRepo.save(new Category(name, color));
        return "redirect:/categories";
    }
    @PostMapping("/categories/delete")
    public String deleteCategory(@RequestParam String id, Model model) {
        categoryRepo.deleteById(id);
        return "redirect:/categories";
    }
}
