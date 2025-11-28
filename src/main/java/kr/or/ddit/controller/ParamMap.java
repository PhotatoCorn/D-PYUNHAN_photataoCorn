package kr.or.ddit.controller;

import java.util.HashMap;

public class ParamMap extends HashMap<String, Object> {
	
	public ParamMap() {}
	public static ParamMap init() {
		return new ParamMap();
	}
	public String getString(String key) {
		Object object = this.get(key);
//		return(String) object;
		
		if(object==null) {
			return null;
		}else {
			return String.valueOf(object);
		}
	}
	
	// 정확한 숫자를 계산해야 할 땐 BigInteger
	// int 는 null이 없기떄문에 Integer를쓴다
	public Integer getInt(String key) {
		Object object = this.get(key);
		return (Integer) object;
	}
	public Double getDouble(String key) {
		Object object = this.get(key);
		return (Double) object;
	}
	public <T> T getT(String key, Class<T> classt) {
		Object object = this.get(key);
		return (T) object;
	}
	
	
	public static void maid(String[] args) {
		
		String data = null;
		System.out.println(String.valueOf(data).length());
		System.out.println(data.length());
		
		ParamMap init = ParamMap.init();
		init.put("key1", 1);
		init.put("key2", "test");
		init.put("key3", new StringBuffer("test2"));
		String test = (String) init.get("key2");
//		StringBuffer sb = init.get("key3",StringBuffer.class);
		
//		System.out.println(sb);
	}
}
