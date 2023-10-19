package com.example.reggiepro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggiepro.common.CustomerException;
import com.example.reggiepro.entity.Category;
import com.example.reggiepro.entity.Dish;
import com.example.reggiepro.entity.Setmeal;
import com.example.reggiepro.mapper.CategoryMapper;
import com.example.reggiepro.service.CategoryService;
import com.example.reggiepro.service.DishService;
import com.example.reggiepro.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;
    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        long dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount>0){
            throw new CustomerException("此菜品分类下还含有菜品");
        }


        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setmealCount>0){
            throw new CustomerException("此菜品分类下还含有套餐");
        }
        return super.removeById(id);
    }
}
