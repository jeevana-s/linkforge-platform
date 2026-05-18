package com.linkforge.repository;
import com.linkforge.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
  long countByUrlId(Long urlId);
  @Query("select distinct c.ip from ClickEvent c where c.url.id=:id")
  List<String> findDistinctIps(@Param("id") Long id);
  List<ClickEvent> findTop500ByUrlIdOrderByClickedAtDesc(Long urlId);
}
