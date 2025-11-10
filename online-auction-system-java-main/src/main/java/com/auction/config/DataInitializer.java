package com.auction.config;

import com.auction.model.Auction;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public void run(String... args) {
        // Create sample users
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("john_doe");
            user1.setEmail("john@example.com");
            user1.setPassword("password123");
            user1.setFullName("John Doe");
            user1 = userRepository.save(user1);

            User user2 = new User();
            user2.setUsername("jane_smith");
            user2.setEmail("jane@example.com");
            user2.setPassword("password123");
            user2.setFullName("Jane Smith");
            user2 = userRepository.save(user2);

            // Create sample auctions
            Auction auction1 = new Auction();
            auction1.setTitle("Vintage Rolex Watch");
            auction1.setDescription("Beautiful vintage Rolex watch from 1980s. Excellent condition with original box and papers.");
            auction1.setStartingPrice(new BigDecimal("5000.00"));
            auction1.setCurrentPrice(new BigDecimal("5000.00"));
            auction1.setStartTime(LocalDateTime.now().minusDays(2));
            auction1.setEndTime(LocalDateTime.now().plusDays(5));
            auction1.setImageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800");
            auction1.setSeller(user1);
            auction1.setStatus(Auction.AuctionStatus.ACTIVE);
            auctionRepository.save(auction1);

            Auction auction2 = new Auction();
            auction2.setTitle("Rare Collectible Comic Book");
            auction2.setDescription("First edition Spider-Man comic book from 1963. Mint condition, professionally graded.");
            auction2.setStartingPrice(new BigDecimal("2000.00"));
            auction2.setCurrentPrice(new BigDecimal("2000.00"));
            auction2.setStartTime(LocalDateTime.now().minusDays(1));
            auction2.setEndTime(LocalDateTime.now().plusDays(7));
            auction2.setImageUrl("https://images.unsplash.com/photo-1612198188060-c7c2a3b66eae?w=800");
            auction2.setSeller(user2);
            auction2.setStatus(Auction.AuctionStatus.ACTIVE);
            auctionRepository.save(auction2);

            Auction auction3 = new Auction();
            auction3.setTitle("Antique Persian Rug");
            auction3.setDescription("Handwoven Persian rug from early 1900s. Beautiful intricate patterns, excellent condition.");
            auction3.setStartingPrice(new BigDecimal("3000.00"));
            auction3.setCurrentPrice(new BigDecimal("3000.00"));
            auction3.setStartTime(LocalDateTime.now());
            auction3.setEndTime(LocalDateTime.now().plusDays(10));
            auction3.setImageUrl("https://images.unsplash.com/photo-1586075010923-2dd4570fb338?w=800");
            auction3.setSeller(user1);
            auction3.setStatus(Auction.AuctionStatus.ACTIVE);
            auctionRepository.save(auction3);
        }
    }
}




