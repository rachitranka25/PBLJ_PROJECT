package com.auction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiInfoController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Online Auction System API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("status", "running");
        
        Map<String, Object> endpoints = new HashMap<>();
        
        // Authentication endpoints
        Map<String, String> authEndpoints = new HashMap<>();
        authEndpoints.put("register", "POST /api/auth/register");
        authEndpoints.put("login", "POST /api/auth/login");
        authEndpoints.put("checkSession", "GET /api/auth/session/{sessionId}");
        endpoints.put("authentication", authEndpoints);
        
        // Auction endpoints
        Map<String, String> auctionEndpoints = new HashMap<>();
        auctionEndpoints.put("getAll", "GET /api/auctions");
        auctionEndpoints.put("getAllActive", "GET /api/auctions?status=active");
        auctionEndpoints.put("search", "GET /api/auctions?search={query}");
        auctionEndpoints.put("getById", "GET /api/auctions/{id}");
        auctionEndpoints.put("create", "POST /api/auctions");
        auctionEndpoints.put("getBids", "GET /api/auctions/{id}/bids");
        auctionEndpoints.put("placeBid", "POST /api/auctions/{id}/bid");
        auctionEndpoints.put("getBySeller", "GET /api/auctions/seller/{sellerId}");
        endpoints.put("auctions", auctionEndpoints);
        
        apiInfo.put("endpoints", endpoints);
        
        Map<String, String> info = new HashMap<>();
        info.put("baseUrl", "http://localhost:8080/api");
        info.put("documentation", "See README.md for detailed API documentation");
        apiInfo.put("info", info);
        
        return ResponseEntity.ok(apiInfo);
    }
}

