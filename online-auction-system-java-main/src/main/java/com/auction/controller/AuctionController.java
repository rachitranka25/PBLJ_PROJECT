package com.auction.controller;

import com.auction.dto.AuctionDTO;
import com.auction.dto.BidRequest;
import com.auction.model.Auction;
import com.auction.model.User;
import com.auction.service.AuctionService;
import com.auction.service.BidService;
import com.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auctions")
@CrossOrigin(origins = "*")
public class AuctionController {
    @Autowired
    private AuctionService auctionService;
    
    @Autowired
    private BidService bidService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<AuctionDTO>> getAllAuctions(@RequestParam(required = false) String status,
                                                           @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(auctionService.searchAuctions(search));
        }
        if ("active".equals(status)) {
            return ResponseEntity.ok(auctionService.getAllActiveAuctions());
        }
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDTO> getAuctionById(@PathVariable Long id) {
        AuctionDTO auction = auctionService.getAuctionById(id);
        if (auction != null) {
            return ResponseEntity.ok(auction);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAuction(@RequestBody Auction auction,
                                                              @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        Map<String, Object> response = new HashMap<>();
        
        User seller = userService.getUserBySession(sessionId);
        if (seller == null) {
            response.put("success", false);
            response.put("message", "Please login to create an auction");
            return ResponseEntity.status(401).body(response);
        }
        
        try {
            AuctionDTO created = auctionService.createAuction(auction, seller);
            response.put("success", true);
            response.put("auction", created);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<List<com.auction.dto.BidDTO>> getAuctionBids(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.getBidsByAuction(id));
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<Map<String, Object>> placeBid(@PathVariable Long id,
                                                        @RequestBody BidRequest bidRequest,
                                                        @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        Map<String, Object> response = new HashMap<>();
        
        User bidder = userService.getUserBySession(sessionId);
        if (bidder == null) {
            response.put("success", false);
            response.put("message", "Please login to place a bid");
            return ResponseEntity.status(401).body(response);
        }
        
        try {
            com.auction.dto.BidDTO bid = bidService.placeBid(id, bidRequest.getAmount(), bidder);
            response.put("success", true);
            response.put("bid", bid);
            response.put("message", "Bid placed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<AuctionDTO>> getAuctionsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(auctionService.getAuctionsBySeller(sellerId));
    }
}




