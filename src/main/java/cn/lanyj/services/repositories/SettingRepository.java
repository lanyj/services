package cn.lanyj.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.lanyj.services.models.Setting;

/**
 * @author Raysmond
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    Setting findByKey(String key);
}
