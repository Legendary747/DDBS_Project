package com.ddbs.datacenter.repository.db2;

import com.ddbs.datacenter.entities.PopularRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularRankTwoRepository extends JpaRepository<PopularRank, String> {
    // Custom query methods for PopularRank
}
