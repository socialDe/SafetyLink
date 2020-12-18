package com.biz;

import org.springframework.stereotype.Service;

import com.vo.CarVO;
@Service
public class FcmLogBiz implements FcmLog<CarVO> {

	@Override
	public void fcmlog(CarVO v) {
		System.out.println("FcmLog:"+v);
	}

}
