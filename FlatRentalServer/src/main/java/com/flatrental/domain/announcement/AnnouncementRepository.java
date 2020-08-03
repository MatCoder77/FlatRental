package com.flatrental.domain.announcement;

import com.flatrental.domain.managedobject.ManagedObjectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, CustomAnnouncementRepository {

    Optional<Announcement> findByIdAndObjectStateNot(Long id, ManagedObjectState objectState);

    List<Announcement> getAllByUpdatedAtBeforeAndObjectState(Instant updatedAt, ManagedObjectState managedObjectState);

}
