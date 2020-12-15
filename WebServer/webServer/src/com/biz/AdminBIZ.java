package com.biz;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.vo.AdminVO;
import com.vo.UsersVO;

@Service("abiz")
public class AdminBIZ implements Biz<String,Integer,AdminVO> {

	@Resource(name="adao")
	Dao<String,Integer,AdminVO> dao;
	
	@Override
	public void register(AdminVO v) throws Exception {
		dao.insert(v);
	}

	@Override
	public void remove(String k) throws Exception {
		int result = 0;
		result = dao.delete(k);
		if(result == 0) {
			throw new Exception();
		}	
	}

	@Override
	public void modify(AdminVO v) throws Exception {
		int result = 0;
		result = dao.update(v);
		if(result == 0) {
			throw new Exception();
		}	
	}

	@Override
	public AdminVO get(String k) throws Exception {
		return dao.select(k);
	}

	@Override
	public ArrayList<AdminVO> get() throws Exception {
		return dao.selectall();
	}

	// 안씀
	@Override
	public ArrayList<AdminVO> getcarsfromuser(Integer k) throws Exception {
		return null;
	}

	@Override
	public AdminVO getFromKeys(String k1, Integer k2) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminVO carfromnumber(String k) throws Exception {
		return null;
	
	}

	public ArrayList<AdminVO> getdrivingcars(Integer k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminVO movingcarfromnumber(String k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
