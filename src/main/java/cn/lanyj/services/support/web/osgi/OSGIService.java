package cn.lanyj.services.support.web.osgi;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

@PropertySource({"classpath:app.properties"})
@Service
public class OSGIService {

	private Logger log = LoggerFactory.getLogger(OSGIService.class);

	ConcurrentReferenceHashMap<String, IOSGIService> services;
	FileListener listener;

	@Autowired
    public OSGIService(Environment env) {
		String basePath = env.getProperty("osgi.lib.base");
		if(!StringUtils.hasText(basePath)) {
			basePath = "/tomcat/services/osgi/lib/";
			File tmp = new File(basePath);
			if(!tmp.exists()) {
				tmp.mkdirs();
			}
			log.warn("app.properties not contains osgi.lib.base. using " + tmp.getAbsolutePath() + " as default.");
		}
		
		File root = new File(basePath);
		services = new ConcurrentReferenceHashMap<>();
		listener = new FileListener(root.getAbsolutePath(), services);

		long interval = TimeUnit.SECONDS.toMillis(10);
		IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
		IOFileFilter filter = FileFilterUtils.or(directories, FileFilterUtils.fileFileFilter());
		FileAlterationObserver observer = new FileAlterationObserver(root, filter);
		observer.addListener(listener);
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
		try {
			monitor.start();
		} catch (Exception e) {
		}
	}

	public boolean avaliabe(String name) {
		return services.containsKey(name);
	}
	
	public Object invoke(String name, HttpServletRequest req, HttpServletResponse rep) {
		IOSGIService service = services.get(name);
		if(service != null) {
			return service.invoke(req, rep);
		} else {
			log.debug("osgi module '{}' not found", name);
			throw new OSGIServiceNotFoundException("osgi module '" + name + "' not loaded.");
		}
	}

	public void reload() {
		log.debug("osgi module is reloading...");
		listener.reload(null);
	}
	
}

class FileListener extends FileAlterationListenerAdaptor {
	private Logger log = LoggerFactory.getLogger(FileListener.class);

	Map<String, IOSGIService> services;
	ServiceLoader<IOSGIService> loader;
	
	public FileListener(String basepath, Map<String, IOSGIService> services) {
		this.services = services;
		URLClassLoader classLoader = null;
		try {
			if(!StringUtils.hasText(basepath)) {
				classLoader = new URLClassLoader(new URL[] {}, Thread.currentThread().getContextClassLoader());
			} else {
				basepath = basepath.replaceAll("\\\\", "/");
				if(!basepath.endsWith("/")) {
					basepath += "/";
				}
				classLoader = new URLClassLoader(new URL[] {new URL("file:///" + basepath)}, Thread.currentThread().getContextClassLoader());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		loader = ServiceLoader.load(IOSGIService.class, classLoader);
		
		reload(null);
	}
	
	public void reload(File file) {
		if(file != null && !file.getName().equals(IOSGIService.class.getName())) {
			return;
		}
		log.info("Service loading...");
		services.clear();
		loader.reload();
		for(IOSGIService service : loader) {
			log.info("{} was reloaded.", service);
			services.put(service.name(), service);
		}
		log.info("Service loaded");
	}
	
	public void onFileCreate(File file) {
		reload(file);
	}

	public void onFileChange(File file) {
		reload(file);
	}

	public void onFileDelete(File file) {
		reload(file);
	}

	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
	}

	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
	}

}