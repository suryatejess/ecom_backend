package com.example.ecom_backend.services;

import com.example.ecom_backend.entities.Product;
import com.example.ecom_backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository repo;

    public List<Product> findAll() {
        return repo.findAll();
    }

    public void save(Product product) {
        repo.save(product);
    }

    public Product findById(Long id) {
        return repo.findById(id).get();
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }


}
