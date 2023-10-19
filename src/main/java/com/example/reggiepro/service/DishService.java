package com.example.reggiepro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggiepro.dto.DishDto;
import com.example.reggiepro.entity.Dish;

public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
