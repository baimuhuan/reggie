package com.example.reggiepro.dto;

import com.example.reggiepro.entity.Setmeal;
import com.example.reggiepro.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
