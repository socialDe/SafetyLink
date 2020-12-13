package com.biz;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.vo.CarSensorVO;

@Service("sbiz")
public class CarSensorBIZ implements Biz<Integer,String,CarSensorVO> {

	@Resource(name="sdao")
	Dao<Integer,String,CarSensorVO> dao;
	
	@Override
	public void register(CarSensorVO v) throws Exception {
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
	public void modify(CarSensorVO v) throws Exception {
		int result = 0;
		result = dao.update(v);
		if(result ==0) {
			throw new Exception();
		}	
	}

	@Override
	public CarSensorVO get(Integer k) throws Exception {
		return dao.select(k);
	}

	@Override
	public ArrayList<CarSensorVO> get() throws Exception {
		return dao.selectall();
	}

	@Override
	public ArrayList<CarSensorVO> getcarsfromuser(String k) throws Exception {
		return dao.selectcarsfromuser(k);
	}
	
	public CarSensorVO getFromKeys(Integer k1, String k2) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarSensorVO caridfromnumber(String k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CarSensorVO movingcarfromnumber(String k) throws Exception {
		return dao.movingcarfromnumber(k);
	}

}
