package cn.lanyj.services.support.web.storage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import cn.lanyj.services.support.web.storage.StorageService;
import cn.lanyj.services.support.web.storage.StorageServiceException;
import cn.lanyj.services.util.ZipUtils;

@Service("disk-storage-service")
@PropertySource({"classpath:app.properties"})
public class DiskStorageService implements StorageService {

	private Logger log = LoggerFactory.getLogger(DiskStorageService.class);
	
	final String BASE;
	
	@Autowired
	public DiskStorageService(Environment env) {
		String path = env.getProperty("disk.storage.path");
		if(path == null) {
			BASE = "/tomcat/services/disk_storage_service/";
			log.warn("app.properties not contains disk.storage.path. using " + BASE + " as default.");
		} else {
			BASE = path;
		}
		File file = new File(BASE);
		if(!file.exists()) {
			file.mkdirs();
		}
		if(!file.isDirectory()) {
			file.delete();
			file.mkdirs();
		}
		log.debug("set disk storage base as '{}'", file.getAbsolutePath());
	}
	
	private void close(InputStream is) {
		if(is != null) {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}
	
	private void close(OutputStream os) {
		if(os != null) {
			try {
				os.close();
			} catch (Exception e) {
			}
		}
	}

	
	private String path(String id) {
		return BASE + id + ".sve";
	}
	
	@Override
	public boolean put(String id, InputStream is) {
		File file = null;
		FileOutputStream os = null;
		try {
			file = new File(path(id));
			os = new FileOutputStream(file);
			StreamUtils.copy(is, os);
		} catch (IOException e) {
			e.printStackTrace();
			if(file != null) {
				file.delete();
			}
			log.error("save file failed", e);
			throw new StorageServiceException("Save file failed.");
		} finally {
			close(os);
		}
		return false;
	}

	@Override
	public long get(String id, OutputStream os) {
		File file = null;
		InputStream is = null;
		try {
			file = new File(path(id));
			if(file == null || !file.exists() || file.isDirectory()) {
				return -1;
			}
			is = new FileInputStream(file);
			return StreamUtils.copy(is, os);
		} catch (IOException e) {
			log.error("get file failed", e);
			throw new StorageServiceException("Open file failed.");
		} finally {
			close(is);
		}
	}

	@Override
	public boolean getInZip(List<String> ids, List<String> names, OutputStream os, boolean skipOnNotFound) {
		try {
			for(int i = ids.size() - 1; i >= 0; i--) {
				// case file will not exist
				String id = ids.get(i);
				if(constains(id)) {
					ids.set(i, new File(path(id)).getAbsolutePath());
				} else {
					if(skipOnNotFound) {
						ids.remove(i);
						names.remove(i);
					} else {
						return false;
					}
				}
			}
			ZipUtils.zip(os, ids, names);
		} catch (NullPointerException | IOException e) {
			log.error("zip file failed", e);
			throw new StorageServiceException("Zip file failed.");
		}
		return true;
	}

	@Override
	public boolean constains(String id) {
		File file = new File(path(id));
		return (file != null) && (file.exists()) && (file.isFile());
	}

	@Override
	public long length(String id) {
		return constains(id) ? (new File(path(id)).length()) : -1;
	}

	@Override
	public void delete(String id) {
		if(constains(id)) {
			File file = new File(path(id));
			file.delete();
		}
	}

	@Override
	public void deleteInBatch(List<String> ids) {
		for(String id : ids) {
			delete(id);
		}
	}

}
