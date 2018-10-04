package org.nick.mics.repository;

import org.nick.mics.domain.Host;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Host entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HostRepository extends JpaRepository<Host, Long> {

}
