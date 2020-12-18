package com.log;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;

import com.vo.CarSensorVO;
import com.vo.CarVO;
import com.vo.UsersVO;
@Service
@Aspect
public class Loggers {
//	private Logger car_log = 
//			Logger.getLogger("car"); 
//	private Logger work_log = 
//			Logger.getLogger("user"); 
//	private Logger data_log = 
//			Logger.getLogger("data"); 
	private Logger position_log = 
			Logger.getLogger("position"); 
	private Logger fcm_log = 
			Logger.getLogger("fcm"); 
	
//	
//	
//	
//	// before
//	@Before("execution(* com.biz.CarBiz.*(..))")
//	public void logging3(JoinPoint jp) {
//		Object[] args = jp.getArgs();
//		CarVO cstatus= (CarVO)args[0];
//		car_log.debug(jp.getSignature().getName()+",");
//		System.out.println(jp.getArgs());
//	}
//
//	
//	// before
//	@Before("execution(* com.*.MainController.getTabletSensor(..))")
//	public void logging1(JoinPoint jp) {
//		Object[] args = jp.getArgs();
//		UsersVO user= (UsersVO)args[0];
//		CarVO car = (CarVO)args[0];
//		CarSensorVO carsen = (CarSensorVO)args[0];
//		position_log.debug(jp.getSignature().getName()+","+"ABC");
//		System.out.println("position log:"+jp.getArgs());
//	}
	
	// before
	@Before("execution(* com.biz.FcmLogBiz.*(..))")
	public void logging2(JoinPoint jp) {
		Object[] args = jp.getArgs();
		CarVO car = (CarVO)args[0];
		fcm_log.debug(car.getUserid()+","+car.getCarid()+","+car.getCarnum()+","+car.getFcmType());
	}

	
}





