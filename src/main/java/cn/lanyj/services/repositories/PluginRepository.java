package cn.lanyj.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.lanyj.services.models.Plugin;

/**
 * @author Raysmond
 */
@Repository
public interface PluginRepository extends JpaRepository<Plugin, String> {
	Plugin findByRemoteUrl(String url);
}
