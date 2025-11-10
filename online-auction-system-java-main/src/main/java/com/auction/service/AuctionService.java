package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.model.Auction;
import com.auction.model.Auction.AuctionStatus;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    @Autowired
    private AuctionRepository auctionRepository;
    
    @Autowired
    private UserService userService;

    public List<AuctionDTO> getAllActiveAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> auctions = auctionRepository.findActiveAuctions(now);
        return auctions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<AuctionDTO> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuctionDTO getAuctionById(Long id) {
        if (id == null) return null;
        Auction auction = auctionRepository.findById(id).orElse(null);
        return auction != null ? convertToDTO(auction) : null;
    }

    @Transactional
    public AuctionDTO createAuction(Auction auction, User seller) {
        auction.setSeller(seller);
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setCurrentPrice(auction.getStartingPrice());
        auction = auctionRepository.save(auction);
        return convertToDTO(auction);
    }

    @Transactional
    public AuctionDTO updateAuction(Long id, Auction updatedAuction) {
        if (id == null) return null;
        Auction auction = auctionRepository.findById(id).orElse(null);
        if (auction == null) return null;
        
        auction.setTitle(updatedAuction.getTitle());
        auction.setDescription(updatedAuction.getDescription());
        auction.setStartingPrice(updatedAuction.getStartingPrice());
        auction.setEndTime(updatedAuction.getEndTime());
        auction.setImageUrl(updatedAuction.getImageUrl());
        
        auction = auctionRepository.save(auction);
        return convertToDTO(auction);
    }

    public List<AuctionDTO> getAuctionsBySeller(Long sellerId) {
        if (sellerId == null) return List.of();
        return auctionRepository.findBySellerId(sellerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuctionDTO> searchAuctions(String query) {
        return auctionRepository.findByTitleContainingIgnoreCase(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuctionDTO convertToDTO(Auction auction) {
        if (auction == null) return null;
        
        AuctionDTO dto = new AuctionDTO();
        dto.setId(auction.getId());
        dto.setTitle(auction.getTitle());
        dto.setDescription(auction.getDescription());
        dto.setStartingPrice(auction.getStartingPrice());
        dto.setCurrentPrice(auction.getCurrentPrice());
        dto.setStartTime(auction.getStartTime());
        dto.setEndTime(auction.getEndTime());
        dto.setImageUrl(auction.getImageUrl());
        dto.setStatus(auction.getStatus());
        dto.setSeller(userService.convertToDTO(auction.getSeller()));
        dto.setCurrentWinner(userService.convertToDTO(auction.getCurrentWinner()));
        dto.setBidCount((long) auction.getBids().size());
        dto.setCreatedAt(auction.getCreatedAt());
        return dto;
    }

    public Auction getAuctionEntity(Long id) {
        if (id == null) return null;
        return auctionRepository.findById(id).orElse(null);
    }
}

