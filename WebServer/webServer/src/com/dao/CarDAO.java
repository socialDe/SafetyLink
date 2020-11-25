	package com.dao;

import org.springframework.stereotype.Repository;

import com.frame.Dao;
import com.vo.CarVO;

@Repository("cdao")
public interface CarDAO extends Dao<Integer, CarVO> {

}
