package com.example.reggiepro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggiepro.common.R;
import com.example.reggiepro.entity.Employee;
import com.example.reggiepro.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        String s = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);

        if (one==null){
            return R.error("登录失败");
        }else if (!one.getPassword().equals(s)){
            return R.error("密码错误");
        }else if (one.getStatus()==0){
            return R.error("账号已被禁用");
        }
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @RequestMapping("/page")
    public R<Page<Employee>> page(Integer page, Integer pageSize,String name){
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(StringUtils.isNotEmpty(name),Employee::getName,name);
        employeeLambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        Page<Employee> employeePage = new Page<>(page,pageSize);
        employeeService.page(employeePage, employeeLambdaQueryWrapper);
        return R.success(employeePage);
    }

    @PostMapping
    public R<String> sava(@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request){
        log.info(employee.toString());
        employeeService.updateById(employee);
        return  R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee ep = employeeService.getById(id);
        if (ep!=null){
            return R.success(ep);
        }
        return R.error("没有找到相关的员工信息");

    }
}
