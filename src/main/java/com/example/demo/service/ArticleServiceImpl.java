package com.example.demo.service;

import com.example.demo.po.Article;
import com.example.demo.dao.ArticleDAOImpl;
import com.example.demo.dao.IArticleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/13.
 */
@Service
@Transactional
public class ArticleServiceImpl {
    @Autowired
    private IArticleDAO dao;

    @Autowired
    private ArticleDAOImpl adao;

    public Page<Article> findAll(Pageable pageable, Integer rid){
        return dao.findAll(pageable,rid);
    }

    public void delete(Integer id ){
        dao.delete(id,id);
    }
    public void deletec(Integer id,Integer rootid ){
        dao.deletec(id,rootid);
    }
    public Map<String,Object> findArticleByid(Integer id){
        return adao.findArticleByid(id);
    }

    public void  save(Article article){
        dao.save(article);
    }
}
