package cn.lanyj.services.support.web.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.util.StreamUtils;

public interface StorageService {
	/**
	 * save file with id, if old id exist, it will replace old id
	 * @param id as filename's part
	 * @param is input stream
	 * @return true on success
	 */
	public boolean put(String id, InputStream is);
	
	/**
	 * read from storage
	 * @param id
	 * @param os
	 * @return size of read
	 */
	public long get(String id, OutputStream os);
	
	/**
	 * zip file
	 * @param ids file's id
	 * @param names name's id
	 * @param os
	 * @param skipOnNotFound true will not abort on file not exist
	 * @return true on success, generally if {@code skipOnNotFound is true}, it will be return true normally
	 */
	public boolean getInZip(List<String> ids, List<String> names, OutputStream os, boolean skipOnNotFound);
	
	/**
	 * test is file exist
	 * @param id
	 * @return
	 */
	public boolean constains(String id);
	
	/**
	 * get file length
	 * @param id
	 * @return
	 */
	public long length(String id);
	
	/**
	 * remove file
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * remove files, most useful when impl is remote file storage service
	 * @param ids
	 */
	public void deleteInBatch(List<String> ids);
	
	public static void streamCopy(InputStream is, OutputStream os) throws IOException {
		StreamUtils.copy(is, os);
	}
	
}
