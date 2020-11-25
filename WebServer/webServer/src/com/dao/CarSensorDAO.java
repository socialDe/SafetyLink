package com.dao;

import org.springframework.stereotype.Repository;

import com.frame.Dao;
import com.vo.CarSensorVO;

@Repository("sdao")
public interface CarSensorDAO extends Dao<Integer, CarSensorVO> {

}
