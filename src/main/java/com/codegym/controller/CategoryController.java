package com.codegym.controller;


import com.codegym.exception.NotFoundException;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IProductService productService;

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView notFoundPage(){
        return new ModelAndView("error-404");
    }

    @GetMapping("/list")
    public ModelAndView showListCategory(@RequestParam(name = "q") Optional<String> q,@RequestParam(name = "page",required = false, defaultValue = "0") Integer page) {
        ModelAndView modelAndView = new ModelAndView("/category/list");
        PageRequest pageable = PageRequest.of(page,2, Sort.by("id").ascending());
        Page<Category> categories;
        categories = categoryService.findAll(pageable);
        if (q.isPresent()) {
            categories = categoryService.findAllByNameContaining(q.get(), pageable);
        }
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView ShowDeleteCategory() {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView CreateCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return new ModelAndView("redirect:/categories/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView ShowEdiCategory(@PathVariable Long id) throws NotFoundException {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            throw new NotFoundException();
        }
        ModelAndView modelAndView = new ModelAndView("/category/edit");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView EditCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return new ModelAndView("redirect:/categories/list");
    }


    @GetMapping("/delete/{id}")
    public ModelAndView ShowDeleteCategory(@PathVariable Long id) throws NotFoundException {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            throw new NotFoundException();
        }
        ModelAndView modelAndView = new ModelAndView("/category/delete");
        modelAndView.addObject("category", category.get());
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView DeleteCategory(@PathVariable Long id) throws NotFoundException {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            throw new NotFoundException();
        }
        categoryService.deleteCategory(id);
        return new ModelAndView("redirect:/categories/list");
    }
    @GetMapping("/viewByCategory/{id}")
    public ModelAndView ShowProductByCategoryId(@PathVariable Long id, @PageableDefault(value = 5) Pageable pageable) {
        Page<Product> products;
        products = productService.getProductWithName(id, pageable);
        ModelAndView modelAndView = new ModelAndView("/product/listProductByCategory");
        modelAndView.addObject("products", products);
        modelAndView.addObject("id",id);
        return modelAndView;
    }

}