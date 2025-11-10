# API Endpoints Reference

## Base URL
```
http://localhost:8080/api
```

## API Info Endpoint
**GET** `/api` - Get API information and list of all endpoints

Response:
```json
{
  "name": "Online Auction System API",
  "version": "1.0.0",
  "status": "running",
  "endpoints": {
    "authentication": { ... },
    "auctions": { ... }
  }
}
```

---

## 🔐 Authentication Endpoints

### Register User
**POST** `/api/auth/register`

Request Body:
```json
{
  "username": "test_user",
  "email": "test@example.com",
  "password": "password123",
  "fullName": "Test User"
}
```

Response:
```json
{
  "success": true,
  "message": "User registered successfully",
  "sessionId": "session-id-here"
}
```

**cURL Example:**
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

---

### Login
**POST** `/api/auth/login`

Request Body:
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

Response:
```json
{
  "success": true,
  "message": "Login successful",
  "sessionId": "session-id-here",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

---

### Check Session
**GET** `/api/auth/session/{sessionId}`

Response:
```json
{
  "valid": true,
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/auth/session/your-session-id
```

---

## 🏛️ Auction Endpoints

### Get All Auctions
**GET** `/api/auctions`

Query Parameters:
- `status` (optional): Filter by status (e.g., `active`)
- `search` (optional): Search query string

Response:
```json
[
  {
    "id": 1,
    "title": "Vintage Rolex Watch",
    "description": "Beautiful vintage Rolex watch...",
    "startingPrice": 5000.00,
    "currentPrice": 5000.00,
    "startTime": "2025-11-08T01:43:21.86775",
    "endTime": "2025-11-15T01:43:21.86776",
    "imageUrl": "https://...",
    "status": "ACTIVE",
    "seller": {
      "id": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "fullName": "John Doe"
    },
    "currentWinner": null,
    "bidCount": 0,
    "createdAt": "2025-11-10T01:43:21.868018"
  }
]
```

**cURL Examples:**
```bash
# Get all auctions
curl http://localhost:8080/api/auctions

# Get active auctions only
curl "http://localhost:8080/api/auctions?status=active"

# Search auctions
curl "http://localhost:8080/api/auctions?search=watch"
```

---

### Get Auction by ID
**GET** `/api/auctions/{id}`

**cURL Example:**
```bash
curl http://localhost:8080/api/auctions/1
```

---

### Create Auction
**POST** `/api/auctions`

**Headers:**
- `X-Session-Id`: Your session ID (required)

Request Body:
```json
{
  "title": "New Item",
  "description": "Item description",
  "startingPrice": 100.00,
  "startTime": "2025-11-10T10:00:00",
  "endTime": "2025-11-20T10:00:00",
  "imageUrl": "https://example.com/image.jpg"
}
```

Response:
```json
{
  "success": true,
  "auction": {
    "id": 4,
    "title": "New Item",
    ...
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auctions \
  -H "Content-Type: application/json" \
  -H "X-Session-Id: your-session-id" \
  -d '{
    "title": "New Item",
    "description": "Item description",
    "startingPrice": 100.00,
    "startTime": "2025-11-10T10:00:00",
    "endTime": "2025-11-20T10:00:00",
    "imageUrl": "https://example.com/image.jpg"
  }'
```

---

### Get Bids for Auction
**GET** `/api/auctions/{id}/bids`

**cURL Example:**
```bash
curl http://localhost:8080/api/auctions/1/bids
```

---

### Place a Bid
**POST** `/api/auctions/{id}/bid`

**Headers:**
- `X-Session-Id`: Your session ID (required)

Request Body:
```json
{
  "sessionId": "your-session-id",
  "amount": 5500.00
}
```

Response:
```json
{
  "success": true,
  "bid": {
    "id": 1,
    "amount": 5500.00,
    "bidTime": "2025-11-10T02:00:00",
    "bidder": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe"
    }
  },
  "message": "Bid placed successfully"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auctions/1/bid \
  -H "Content-Type: application/json" \
  -H "X-Session-Id: your-session-id" \
  -d '{
    "sessionId": "your-session-id",
    "amount": 5500.00
  }'
```

---

### Get Auctions by Seller
**GET** `/api/auctions/seller/{sellerId}`

**cURL Example:**
```bash
curl http://localhost:8080/api/auctions/seller/1
```

---

## 📝 Quick Test Commands

### Test API Info
```bash
curl http://localhost:8080/api | python3 -m json.tool
```

### Test Get All Auctions
```bash
curl http://localhost:8080/api/auctions | python3 -m json.tool
```

### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}' | python3 -m json.tool
```

### Test Get Auction by ID
```bash
curl http://localhost:8080/api/auctions/1 | python3 -m json.tool
```

---

## 🔒 Authentication

Most endpoints require authentication via session ID:
1. Register or Login to get a `sessionId`
2. Include the session ID in the `X-Session-Id` header for protected endpoints

**Protected Endpoints:**
- `POST /api/auctions` - Create auction
- `POST /api/auctions/{id}/bid` - Place bid

**Public Endpoints:**
- `GET /api/auctions` - Get all auctions
- `GET /api/auctions/{id}` - Get auction by ID
- `GET /api/auctions/{id}/bids` - Get bids
- `GET /api/auctions/seller/{sellerId}` - Get auctions by seller

---

## 📊 Response Status Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## 🌐 CORS

All endpoints support CORS and can be accessed from any origin.

---

## 💡 Tips

1. **Pretty Print JSON**: Use `python3 -m json.tool` or `jq` to format JSON responses
2. **Session Management**: Store the session ID from login/register responses
3. **Error Handling**: Check the `success` field in responses for error messages
4. **Testing**: Use Postman, Insomnia, or curl for testing endpoints

