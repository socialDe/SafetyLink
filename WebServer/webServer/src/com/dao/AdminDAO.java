package com.dao;

import org.springframework.stereotype.Repository;

import com.frame.Dao;
import com.vo.AdminVO;

@Repository("adao")
public interface AdminDAO extends Dao<String, AdminVO> {

}
