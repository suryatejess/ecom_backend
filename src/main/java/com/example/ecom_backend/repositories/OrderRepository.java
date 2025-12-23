package com.example.ecom_backend.repositories;

import com.example.ecom_backend.entities.Cart;
import com.example.ecom_backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order,Integer> {
}
