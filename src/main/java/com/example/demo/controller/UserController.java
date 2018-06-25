package com.example.demo.controller;

import com.example.demo.po.Bbsuser;
import com.example.demo.service.UserServiceImpl;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/6.
 */
@WebServlet(name = "u1",urlPatterns = {"/user"},
        initParams = {
            @WebInitParam(name = "welcome",value = "/welcome")
        })
public class UserController extends HttpServlet {
    @Autowired
    private UserServiceImpl service;


    private Map<String,String> map=new HashMap<String,String>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,Object> vmap=null;
        if(ServletFileUpload.isMultipartContent(req)){//true 流形式
            Bbsuser user=service.upload(req);
            vmap=new HashMap<String,Object>();
            user=service.save(user);
            if(user!=null){
                vmap.put("msg","恭喜你，注册成功了！");

            }
            req.setAttribute("vmp",vmap);
            RequestDispatcher dispatcher=req.getRequestDispatcher(map.get("welcome"));
            dispatcher.forward(req,resp);

        }else{//键值对形式
            String action=req.getParameter("action");
            vmap=new HashMap<String,Object>();
            Bbsuser user=null;
            switch (action){
                case "login":
                    user=login(req,resp);
//                    vmap.put("msg","恭喜"+user.getUsername()+"，登录成功！");
//                    vmap.put("user",user);
                    req.getSession().setAttribute("user",user);
                    RequestDispatcher dispatcher=req.getRequestDispatcher(map.get("welcome"));
                    dispatcher.forward(req,resp);
                   // FreemarkerUtils.forward(resp,map.get("show"),vmap);
                    break;

                case "out":
                    out(req,resp);
                    dispatcher=req.getRequestDispatcher(map.get("welcome"));
                    dispatcher.forward(req,resp);
                    break;
                case "pic":
                    pic(req,resp);
                    break;
            }


        }


    }

    private void pic(HttpServletRequest req, HttpServletResponse resp) {
        String id=req.getParameter("id");
        Bbsuser user=service.getPic(id);


        try {
            resp.setContentType("image/jpeg");
            OutputStream out=resp.getOutputStream();
            out.write(user.getPic());

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void out(HttpServletRequest req,HttpServletResponse resp) {
        req.getSession().invalidate();
       // FreemarkerUtils.forward(resp,map.get("show"),null);
    }



    private Bbsuser login(HttpServletRequest req,HttpServletResponse resp) {
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        Bbsuser bbsuser=new Bbsuser();
        bbsuser.setUsername(username);
        bbsuser.setPassword(password);
        bbsuser=service.login(bbsuser);
        if(bbsuser!=null){//成功，处理cookie
            String sun=req.getParameter("sun");
            if (sun!=null) {//点击了记住一星期

                Cookie uc=new Cookie("papaoku",username);
                uc.setMaxAge(3600*24*7);
                resp.addCookie(uc);
                Cookie pc=new Cookie("papaokp",password);
                pc.setMaxAge(3600*24*7);
                resp.addCookie(pc);


            }
            return  bbsuser;
        }
        return null;

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("welcome",config.getInitParameter("welcome"));
    }
}
