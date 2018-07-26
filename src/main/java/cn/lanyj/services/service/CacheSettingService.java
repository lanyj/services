package cn.lanyj.services.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lanyj.services.models.Setting;
import cn.lanyj.services.repositories.SettingRepository;

/**
 * @author Raysmond
 */
@Service
@Transactional
public class CacheSettingService implements SettingService {
    private Logger log = LoggerFactory.getLogger(CacheSettingService.class);
	private SettingRepository settingRepository;

    @Autowired
    public CacheSettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    @Cacheable(value = "settingCache", key = "#key")
    public Serializable get(String key) {
        Setting setting = settingRepository.findByKey(key);
        Serializable value = null;
        try {
            value = setting == null ? null : setting.getValue();
        } catch (Exception ex) {
            log.info("Cannot deserialize setting value with key = " + key);
        }

        log.info("Get setting " + key + " from database. Value = " + value);

        return value;
    }

    @Override
    @Cacheable(value = "settingCache", key = "#key")
    public Serializable get(String key, Serializable defaultValue) {
        Serializable value = get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    @CacheEvict(value = "settingCache", key = "#key")
    public void put(String key, Serializable value) {
        log.info("Update setting " + key + " to database. Value = " + value);

        Setting setting = settingRepository.findByKey(key);
        if (setting == null) {
            setting = new Setting();
            setting.setKey(key);
        }
        try {
            setting.setValue(value);
            settingRepository.save(setting);
        } catch (Exception ex) {

            log.info("Cannot save setting value with type: " + value.getClass() + ". key = " + key);
        }
    }
}
