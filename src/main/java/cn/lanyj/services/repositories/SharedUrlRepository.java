package cn.lanyj.services.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.lanyj.services.models.SharedUrl;

@Repository
public interface SharedUrlRepository extends JpaRepository<SharedUrl, String> {
	@Query("SELECT s FROM SharedUrl s WHERE TIMESTAMPDIFF(DAY, s.createdAt, NOW()) >= s.expireDay")
	public Page<SharedUrl> getNeededToCleanUrls(Pageable pageable);
	
	public SharedUrl findByUrl(String url);
}
