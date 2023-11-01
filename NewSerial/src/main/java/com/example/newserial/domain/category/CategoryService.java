package com.example.newserial.domain.category;

import org.springframework.beans.factory.annotation.Autowired;

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    void mockData() {
        Category c1 = new Category();
        c1.setName("정치");
    }
}
