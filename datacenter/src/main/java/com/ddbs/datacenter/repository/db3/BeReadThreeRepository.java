package com.ddbs.datacenter.repository.db3;

import com.ddbs.datacenter.entities.BeRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BeReadThreeRepository extends JpaRepository<BeRead, String> {
    // Custom query methods for BeRead
    @Query(value = "SELECT * FROM be_read WHERE aid = ?1", nativeQuery = true)
    BeRead findByAid(String aid);
}
