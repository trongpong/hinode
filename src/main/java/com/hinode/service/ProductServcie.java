package com.hinode.service;

import com.hinode.dao.IProductDAO;
import com.hinode.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServcie {
    @Autowired
    private IProductDAO iProductDAO;

    public List<Product> getAllProduct() {
        return iProductDAO.findAll();
    }
    public Page<Product> findAllPagination(int page, int size, Sort sort) {
        return iProductDAO.findAll(PageRequest.of(page, size, sort));
    }

    public Product getById(int id) {
        return iProductDAO.getOne(id);
    }


    public void delete(int id) {
        iProductDAO.delete(getById(id));
    }

    public Product add(Product product) {
        return iProductDAO.save(product);
    }

    public List<Product> getProductUpdatedAt() {
        return iProductDAO.getProductUpdatedAt();
    }
}
