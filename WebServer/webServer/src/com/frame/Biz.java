package com.frame;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

public interface Biz<K, V> {
	@Transactional //트랙젝션 처리 @Transactional을 사용한다 -  실행이 이상하면 롤백한다
	public void register(V v) throws Exception;
	@Transactional
	public void remove(K k) throws Exception;
	@Transactional
	public void modify(V v) throws Exception;
	public V get(K k) throws Exception;
	public ArrayList<V> get() throws Exception;
}
