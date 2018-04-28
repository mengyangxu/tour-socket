package com.xmy.socket.socket;


import com.xmy.portal.utils.JsonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("socket")
public class ChartWebsocketController {

    @RequestMapping("chat")
    public String chat(String username, HttpServletRequest request, HttpServletResponse response){
        HttpSession session=request.getSession();
        session.setAttribute("username", username);
        //WebSocketTest.setHttpSession(session);
        return "chat";
    }

    @RequestMapping("login")
    public String login() throws Exception{

        return ("chatlogin");
    }

    @RequestMapping("loginOut")
    @ResponseBody
    public JsonResponse loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session=request.getSession();
        session.removeAttribute("username");  
        return new JsonResponse("");
    }  
}  