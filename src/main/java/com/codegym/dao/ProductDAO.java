package com.codegym.dao;

import com.codegym.model.Product;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static javax.swing.text.html.HTML.Tag.SELECT;


@Component
@Transactional//Để khai báo đang sử dụng transaction
public class ProductDAO implements IProductDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        //HQL => TypedQuery => truy vấn sử dụng hibernate
        //SQL => NativeQuery => truy vấn thuần
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product as p", Product.class);
        return query.getResultList();
    }

    @Override
    public Product findById(Long id) {
        TypedQuery<Product> query = entityManager.createQuery("select p from Product as p where p.id = ?1", Product.class);
        query.setParameter(1, id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product); //persist tạo mới
        } else {
            entityManager.merge(product); //merge là cập nhật
        }
        return product;
    }

    @Override
    public void removeById(Long id) {
        Product product = findById(id);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    @Override
    public List<Product> findProductByNameContaining(String name) {
        TypedQuery<Product> query = entityManager.createQuery("select p from Product  as p where p.name like ?1", Product.class);
        query.setParameter(1, name);
        return query.getResultList();
    }
}