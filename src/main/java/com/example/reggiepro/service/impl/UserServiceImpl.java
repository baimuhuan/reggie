package com.example.reggiepro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggiepro.entity.User;
import com.example.reggiepro.mapper.UserMapper;
import com.example.reggiepro.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
