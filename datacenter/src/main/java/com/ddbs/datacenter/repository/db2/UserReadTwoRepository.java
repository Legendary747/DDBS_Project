package com.ddbs.datacenter.repository.db2;

import com.ddbs.datacenter.entities.UserRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReadTwoRepository extends JpaRepository<UserRead, String> {
    // Custom query methods for Read
}
