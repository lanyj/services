package cn.lanyj.services.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.lanyj.services.models.SharedFile;

@Repository
public interface SharedFileRepository extends JpaRepository<SharedFile, String> {
	@Query("SELECT s FROM SharedFile s WHERE TIMESTAMPDIFF(DAY, s.createdAt, NOW()) >= s.expireDay")
	public Page<SharedFile> getNeededToCleanFiles(Pageable pageable);
}
