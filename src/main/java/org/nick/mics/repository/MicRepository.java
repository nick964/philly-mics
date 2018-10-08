package org.nick.mics.repository;

import org.nick.mics.domain.Mic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Mic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicRepository extends JpaRepository<Mic, Long> {

    @Query("select mic from Mic mic where mic.user.login = ?#{principal.username}")
    List<Mic> findByUserIsCurrentUser();

    @Query(value = "select distinct mic from Mic mic left join fetch mic.hosts",
        countQuery = "select count(distinct mic) from Mic mic")
    Page<Mic> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct mic from Mic mic left join fetch mic.hosts")
    List<Mic> findAllWithEagerRelationships();

    @Query("select mic from Mic mic left join fetch mic.hosts where mic.id =:id")
    Optional<Mic> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct mic from Mic mic where mic.micType = :micType and mic.endDate > :givenDate")
    List<Mic> findMicsbyTypeAndDate(@Param("micType") String micType, @Param("givenDate") Date givenDate);

}
