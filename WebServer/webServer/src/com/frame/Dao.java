package com.frame;

import java.util.ArrayList;

public interface Dao<K1,K2,V> {
	public int insert(V v) throws Exception;
	public int delete(K1 k) throws Exception;
	public int update(V v) throws Exception;
	public V select(K1 k) throws Exception;
	public ArrayList<V> selectall() throws Exception;
	public ArrayList<V> selectcarsfromuser(K2 k) throws Exception;
	public V selectfromkeys(K1 k1, K2 k2) throws Exception;
	public V caridfromnumber(String k) throws Exception;
}
