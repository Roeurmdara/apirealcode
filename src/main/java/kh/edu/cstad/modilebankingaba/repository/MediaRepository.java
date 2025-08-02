package kh.edu.cstad.modilebankingaba.repository;

import kh.edu.cstad.modilebankingaba.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    Optional<Media> findByNameAndExtension(String name, String extension);

    @Transactional
    @Modifying
    @Query("DELETE FROM Media m WHERE CONCAT(m.name, '.', m.extension) = :filename")
    void deleteByNameAndExtension(String filename);
}
