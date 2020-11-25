package com.biz;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.vo.CarVO;

@Service("cbiz")
public class CarBIZ implements Biz<Integer, CarVO> {

	@Resource(name="cdao")
	Dao<Integer, CarVO> dao;
	
	@Override
	public void register(CarVO v) throws Exception {
		dao.insert(v);
	}

	@Override
	public void remove(Integer k) throws Exception {
		int result = 0;
		result = dao.delete(k);
		if(result == 0) {
			throw new Exception();
		}
	}

	@Override
	public void modify(CarVO v) throws Exception {
		int result = 0;
		result = dao.update(v);
		if(result ==0) {
			throw new Exception();
		}	
	}

	@Override
	public CarVO get(Integer k) throws Exception {
		return dao.select(k);
	}

	@Override
	public ArrayList<CarVO> get() throws Exception {
		return dao.selectall();
	}

}
