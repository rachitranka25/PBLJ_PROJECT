package com.auction.repository;

import com.auction.model.Auction;
import com.auction.model.Auction.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatus(AuctionStatus status);
    List<Auction> findByStatusOrderByEndTimeAsc(AuctionStatus status);
    List<Auction> findBySellerId(Long sellerId);
    List<Auction> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT a FROM Auction a WHERE a.status = 'ACTIVE' AND a.endTime > :now ORDER BY a.endTime ASC")
    List<Auction> findActiveAuctions(LocalDateTime now);
}




