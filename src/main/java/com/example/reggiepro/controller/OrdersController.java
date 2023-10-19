package com.example.reggiepro.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggiepro.common.BaseContext;
import com.example.reggiepro.common.R;
import com.example.reggiepro.entity.Orders;
import com.example.reggiepro.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("order")
public class OrdersController {
    @Autowired
    OrdersService ordersService;
    @GetMapping("/userPage")
    public R<Page<Orders>> userPage(Integer page, Integer pageSize){
        Long userId = BaseContext.getId();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<Orders>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,userId);
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);
        return R.success(ordersPage);
    }

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
//        ordersService.submit(orders);
        return R.success("下单成功");
    }

}
