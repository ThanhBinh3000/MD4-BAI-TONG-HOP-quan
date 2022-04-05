package com.codegym.controller;


import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@CrossOrigin("*")
@RequestMapping("/categories")
public class CategoryRestController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    IProductService productService;

    @GetMapping // hiển thị danh mục sản phẩm category
    public ResponseEntity<Iterable<Category>> findAll(@RequestParam(name = "q") Optional<String> q, @RequestParam(name = "page",required = false, defaultValue = "0") Integer page){
        Iterable<Category> categories = categoryService.findAll();
        PageRequest pageable = PageRequest.of(page,2, Sort.by("price").ascending());
        if (q.isPresent()){
            categories = categoryService.findAllByNameContaining(q.get(), pageable);

        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/{id}")//tìm kiếm danh mục category theo id
    public ResponseEntity<Category> findById(@PathVariable Long id){
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(categoryOptional.get(),HttpStatus.OK);
    }

    @GetMapping("/view/{id}")// tìm kiếm id category thì hiện ra tất cả product có id như đã tìm
    public ResponseEntity<Iterable<Product>> findByIdCategoryProduct(@PathVariable Long id,Pageable pageable){
        Iterable<Product> products = productService.getProductWithName(id, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping //thêm sản phẩm mới category
    public ResponseEntity<Category> seve(@RequestBody Category category){
        return new ResponseEntity<>(categoryService.save(category),HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // sửa sản phẩm category
    public ResponseEntity<Category> updateProduct(@PathVariable Long id, @RequestBody Category newCategory){
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newCategory.setId(id);
        return new ResponseEntity<>(categoryService.save(newCategory),HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // xóa sản phẩm category
    public ResponseEntity<Category> deleteProduct(@PathVariable Long id){
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.removeById(id);
        return new ResponseEntity<>(categoryOptional.get(), HttpStatus.OK);
    }

}
