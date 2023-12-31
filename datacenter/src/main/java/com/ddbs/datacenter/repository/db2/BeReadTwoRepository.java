package com.ddbs.datacenter.repository.db2;

import com.ddbs.datacenter.entities.BeRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeReadTwoRepository extends JpaRepository<BeRead, String> {
    // Custom query methods for BeRead
}
