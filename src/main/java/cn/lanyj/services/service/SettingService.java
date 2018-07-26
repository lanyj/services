package cn.lanyj.services.service;

import java.io.Serializable;

/**
 * @author Raysmond
 */
public interface SettingService {
    Serializable get(String key);

    Serializable get(String key, Serializable defaultValue);

    void put(String key, Serializable value);
}
