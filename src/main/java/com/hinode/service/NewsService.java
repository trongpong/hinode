package com.hinode.service;

import com.hinode.dao.INewsDao;
import com.hinode.entity.New;
import com.hinode.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private INewsDao iNewsDao;

    public List<New> getAllNew() {
        return iNewsDao.findAll();
    }

    public New getById(int id) {
        return iNewsDao.getOne(id);
    }

    public New add(New newpage) {
        return iNewsDao.save(newpage);
    }

    public void delete(int id) {
        iNewsDao.delete(getById(id));
    }

    public List<New> getNewtUpdatedAt() {
        return iNewsDao.getNewtUpdatedAt();
    }
}

