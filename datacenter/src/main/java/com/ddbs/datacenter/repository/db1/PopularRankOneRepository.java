package com.ddbs.datacenter.repository.db1;

import com.ddbs.datacenter.entities.PopularRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularRankOneRepository extends JpaRepository<PopularRank, String> {
    // Custom query methods for PopularRank
}
