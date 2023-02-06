package org.kq.addressbook;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel="buddyInfoes", path="buddyInfoes")
public interface BuddyInfoRepository extends CrudRepository<BuddyInfo, Long> {
    BuddyInfo findById(long id);
    List<BuddyInfo> findByName(@Param("name") String name);
}
