package com.url_shortener.repository;

import com.url_shortener.model.UrlHash;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlHashRepository extends CrudRepository<UrlHash, String> {
    List<UrlHash> findAll();
    Optional<UrlHash> getByHash(String hash);

    @Modifying
    @Query("delete from url_hash t where t.hash = :hash")
    void deleteByHash(@Param("hash") String hash);
}
