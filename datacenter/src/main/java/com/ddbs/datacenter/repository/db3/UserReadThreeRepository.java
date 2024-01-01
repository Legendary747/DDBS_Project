package com.ddbs.datacenter.repository.db3;

import com.ddbs.datacenter.entities.UserRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReadThreeRepository extends JpaRepository<UserRead, String> {

    @Query(value = "SELECT * FROM user_read WHERE uid = ?1", nativeQuery = true)
    List<UserRead> findByUid(String userId);
    // Custom query methods for Read
}
