package cn.lanyj.services.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	
	static final AtomicLong ID = new AtomicLong();
	
	/**
	 * os will not be closed
	 * @param os
	 * @param paths
	 * @param names
	 * @throws IOException
	 */
	public static void zip(OutputStream os, List<String> paths, List<String> names) throws IOException {
		if(paths.size() != names.size()) {
			throw new IllegalArgumentException("path size not equals with name size.");
		}
		ZipOutputStream zos = new ZipOutputStream(os);
		for(int i = paths.size() - 1; i >= 0; i--) {
			String path = paths.get(i);
			String name = paths.get(i);
			zos.putNextEntry(new ZipEntry(name));
			putFile(zos, new File(path));
		}
	}

	public static void putFile(ZipOutputStream zos, File file) throws IOException {
		ZipEntry entry = new ZipEntry(file.getName());
		zos.putNextEntry(entry);
		InputStream is = new FileInputStream(file);
		is2os(zos, is);
		is.close();
	}
	
	public static void is2os(OutputStream os, InputStream is) throws IOException {
		byte[] buf = new byte[1024 * 4];
		int length = -1;
		while((length = is.read(buf)) != -1) {
			os.write(buf, 0, length);
		}
	}
	
}
