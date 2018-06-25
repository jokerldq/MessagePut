package com.example.demo.dao;



import com.example.demo.po.Article;
import com.example.demo.po.Bbsuser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/5/14.
 */
@Repository
public class ArticleDAOImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public Map<String,Object> findArticleByid(Integer id){
        Map<String,Object> map=new HashMap<String,Object>();
        StoredProcedureQuery sp=entityManager.createStoredProcedureQuery("p_3");
        sp.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        sp.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);

        sp.setParameter(1,id);

        sp.execute();//执行sql

        List<Object[]> list=sp.getResultList();
        List<Article> articleList=new ArrayList<Article>();
        for( Object []o :list){
            Article a=new Article();

            a.setId(Integer.parseInt(o[0].toString()));
            a.setRootid(Integer.parseInt(o[1].toString()));
            a.setTitle(o[2].toString());
            a.setContent(o[3].toString());
            Bbsuser user=new Bbsuser();
            user.setUserid(Integer.parseInt(o[4].toString()));
            a.setUser(user);
            try {
                Date d=new SimpleDateFormat("yyyy-MM-dd").parse(o[5].toString());
                a.setDatetime(new java.sql.Date(d.getTime()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            articleList.add(a);

        }



        map.put("list",articleList);
        map.put("title",sp.getOutputParameterValue(2));


        return map;
    }


}


