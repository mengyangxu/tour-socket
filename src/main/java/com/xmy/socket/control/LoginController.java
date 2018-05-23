package com.xmy.socket.control;

import com.xmy.socket.redis.RedisService;
import com.xmy.socket.utils.BusinessException;
import com.xmy.socket.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class LoginController {
    @Autowired
    private RedisService redisService;
    //@Autowired
   // private UserServcie userServcie;

    @CrossOrigin(origins = "*")
    @RequestMapping("/admin/login")
    public JsonResponse login(@RequestParam("username") String username,@RequestParam("password") String password){
        //boolean flag = UserService.login();
        if("admin".equals(username)&&"admin".equals(password)) {
            String key = UUID.randomUUID() + "";
            redisService.set(key, "login", 30);
            return new JsonResponse(key);
        }else{
            return new JsonResponse(new BusinessException(400,"用户名密码错误"));
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/admin/checkLogin")
    public JsonResponse checkLogin(@RequestParam("key") String key){
        //boolean flag = UserService.login();
        String str = redisService.get(key).toString();
        if("login".equals(str)) {
            return new JsonResponse("");
        }else {
            return new JsonResponse(new BusinessException(400,"未登录"));
        }
    }


}
