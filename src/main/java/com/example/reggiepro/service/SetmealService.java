package com.example.reggiepro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.reggiepro.dto.SetmealDto;
import com.example.reggiepro.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
