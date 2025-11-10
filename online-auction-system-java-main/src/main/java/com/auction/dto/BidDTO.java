package com.auction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidDTO {
    private Long id;
    private BigDecimal amount;
    private Long auctionId;
    private UserDTO bidder;
    private LocalDateTime bidTime;

    public BidDTO() {}

    public BidDTO(Long id, BigDecimal amount, Long auctionId, UserDTO bidder, LocalDateTime bidTime) {
        this.id = id;
        this.amount = amount;
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.bidTime = bidTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }
    public UserDTO getBidder() { return bidder; }
    public void setBidder(UserDTO bidder) { this.bidder = bidder; }
    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }
}
