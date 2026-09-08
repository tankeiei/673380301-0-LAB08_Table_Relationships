package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. หน้าแสดงรายการสินค้า
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    // 2. หน้าฟอร์มเพิ่มสินค้าใหม่
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product = new Product();
        // เตรียม Review เปล่าไว้ 1 รายการสำหรับฟอร์ม add.html ที่ binding reviews[0]
        product.getReviews().add(new Review());
        model.addAttribute("product", product);
        return "products/add";
    }

    // 3. บันทึกข้อมูลเพิ่มสินค้า
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "เพิ่มสินค้าเรียบร้อยแล้ว!");
        return "redirect:/products";
    }

    // 4. หน้าฟอร์มแก้ไขสินค้า
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id: " + id));
        model.addAttribute("product", product);
        return "products/edit";
    }

    // 5. บันทึกการแก้ไข
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        productService.updateProduct(id, product);
        redirectAttributes.addFlashAttribute("message", "แก้ไขข้อมูลสินค้าสำเร็จ!");
        return "redirect:/products";
    }

    // 6. หน้ายืนยันการลบ
    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id: " + id));
        model.addAttribute("product", product);
        return "products/delete";
    }

    // 7. ดำเนินการลบสินค้า
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "ลบสินค้าสำเร็จ!");
        return "redirect:/products";
    }
}
