package com.codegym.controller;


import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private IProductService productService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping("/products/list")
    public ModelAndView showListProduct(@RequestParam(name = "q")Optional<String> q) {
        ModelAndView modelAndView = new ModelAndView("/product/list");
        List<Product> products = productService.findAll();
        if (q.isPresent()){
            products = productService.findProductByNameContaining(q.get());
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/delete/{id}")
    public ModelAndView showDeleteProduct(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        Product product = productService.findById(id);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/products/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        File file = new File(uploadPath + product.getImage());
        if (file.exists()){
            file.delete();
        }
        productService.removeById(id);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/create")
    public ModelAndView showCreateProduct() {
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new ProductForm());//Gửi 1 đối tượng product rỗng sang file view để tạo mới
        return modelAndView;
    }

    @PostMapping("/products/create")
    public ModelAndView createProduct(@ModelAttribute ProductForm productForm) {
        String fileName = productForm.getImage().getOriginalFilename();
        long currentTime = System.currentTimeMillis(); //Xử lý lấy thời gian hiện tại
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(), productForm.getDescription(), fileName);
        productService.save(product);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        Product product = productService.findById(id);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/products/edit/{id}")
    public ModelAndView editProduct(@PathVariable Long id, @ModelAttribute ProductForm productForm) {
        MultipartFile img = productForm.getImage();
        Product oldProduct = productService.findById(id);
        File file = new File(uploadPath + oldProduct.getImage());
        if (file.exists()){
            file.delete();
        }
        if (img.getSize() != 0) {
            String fileName = productForm.getImage().getOriginalFilename();
            long currentTime = System.currentTimeMillis(); //Xử lý lấy thời gian hiện tại
            fileName = currentTime + fileName;
            oldProduct.setImage(fileName);
            try {
                FileCopyUtils.copy(img.getBytes(), new File(uploadPath + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        oldProduct.setPrice(productForm.getPrice());
        oldProduct.setDescription(productForm.getDescription());
        oldProduct.setName(productForm.getName());
        productService.save(oldProduct);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/{id}")
    public ModelAndView showProductDetail(@PathVariable Long id) {
        Product product = productService.findById(id);
        return new ModelAndView("/product/view", "product", product);
    }
}