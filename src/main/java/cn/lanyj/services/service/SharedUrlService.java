package cn.lanyj.services.service;

import java.util.List;

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

import cn.lanyj.services.models.SharedUrl;
import cn.lanyj.services.repositories.SharedUrlRepository;

@Transactional
@Service
public class SharedUrlService implements CacheableService<SharedUrl> {
	private Logger log = LoggerFactory.getLogger(SharedUrlService.class);
	
	@Autowired
	SharedUrlRepository repository;
	
	@CacheEvict(value="urlCache", allEntries=true)
	@Scheduled(cron="0 0 0 * * ?")
	public void cleanAllExpired() {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Page<SharedUrl> page = repository.getNeededToCleanUrls(pageable);
		List<SharedUrl> urls = page.getContent();
		log.info("Founded {} expired urls", urls.size());
		repository.deleteInBatch(urls);
	}
	
	@Cacheable(value="urlCache", key="#id")
	public SharedUrl get(String id) {
		return repository.getOne(id);
	}
	
	public String save(SharedUrl url) {
		String id = repository.save(url).getId();
		url.setId(id);
		log.debug("saving url id = {}, url = {}", id, url);
		return id;
	}
	
	@CacheEvict(value="urlCache", key = "#id")
	public SharedUrl sync(String id) {
		return get(id);
	}
	
	public SharedUrl findByUrl(String url) {
		return repository.findByUrl(url);
	}
}
