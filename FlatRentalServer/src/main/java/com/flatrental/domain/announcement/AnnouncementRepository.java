package com.flatrental.domain.announcement;

import com.flatrental.domain.ManagedObjectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    Optional<Announcement> findByIdAndObjectStateNot(Long id, ManagedObjectState objectState);

}
