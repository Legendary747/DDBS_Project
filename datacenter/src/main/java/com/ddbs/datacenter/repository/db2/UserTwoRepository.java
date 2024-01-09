package com.ddbs.datacenter.repository.db2;

import com.ddbs.datacenter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTwoRepository extends JpaRepository<User, String> {
    @Query("SELECT MAX(u.uid) FROM User u")
    String findMaxUid();
}
