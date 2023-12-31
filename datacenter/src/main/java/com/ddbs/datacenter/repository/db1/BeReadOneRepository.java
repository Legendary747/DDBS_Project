package com.ddbs.datacenter.repository.db1;

import com.ddbs.datacenter.entities.BeRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeReadOneRepository extends JpaRepository<BeRead, String> {
    // Custom query methods for BeRead
}
