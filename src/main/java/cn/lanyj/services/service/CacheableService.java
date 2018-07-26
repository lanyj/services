package cn.lanyj.services.service;

public interface CacheableService<T> {
	
	public T sync(String id);
	
	public T get(String id);
	
	public String save(T value);
}
