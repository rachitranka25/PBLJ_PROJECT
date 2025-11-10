package com.auction.dto;

import com.auction.model.Auction.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AuctionDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;
    private AuctionStatus status;
    private UserDTO seller;
    private UserDTO currentWinner;
    private Long bidCount;
    private LocalDateTime createdAt;

    public AuctionDTO() {}

    public AuctionDTO(Long id, String title, String description, BigDecimal startingPrice, BigDecimal currentPrice,
                       LocalDateTime startTime, LocalDateTime endTime, String imageUrl, AuctionStatus status,
                       UserDTO seller, UserDTO currentWinner, Long bidCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageUrl = imageUrl;
        this.status = status;
        this.seller = seller;
        this.currentWinner = currentWinner;
        this.bidCount = bidCount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getStartingPrice() { return startingPrice; }
    public void setStartingPrice(BigDecimal startingPrice) { this.startingPrice = startingPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }
    public UserDTO getSeller() { return seller; }
    public void setSeller(UserDTO seller) { this.seller = seller; }
    public UserDTO getCurrentWinner() { return currentWinner; }
    public void setCurrentWinner(UserDTO currentWinner) { this.currentWinner = currentWinner; }
    public Long getBidCount() { return bidCount; }
    public void setBidCount(Long bidCount) { this.bidCount = bidCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
