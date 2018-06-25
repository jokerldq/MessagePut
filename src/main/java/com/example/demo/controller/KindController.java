package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/5/14.
 */
@WebServlet(name="/file",urlPatterns = {"/kindupload"})
public class KindController extends HttpServlet {

    private Map<String,String> types=new HashMap<String,String>();

    public KindController(){
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s =uploadPic(req);
        PrintWriter out=resp.getWriter();

        out.print(s);
        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    public String uploadPic(HttpServletRequest req){
        CommonsMultipartResolver commonsMultipartResolver
                =new CommonsMultipartResolver(req.getServletContext());
        if(commonsMultipartResolver.isMultipart(req)){//IO的形式上传

            FileItemFactory ff=new DiskFileItemFactory();

            ServletFileUpload upload=new ServletFileUpload(ff);
            upload.setHeaderEncoding("utf-8");

            try {
                FileItemIterator fileItemIterator=upload.getItemIterator(req);

                while(fileItemIterator.hasNext()){

                    FileItemStream fis=fileItemIterator.next();//每个文件域上传的文件

                    String fieldname=fis.getFieldName();
                    InputStream is=fis.openStream();

                    if(!fis.isFormField()&&fis.getName().length()>0){//已经找到了上传文件
                        String contenttype=fis.getContentType();
                        if(!types.containsKey(contenttype)){
                            return "文件格式错误，请重新上传";
                        }
                        //形成上传到服务器的绝对路径
                        String path=this.getClass().getResource("/").getPath();

                        String id= UUID.randomUUID().toString();//全球唯一编码
                        //得到上传文件目录
                        String dir=req.getParameter("dir");
                        //组成一个新的通过富文本编辑器上传的图片名字
                        String filename=path+ "static/editor/upload/" +dir+"/"+ id+types.get(contenttype);


                        BufferedInputStream bis=new BufferedInputStream(is);


                        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(new File(filename)));
                        //形成在编辑器页面显示的url地址 localhost://XXX
                        String tpath=req.getRequestURL().toString() ;
                        tpath=tpath.substring(0,tpath.lastIndexOf("/"));
                        String path1=tpath+"/editor/upload/"+dir+"/";//最终显示在编辑器中图片路径



                        Streams.copy(bis, bos, true);
                        JSONObject obj = new JSONObject();
                        obj.put("error", 0);//无错误
                        obj.put("url", path1+ id+types.get(contenttype));//使用json格式把上传文件信息传递到前端

                        return obj.toJSONString();
                    }

                }

            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
