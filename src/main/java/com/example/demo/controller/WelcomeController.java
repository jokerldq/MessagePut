package com.example.demo.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/6.
 */
@WebServlet(name = "w1",urlPatterns = {"/welcome"},
        initParams ={
         @WebInitParam(name = "index",value = "article?action=queryall&cur=0")}
        )
public class WelcomeController extends HttpServlet {
    private Map<String,String> map=new HashMap<String,String>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        RequestDispatcher requestDispatcher=req.getRequestDispatcher(map.get("index"));
        requestDispatcher.forward(req,resp);
        //FreemarkerUtils.forward(resp,map.get("index"),null);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("index",config.getInitParameter("index"));
    }
}
