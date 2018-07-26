package cn.lanyj.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.lanyj.services.models.User;

/**
 * @author Raysmond
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
