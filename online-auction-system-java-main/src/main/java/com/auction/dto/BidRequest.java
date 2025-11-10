package com.auction.dto;

import java.math.BigDecimal;

public class BidRequest {
    private Long auctionId;
    private BigDecimal amount;

    public BidRequest() {}

    public BidRequest(Long auctionId, BigDecimal amount) {
        this.auctionId = auctionId;
        this.amount = amount;
    }

    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
