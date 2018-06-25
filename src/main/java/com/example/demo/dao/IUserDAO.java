package com.example.demo.dao;

import com.example.demo.po.Bbsuser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Administrator on 2017/5/6.
 */
public interface IUserDAO extends CrudRepository<Bbsuser,Integer> {
    @Query("select c from Bbsuser c where username=:u and password=:p")
    Bbsuser login(@Param("u") String username, @Param("p") String password);

    Bbsuser save(Bbsuser user);

    @Query("select c from Bbsuser c where userid=:id")
    Bbsuser getPic(@Param("id") Integer id);

    Iterable<Bbsuser> findAll();


}
