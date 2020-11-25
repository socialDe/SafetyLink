package com.dao;

import org.springframework.stereotype.Repository;

import com.frame.Dao;
import com.vo.UsersVO;

@Repository("udao")
public interface UsersDAO extends Dao<String, UsersVO> {

}
