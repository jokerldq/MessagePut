package com.example.demo.service;

import com.example.demo.dao.IUserDAO;
import com.example.demo.po.Bbsuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/5/6.
 */
@Service
@Transactional
public class UserServiceImpl {

    private Map<String,String> types=new HashMap<String,String>();
    public UserServiceImpl(){
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }
    @Autowired
    private IUserDAO dao;

    public Bbsuser upload(HttpServletRequest req){//上传
        //让该组件作用到全部的application中

        CommonsMultipartResolver commonsMultipartResolver=
                new CommonsMultipartResolver(req.getSession().getServletContext());
        //图片名字可以有中文
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        //设置spring‘延时加载，防止出现上传大文件而提前解析
        commonsMultipartResolver.setResolveLazily(true);
        //设置缓存
        commonsMultipartResolver.setMaxInMemorySize(4096*1024);
        //设置每个文件大小
        commonsMultipartResolver.setMaxUploadSizePerFile(1024*1024);
        //设置整个文件的和大小
        commonsMultipartResolver.setMaxUploadSize(1024*1024*5);

        //把req转换成流的request
        MultipartHttpServletRequest mreq=commonsMultipartResolver.resolveMultipart(req);
        MultipartFile mfile =mreq.getFile("file0");

        String filename=mfile.getOriginalFilename();
        //找文件类型
        String filepath="upload"+File.separator+filename;
        File file=new File(filepath);
        try {
            mfile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bbsuser user=new Bbsuser();
        user.setUsername(mreq.getParameter("reusername"));
        user.setPassword(mreq.getParameter("repassword"));
        user.setPagenum(10);
        user.setPicPath(filepath);
        //往pic送字节
        try(InputStream fis=new FileInputStream(file)) {
            byte[] buffer=new byte[fis.available()];
            fis.read(buffer);
            user.setPic(buffer);
    } catch (Exception e) {
        e.printStackTrace();
    }
        return user;

    }

    public Bbsuser save(Bbsuser bbsuser){
        return dao.save(bbsuser);
    }

    public Bbsuser login(Bbsuser bbsuser){
        return dao.login(bbsuser.getUsername(),bbsuser.getPassword());
    }

    public Bbsuser getPic(@Param("id") String id){
        return dao.getPic(Integer.parseInt(id));
    }
}
