package com.biz;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.vo.AdminVO;
import com.vo.UsersVO;

@Service("abiz")
public class AdminBIZ implements Biz<String, AdminVO> {

	@Resource(name="adao")
	Dao<String, AdminVO> dao;
	
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
		if(result ==0) {
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

}
