package com.frame;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

public interface Biz<K1, K2, V> {
	@Transactional //트랙젝션 처리 @Transactional을 사용한다 -  실행이 이상하면 롤백한다
	public void register(V v) throws Exception;
	@Transactional
	public void remove(K1 k1) throws Exception;
	@Transactional
	public void modify(V v) throws Exception;
	public V get(K1 k1) throws Exception;
	public ArrayList<V> get() throws Exception;
	public V getFromKeys(K1 k1, K2 k2) throws Exception;
}
