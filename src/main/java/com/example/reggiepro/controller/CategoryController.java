package com.example.reggiepro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggiepro.common.R;
import com.example.reggiepro.entity.Category;
import com.example.reggiepro.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<Category>> page(Integer page, Integer pageSize){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByDesc(Category::getUpdateTime);
        Page<Category> categoryPage = new Page<>(page, pageSize);
        categoryService.page(categoryPage,categoryLambdaQueryWrapper);
        return R.success(categoryPage);
    }

    @PostMapping
    public R<String> add(@RequestBody  Category category){
        log.info(String.valueOf(category));
        categoryService.save(category);
        return R.success("添加菜品分类成功");
    }

    @DeleteMapping()
    public R<String> delete(Long ids){
        categoryService.removeById(ids);
        return R.success("删除分类成功");
    }

    @PutMapping
    public R<String> alter(@RequestBody Category category){
        log.info("当前要修改的分类信息{}",category.toString());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(@RequestParam(value = "type",required = false) Integer type){
        log.info("所选类型为{}",type);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(type!=null,Category::getType,type);
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }

//    @GetMapping("/list")
//    public R<List<Category>> list(Category category){
//        log.info("所选类型为{}",category.getType());
//        //条件构造器
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        //添加条件
//        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
//        //添加排序条件
//        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
//
//        List<Category> list = categoryService.list(queryWrapper);
//        return R.success(list);
//    }
}
