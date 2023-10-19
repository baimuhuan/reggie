package com.example.reggiepro.controller;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggiepro.common.R;
import com.example.reggiepro.entity.User;
import com.example.reggiepro.service.UserService;
import com.example.reggiepro.utils.AliSMSUtils;
import com.example.reggiepro.utils.SMSUtils;
import com.example.reggiepro.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody  User user, HttpSession session) throws ClientException {

        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            //生成随机的四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("邹顺的瑞吉外卖","SMS_288735304",phone,code);
//            AliSMSUtils.sendSms(null,phone,"邹顺的瑞吉外卖","SMS_288735304");
            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);
            return R.success("验证码已发送，注意查收...");
        }
        return R.error("短信发送失败...");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody HashMap map, HttpSession session){
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object cd = session.getAttribute(phone);
        if (cd.equals(code)){
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user==null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success("登录成功");
        }
        return R.error("验证码错误");
    }
}
