package com.example.demo.config;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/6.
 */
public class FreemarkerUtils {
    private static Configuration configuration;
    //获取Freemarker的Configuration对象
    private static Configuration bulitConfiguration() {
        if(configuration==null){
            configuration=new Configuration(Configuration.VERSION_2_3_25);
            String path=FreemarkerUtils.class.getResource("/").getPath();
            File file=new File(path+File.separator+"templates");
            try {
                configuration.setDirectoryForTemplateLoading(file);
                configuration.setDefaultEncoding("utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return configuration;
    }

    /**
     *
     * @param response:相应对象
     * @param targetView：目标页的名字
     * @param vmap：传给目标页得键值对
     */
    public static void forward(
            HttpServletResponse response, String targetView,
            Map<String,Object> vmap){
        try {
            Template temp=bulitConfiguration().getTemplate(targetView);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            PrintWriter out=response.getWriter();

            temp.process(vmap,out);

            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
