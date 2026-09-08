package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.strategy.DiscountContext;
import com.example.demo.strategy.DiscountStrategy;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // ใช้ Constructor Injection ตามหลัก Dependency Inversion Principle (DIP)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Read ทั้งหมด พร้อมคำนวณราคาสุทธิหลังหักส่วนลดด้วย Strategy Pattern
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            DiscountStrategy strategy = DiscountContext.getStrategy(product.getDiscountType());
            double discounted = strategy.calculateDiscountedPrice(product.getPrice());
            product.setDiscountedPrice(discounted);
        }
        return products;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Save (Create) สินค้า พร้อมดูแลความสัมพันธ์ของ ProductDetail และ Review
    public Product saveProduct(Product product) {
        if (product.getDetail() != null) {
            product.getDetail().setProduct(product);
        }
        if (product.getReviews() != null) {
            // กรอง review ที่ผู้ใช้ไม่ได้กรอกชื่อผู้รีวิวออก
            product.getReviews().removeIf(r -> r.getReviewer() == null || r.getReviewer().trim().isEmpty());
            for (Review r : product.getReviews()) {
                r.setProduct(product);
            }
        }
        return productRepository.save(product);
    }

    // Update สินค้า
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(updatedProduct.getName());
            existing.setCategory(updatedProduct.getCategory());
            existing.setBrand(updatedProduct.getBrand());
            existing.setStock(updatedProduct.getStock());
            existing.setPrice(updatedProduct.getPrice());
            existing.setDiscountType(updatedProduct.getDiscountType());

            // อัปเดตข้อมูล ProductDetail
            if (updatedProduct.getDetail() != null) {
                if (existing.getDetail() == null) {
                    existing.setDetail(updatedProduct.getDetail());
                } else {
                    existing.getDetail().setDescription(updatedProduct.getDetail().getDescription());
                    existing.getDetail().setWarranty(updatedProduct.getDetail().getWarranty());
                    existing.getDetail().setWeight(updatedProduct.getDetail().getWeight());
                    existing.getDetail().setDimensions(updatedProduct.getDetail().getDimensions());
                    existing.getDetail().setManufacturedCountry(updatedProduct.getDetail().getManufacturedCountry());
                }
            }
            return productRepository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    }

    // Delete สินค้า
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
