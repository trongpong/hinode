package com.hinode.service;

import com.hinode.dao.INewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    private INewsDao iNewsDao;
}

