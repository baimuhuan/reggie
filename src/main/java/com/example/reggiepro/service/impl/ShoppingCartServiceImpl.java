package com.example.reggiepro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggiepro.entity.ShoppingCart;
import com.example.reggiepro.mapper.ShoppingCartMapper;
import com.example.reggiepro.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
