package com.codegym.repository;

import com.codegym.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {
    Iterable<Product> findByNameContaining(String name);

    @Query(value = "select * from " +
            "products where (price between ?1 and ?2) " +
            "and image is not null", nativeQuery = true)
    Iterable<Product> findProductPriceBetween(Double min, Double max);
}
