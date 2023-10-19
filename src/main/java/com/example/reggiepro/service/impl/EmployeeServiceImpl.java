package com.example.reggiepro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggiepro.entity.Employee;
import com.example.reggiepro.mapper.EmployeeMapper;
import com.example.reggiepro.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
