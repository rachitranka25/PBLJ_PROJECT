# Backend & Database Details Guide

This guide shows you how to view and inspect your backend API and PostgreSQL database.

## 📊 Database Details

### Connect to PostgreSQL Database

```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb
```

Or if PostgreSQL is in your PATH:
```bash
psql -d auctiondb
```

### View Database Schema

#### List all tables:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "\dt"
```

#### View table structure:
```bash
# Users table
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "\d+ users"

# Auctions table
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "\d+ auctions"

# Bids table
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "\d+ bids"
```

#### View all database objects:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "\d"
```

### Query Database Data

#### View all users:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT * FROM users;"
```

#### View all auctions:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT id, title, current_price, status, seller_id FROM auctions;"
```

#### View all bids:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT * FROM bids;"
```

#### View auctions with seller details:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT a.id, a.title, a.current_price, u.username as seller FROM auctions a JOIN users u ON a.seller_id = u.id;"
```

#### Count records:
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT 'users' as table_name, COUNT(*) FROM users UNION SELECT 'auctions', COUNT(*) FROM auctions UNION SELECT 'bids', COUNT(*) FROM bids;"
```

### Database Connection Info

- **Host**: localhost
- **Port**: 5432
- **Database**: auctiondb
- **Username**: priyanshdhingra (your system user)
- **Password**: (empty by default)

## 🔌 Backend API Details

### Base URL
```
http://localhost:8080
```

### API Endpoints

#### Authentication Endpoints

**Register User:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "email": "test@example.com",
    "password": "password123",
    "fullName": "Test User"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

#### Auction Endpoints

**Get All Auctions:**
```bash
curl http://localhost:8080/api/auctions
```

**Get Active Auctions Only:**
```bash
curl http://localhost:8080/api/auctions?status=active
```

**Search Auctions:**
```bash
curl "http://localhost:8080/api/auctions?search=watch"
```

**Get Auction by ID:**
```bash
curl http://localhost:8080/api/auctions/1
```

**Get Bids for Auction:**
```bash
curl http://localhost:8080/api/auctions/1/bids
```

**Place a Bid:**
```bash
curl -X POST http://localhost:8080/api/auctions/1/bid \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "your-session-id",
    "amount": 5500.00
  }'
```

**Get Auctions by Seller:**
```bash
curl http://localhost:8080/api/auctions/seller/1
```

### View API Response (Formatted)

```bash
# Pretty print JSON
curl -s http://localhost:8080/api/auctions | python3 -m json.tool

# Or using jq (if installed)
curl -s http://localhost:8080/api/auctions | jq
```

### Test API Endpoints

```bash
# Test if API is running
curl http://localhost:8080/api/auctions

# Get specific auction
curl http://localhost:8080/api/auctions/1

# Get all users (if endpoint exists)
curl http://localhost:8080/api/users
```

## 📝 View Backend Logs

### If running with Maven:
```bash
# Logs are shown in the terminal where you ran: mvn spring-boot:run
```

### If running as JAR:
```bash
# Logs are shown in the terminal where you ran: java -jar app.jar
```

### Check if application is running:
```bash
ps aux | grep "AuctionApplication" | grep -v grep
```

### View application logs (if redirected to file):
```bash
tail -f logs/application.log
```

## 🗄️ Database Management Commands

### Backup Database
```bash
/opt/homebrew/opt/postgresql@15/bin/pg_dump -d auctiondb > backup.sql
```

### Restore Database
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb < backup.sql
```

### View Database Size
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT pg_size_pretty(pg_database_size('auctiondb'));"
```

### View Table Sizes
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size FROM pg_tables WHERE schemaname = 'public' ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;"
```

### View Database Statistics
```bash
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT schemaname, tablename, n_live_tup as row_count FROM pg_stat_user_tables ORDER BY n_live_tup DESC;"
```

## 🔍 Quick Reference Commands

### Database Quick Access
```bash
# Interactive PostgreSQL session
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb

# Then you can run SQL queries:
# SELECT * FROM users;
# SELECT * FROM auctions;
# \q to quit
```

### API Quick Test
```bash
# Check if backend is running
curl http://localhost:8080/api/auctions

# Get formatted JSON
curl -s http://localhost:8080/api/auctions | python3 -m json.tool | head -50
```

### View All Data at Once
```bash
echo "=== USERS ===" && \
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT * FROM users;" && \
echo -e "\n=== AUCTIONS ===" && \
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT id, title, current_price, status FROM auctions;" && \
echo -e "\n=== BIDS ===" && \
/opt/homebrew/opt/postgresql@15/bin/psql -d auctiondb -c "SELECT * FROM bids;"
```

## 📊 Database Schema Summary

### Users Table
- `id` (bigint, primary key)
- `username` (varchar, unique)
- `email` (varchar, unique)
- `password` (varchar)
- `full_name` (varchar)
- `created_at` (timestamp)

### Auctions Table
- `id` (bigint, primary key)
- `title` (varchar)
- `description` (varchar)
- `starting_price` (numeric)
- `current_price` (numeric)
- `start_time` (timestamp)
- `end_time` (timestamp)
- `status` (varchar: ACTIVE, ENDED, CANCELLED)
- `image_url` (varchar)
- `seller_id` (bigint, foreign key → users.id)
- `current_winner_id` (bigint, foreign key → users.id)
- `created_at` (timestamp)

### Bids Table
- `id` (bigint, primary key)
- `amount` (numeric)
- `bid_time` (timestamp)
- `auction_id` (bigint, foreign key → auctions.id)
- `bidder_id` (bigint, foreign key → users.id)

## 🌐 Web Interface

- **Application**: http://localhost:8080
- **API Base**: http://localhost:8080/api
- **H2 Console**: Disabled (using PostgreSQL)

## 🛠️ Useful SQL Queries

### Find auctions ending soon:
```sql
SELECT id, title, end_time, current_price 
FROM auctions 
WHERE status = 'ACTIVE' 
ORDER BY end_time ASC 
LIMIT 5;
```

### Find highest bids:
```sql
SELECT a.title, b.amount, u.username as bidder, b.bid_time
FROM bids b
JOIN auctions a ON b.auction_id = a.id
JOIN users u ON b.bidder_id = u.id
ORDER BY b.amount DESC
LIMIT 10;
```

### User activity summary:
```sql
SELECT 
  u.username,
  COUNT(DISTINCT a.id) as auctions_created,
  COUNT(DISTINCT b.id) as bids_placed
FROM users u
LEFT JOIN auctions a ON u.id = a.seller_id
LEFT JOIN bids b ON u.id = b.bidder_id
GROUP BY u.id, u.username;
```

