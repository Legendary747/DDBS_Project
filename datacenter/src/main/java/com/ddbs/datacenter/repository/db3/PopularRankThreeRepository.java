package com.ddbs.datacenter.repository.db3;

import com.ddbs.datacenter.entities.PopularRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularRankThreeRepository extends JpaRepository<PopularRank, String> {
    // Custom query methods for PopularRank
}
