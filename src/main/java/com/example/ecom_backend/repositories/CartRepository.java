package com.example.ecom_backend.repositories;

import com.example.ecom_backend.entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart> findFirstByOrderByIdAsc();
}
