package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.FreemarkerUtils;

import com.example.demo.po.Article;
import com.example.demo.po.Bbsuser;
import com.example.demo.service.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/13.
 */
@WebServlet(name = "/a1", urlPatterns = {"/article"},
        initParams = {
                @WebInitParam(name = "show", value = "show.ftl"),
                @WebInitParam(name = "welcome",value = "/welcome"),
                @WebInitParam(name = "showreply", value = "/article?action=queryid&id="),
        })
public class ArticleController extends HttpServlet {
    @Autowired
    private ArticleServiceImpl service;

    private Map<String, String> map = new HashMap<String, String>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "queryall":

                String curpage=req.getParameter("cur");
                HashMap vmap = new HashMap<String, Object>();

                int pagesize=0;
                HashMap vmp=(HashMap)req.getAttribute("vmp");


                Bbsuser user = (Bbsuser) req.getSession().getAttribute("user");
                if(user!=null){//登录成功
                    pagesize=user.getPagenum();
                    vmap.put("msg", "恭喜" + user.getUsername() + "，登录成功！");
                    vmap.put("user", user);

                }else{//游客
                    pagesize=5;
                }
                if(vmp!=null){//注册的信息
                    vmap.put("msg",vmp.get("msg").toString());
                }


                Sort sort=new Sort(Sort.Direction.DESC,"id");
                //limit (当前页码-1)*页面容量 , 页面容量
                Pageable pb=new PageRequest(Integer.parseInt(curpage),pagesize,sort);
                Page<Article> p= service.findAll(pb , 0);
                vmap.put("page", p);

                FreemarkerUtils.forward(resp,map.get("show"),vmap);
                break;
            case "del"://主贴
                String id=req.getParameter("id");

                service.delete(Integer.parseInt(id));
                RequestDispatcher dispatcher=req.getRequestDispatcher(map.get("welcome"));
                dispatcher.forward(req,resp);
                break;
            case "delc"://从贴
                id=req.getParameter("id");//帖子的id
                String rootid=req.getParameter("rootid");
                service.deletec(Integer.parseInt(id),Integer.parseInt(rootid));
                dispatcher=req.getRequestDispatcher(map.get("showreply")+rootid);

                dispatcher.forward(req,resp);

                break;
            case "queryid":
                String rid=req.getParameter("id");
                Map<String,Object> map1=service.findArticleByid(Integer.parseInt(rid));

                resp.setCharacterEncoding("utf-8");
                resp.setContentType("text/html");
                PrintWriter out=resp.getWriter();
                String json=JSON.toJSONString(map1,true);
                out.print(json);
                out.flush();
                out.close();
                break;
            case "add":
                Article a=new Article();
                a.setRootid(0);
                a.setTitle(req.getParameter("title"));
                a.setContent(req.getParameter("content"));
                a.setDatetime(new Date(System.currentTimeMillis()));
                Bbsuser buser=(Bbsuser)req.getSession().getAttribute("user");

                a.setUser(buser);
                service.save(a);//增加主贴

                dispatcher=req.getRequestDispatcher(map.get("welcome"));
                dispatcher.forward(req,resp);

                break;
            case "reply":
                a=new Article();
                rootid=req.getParameter("rootid");
                a.setRootid(Integer.parseInt(rootid));
                a.setTitle(req.getParameter("title"));
                a.setContent(req.getParameter("content"));
                a.setDatetime(new Date(System.currentTimeMillis()));
                buser=(Bbsuser)req.getSession().getAttribute("user");

                a.setUser(buser);
                service.save(a);//增加主贴

                dispatcher=req.getRequestDispatcher(map.get("showreply")+rootid);

                dispatcher.forward(req,resp);

                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("show", config.getInitParameter("show"));
        map.put("welcome", config.getInitParameter("welcome"));
        map.put("showreply", config.getInitParameter("showreply"));
    }
}
