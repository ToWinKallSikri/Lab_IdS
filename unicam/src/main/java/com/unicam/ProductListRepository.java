package com.unicam;

import org.springframework.data.repository.CrudRepository;

public interface ProductListRepository extends CrudRepository<Product, String> {
}
