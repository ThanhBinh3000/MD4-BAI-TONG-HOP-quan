package com.codegym.controller;


import com.codegym.model.Product;
import com.codegym.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/products")
public class ProductRestController {
    @Autowired
    private IProductService productService;

    @GetMapping// hiển thị product
    public ResponseEntity<Iterable<Product>> findAll(@RequestParam(name = "q") Optional<String> q,@RequestParam(name = "page",required = false, defaultValue = "0") Integer page) {
        Iterable<Product> products = productService.findAll();
        PageRequest pageable = PageRequest.of(page,2, Sort.by("price").ascending());
        if (q.isPresent()) {
            products = productService.findProductByNameContaining(q.get(),pageable);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")// tìm kiêm product theo id
    public ResponseEntity<Product> findById(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(productOptional.get(),HttpStatus.OK);
    }

    @PostMapping //thêm sản phẩm product
    public ResponseEntity<Product> save(@RequestBody Product product){
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);

    }

    @PutMapping("/{id}") // sửa sản phẩm product
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product newProduct){
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newProduct.setId(id);
        return new ResponseEntity<>(productService.save(newProduct),HttpStatus.OK);
    }

    @DeleteMapping("/{id}") // xóa sản phẩm product
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()){
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.removeById(id);
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }
}
