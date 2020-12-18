package com.biz;

import org.springframework.stereotype.Service;

@Service
public interface FcmLog<V> {
	public void fcmlog(V v);
}
