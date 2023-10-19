package com.example.reggiepro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggiepro.entity.Orders;
import com.example.reggiepro.mapper.OrdersMapper;
import com.example.reggiepro.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
