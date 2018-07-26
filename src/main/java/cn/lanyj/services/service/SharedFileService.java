package cn.lanyj.services.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lanyj.services.error.ServiceProcessException;
import cn.lanyj.services.models.SharedFile;
import cn.lanyj.services.repositories.SharedFileRepository;
import cn.lanyj.services.support.web.storage.StorageService;
import cn.lanyj.services.support.web.storage.StorageServiceException;

@Transactional
@Service
public class SharedFileService implements CacheableService<SharedFile> {
	private Logger log = LoggerFactory.getLogger(SharedFileService.class);
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	SharedFileRepository repository;
	
	@CacheEvict(value="fileCache", allEntries=true)
	@Scheduled(cron="0 0 0 * * ?")
	public void cleanAllExpired() {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Page<SharedFile> page = repository.getNeededToCleanFiles(pageable);
		List<SharedFile> files = page.getContent();
		log.info("Founded {} expired files", files.size());

		List<String> ids = new ArrayList<>();
		for(SharedFile file : files) {
			ids.add(file.getPath());
		}
		storageService.deleteInBatch(ids);
		
		repository.deleteInBatch(files);
	}
	
	@Cacheable(value="fileCache", key="#id")
	public SharedFile get(String id) {
		return repository.getOne(id);
	}
	
	public String save(SharedFile file) {
		String id = repository.save(file).getId();
		file.setId(id);
		log.debug("save shared file id = {}, path = {}", id, file.getPath());
		return id;
	}
	
	@CacheEvict(value="fileCache", key = "#id")
	public SharedFile sync(String id) {
		SharedFile file = get(id);
		if(file == null) {
			return null;
		}
		if(!(storageService.constains(file.getPath()))) {
			repository.delete(file);
			return null;
		}
		return file;
	}
	
	public SharedFile createAndSave(MultipartFile _file, String charset, int expire) {
		// use '_' to avoid name duplicated
		String filepath = System.currentTimeMillis() + "_" + ID.getAndIncrement();
		InputStream is = null;
		try {
			is = _file.getInputStream();
			storageService.put(filepath, is);
		} catch (IOException | StorageServiceException e) {
			log.error("save file failed", e);

			storageService.delete(filepath);
			throw new ServiceProcessException("save file failed.");
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		SharedFile file = new SharedFile();
		file.setExpireDay(expire);
		file.setPath(filepath);
		try {
			file.setOriginalName(new String(_file.getOriginalFilename().getBytes(charset), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
		}
		save(file);
		return file;
	}
	
	public void read(SharedFile file, OutputStream os) {
		storageService.get(file.getPath(), os);
	}
	
	public void read(List<SharedFile> files, OutputStream os, boolean skipOnNotFound) {
		List<String> ids = new ArrayList<>();
		List<String> names = new ArrayList<>();
		for(SharedFile file : files) {
			ids.add(file.getPath());
			names.add(file.getOriginalName());
		}
		storageService.getInZip(ids, names, os, skipOnNotFound);
	}
	
	public long length(SharedFile file) {
		return storageService.length(file.getPath());
	}
	
	public StorageService getStorageService() {
		return storageService;
	}
	
	final static AtomicLong ID = new AtomicLong(System.currentTimeMillis());
	
}
