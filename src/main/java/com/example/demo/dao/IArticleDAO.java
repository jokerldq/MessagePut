package com.example.demo.dao;

import com.example.demo.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Administrator on 2017/5/13.
 */
public interface IArticleDAO extends CrudRepository<Article,Integer>{
    @Query("select c from Article c where rootid=:rid")
    Page<Article> findAll(Pageable pageable, @Param("rid") Integer rid);

    @Modifying
    @Query("delete from Article where id=:id or rootid=:rid")
    public int delete(@Param("id") Integer id, @Param("rid") Integer rid);

    @Modifying
    @Query("delete from Article where id=:id and rootid=:rid")//删除从贴
    public int deletec(@Param("id") Integer id, @Param("rid") Integer rid);


    Article  save(Article article);

}
