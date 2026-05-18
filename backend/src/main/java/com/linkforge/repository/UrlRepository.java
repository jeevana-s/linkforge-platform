package com.linkforge.repository;
import com.linkforge.entity.Url;
import com.linkforge.entity.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UrlRepository extends JpaRepository<Url, Long> {
  Optional<Url> findByShortCode(String shortCode);
  Page<Url> findByUser(User user, Pageable pageable);
  boolean existsByShortCode(String shortCode);
}
