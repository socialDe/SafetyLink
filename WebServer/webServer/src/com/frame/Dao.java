package com.frame;

import java.util.ArrayList;

public interface Dao<K, V> {
	public int insert(V v) throws Exception;
	public int delete(K k) throws Exception;
	public int update(V v) throws Exception;
	public V select(K k) throws Exception;
	public ArrayList<V> selectall() throws Exception;
}
