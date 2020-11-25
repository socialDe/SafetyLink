package com.biz;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.frame.Biz;
import com.frame.Dao;
import com.vo.UsersVO;

@Service("ubiz")
public class UsersBIZ implements Biz<String, UsersVO> {

	@Resource(name="udao")
	Dao<String, UsersVO> dao;
	
	@Override
	public void register(UsersVO v) throws Exception {
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
	public void modify(UsersVO v) throws Exception {
		int result = 0;
		result = dao.update(v);
		if(result ==0) {
			throw new Exception();
		}		
	}

	@Override
	public UsersVO get(String k) throws Exception {
		return dao.select(k);
	}

	@Override
	public ArrayList<UsersVO> get() throws Exception {
		return dao.selectall();
	}

}
