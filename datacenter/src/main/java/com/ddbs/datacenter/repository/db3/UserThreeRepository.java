package com.ddbs.datacenter.repository.db3;

import com.ddbs.datacenter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserThreeRepository extends JpaRepository<User, String> {
    @Query("SELECT MAX(u.uid) FROM User u")
    String findMaxUid();
}
