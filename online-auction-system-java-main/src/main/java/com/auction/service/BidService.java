package com.auction.service;

import com.auction.dto.BidDTO;
import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;
    
    @Autowired
    private AuctionRepository auctionRepository;
    
    @Autowired
    private AuctionService auctionService;
    
    @Autowired
    private UserService userService;

    @Transactional
    public BidDTO placeBid(Long auctionId, BigDecimal amount, User bidder) {
        Auction auction = auctionService.getAuctionEntity(auctionId);
        
        if (auction == null) {
            throw new RuntimeException("Auction not found");
        }
        
        if (auction.getStatus() != Auction.AuctionStatus.ACTIVE) {
            throw new RuntimeException("Auction is not active");
        }
        
        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new RuntimeException("Auction has ended");
        }
        
        if (amount.compareTo(auction.getCurrentPrice()) <= 0) {
            throw new RuntimeException("Bid amount must be higher than current price");
        }
        
        if (auction.getSeller().getId().equals(bidder.getId())) {
            throw new RuntimeException("You cannot bid on your own auction");
        }
        
        Bid bid = new Bid();
        bid.setAuction(auction);
        bid.setBidder(bidder);
        bid.setAmount(amount);
        
        bid = bidRepository.save(bid);
        
        // Update auction current price and winner
        auction.setCurrentPrice(amount);
        auction.setCurrentWinner(bidder);
        auctionRepository.save(auction);
        
        return convertToDTO(bid);
    }

    public List<BidDTO> getBidsByAuction(Long auctionId) {
        return bidRepository.findByAuctionIdOrderByBidTimeDesc(auctionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BidDTO> getBidsByUser(Long userId) {
        return bidRepository.findByBidderId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BidDTO convertToDTO(Bid bid) {
        if (bid == null) return null;
        
        BidDTO dto = new BidDTO();
        dto.setId(bid.getId());
        dto.setAmount(bid.getAmount());
        dto.setAuctionId(bid.getAuction().getId());
        dto.setBidder(userService.convertToDTO(bid.getBidder()));
        dto.setBidTime(bid.getBidTime());
        return dto;
    }
}

