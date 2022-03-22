package com.codegym.controller;


import com.codegym.model.Product;
import com.codegym.model.ProductForm;
import com.codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private IProductService productService;

    @Value("${file-upload}")
    private String uploadPath;

    @GetMapping("/products/list")
    public ModelAndView showListProduct() {
        ModelAndView modelAndView = new ModelAndView("/product/list");
        List<Product> products = productService.findAll();
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/products/create")
    public ModelAndView showCreateProduct(){
        ModelAndView modelAndView = new ModelAndView("/product/create");
        modelAndView.addObject("product", new ProductForm());
        return modelAndView;
    }
    @PostMapping("/products/create")
    public ModelAndView createProduct(@ModelAttribute ProductForm productForm){
        String fileName = productForm.getImage().getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        fileName = currentTime + fileName;
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(),productForm.getDescription(), fileName);
        productService.create(product);
        return  new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/delete/{id}")
    public ModelAndView showDeleteProduct(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("/product/delete");
        Product product = productService.findById(id);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/products/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable Integer id) {
        productService.removeById(id);
        return new ModelAndView("redirect:/products/list");
    }

    @GetMapping("/products/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        Product product = productService.findById(id);
        ProductForm productForm = new ProductForm(product.getId(), product.getName(), product.getPrice(), product.getDescription(), null);
        modelAndView.addObject("product", product);
        modelAndView.addObject("productForm", productForm);
        return modelAndView;
    }

    @PostMapping("/products/edit/{id}")
    public ModelAndView editProduct(@PathVariable Integer id, @ModelAttribute ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();

        if(multipartFile.getSize() == 0){
            Product productIntact = productService.findById(id);
            Product product = new Product(productForm.getId(),productForm.getName(),productForm.getPrice(),productForm.getDescription(),productIntact.getImage());
            productService.updateById(id,product);
        } else{
            String filename2 = productForm.getImage().getOriginalFilename();
            long currentTime = System.currentTimeMillis();
            filename2 = currentTime + filename2;
            try {
                FileCopyUtils.copy(productForm.getImage().getBytes(), new File(uploadPath + filename2));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Product product = new Product(productForm.getId(),productForm.getName(),productForm.getPrice(),productForm.getDescription(),filename2);
            productService.updateById(id, product);
        }

        return new ModelAndView("redirect:/products/list");

    }

}
