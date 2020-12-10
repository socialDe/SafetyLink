package com.frame;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import com.vo.CarSensorVO;
import com.vo.CarVO;

public interface Biz<K1,K2,V> {
	@Transactional //트랙젝션 처리 @Transactional을 사용한다 -  실행이 이상하면 롤백한다
	public void register(V v) throws Exception;
	@Transactional
	public void remove(K1 k) throws Exception;
	@Transactional
	public void modify(V v) throws Exception;
	public V get(K1 k) throws Exception;
	public ArrayList<V> get() throws Exception;

	public ArrayList<V> getcarsfromuser(K2 k) throws Exception;
	public V getFromKeys(K1 k1, K2 k2) throws Exception;
	public V caridfromnumber(String k) throws Exception;
	public ArrayList<V> getdrivingcars(K2 k) throws Exception;
}
