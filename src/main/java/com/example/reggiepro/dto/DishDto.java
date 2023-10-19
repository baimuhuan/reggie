package com.example.reggiepro.dto;

import com.example.reggiepro.entity.Dish;
import com.example.reggiepro.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
