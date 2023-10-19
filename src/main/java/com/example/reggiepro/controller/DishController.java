package com.example.reggiepro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggiepro.common.CustomerException;
import com.example.reggiepro.common.R;
import com.example.reggiepro.dto.DishDto;
import com.example.reggiepro.entity.*;
import com.example.reggiepro.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    SetmealService setmealService;
    @Autowired
    DishFlavorService dishFlavorService;

    @RequestMapping("/page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        Page<Dish> dishPage = new Page<>(page,pageSize);
        dishService.page(dishPage,dishLambdaQueryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> dishes = dishPage.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
        for (Dish dish:dishes){
            Long categoryId = dish.getCategoryId();
            DishDto dishDto = new DishDto();
            String categoryName = categoryService.getById(categoryId).getName();
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setCategoryName(categoryName);
            dishDtos.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtos);
        log.info("当前页的信息为{}",dishDtoPage);
        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        log.info("添加菜品信息{}",dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }

//    @DeleteMapping
//    public R<String> delete(String ids){
//        log.info("需要删除菜品的id为{}",ids);
//        String[] s = ids.split(",");
//        log.info(Arrays.toString(s));
//        if (s.length>0){
//            for (String id:s) {
//                dishService.removeById(id);
//            }
//            return R.success("删除菜品成功");
//        }
//        return R.error("删除菜品失败");
//    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("需要删除菜品的id为{}",ids);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId,ids);
        long setmealCount = setmealDishService.count(setmealDishLambdaQueryWrapper);
        if (setmealCount>0){
            throw new CustomerException("请先把含有当前菜品的套餐删除才可进行删除操作");
        }
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId,ids);
        dishLambdaQueryWrapper.eq(Dish::getStatus,"1");
        long count = dishService.count(dishLambdaQueryWrapper);
        if (count>0){
            throw new CustomerException("请先把当前菜品停售再进行删除操作");
        }
        dishService.removeBatchByIds(ids);
        return R.success("删除菜品成功");

    }
    @PostMapping("/status/{status}")
    public R<String> alterType(String ids, @PathVariable Integer status){
        log.info("需要修改售卖状态的id为{}",ids);
        String[] split = ids.split(",");
        log.info(Arrays.toString(split));
        if (split.length>0){
            for (String id:split) {
                Dish dish = new Dish();
                dish.setStatus(status);
                dish.setId(Long.valueOf(id));
                dishService.updateById(dish);
                if (status == 0){
                    LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    setmealDishLambdaQueryWrapper.eq(SetmealDish::getDishId,id);
                    SetmealDish one = setmealDishService.getOne(setmealDishLambdaQueryWrapper);
                    if (one!=null){
                        Long setmealId = one.getSetmealId();
                        Setmeal setmeal = new Setmeal();
                        setmeal.setId(setmealId);
                        setmeal.setStatus(status);
                        setmealService.updateById(setmeal);
                    }
                }
            }
            return  R.success("修改售卖状态信息成功");
        }
        return R.error("修改售卖状态信息失败");

    }
    @GetMapping("/{id}")
    public R<?> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> alterDish(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品信息成功");
    }

//    @GetMapping("/list")
//    public R<List<Dish>> list(Long categoryId){
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.eq(Dish::getCategoryId,categoryId);
//        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
//
//        //添加排序条件
//        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(dishLambdaQueryWrapper);
//        return R.success(list);
//    }


    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

}
