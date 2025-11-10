# Online Auction System

A modern, full-stack online auction platform built with Java Spring Boot and a beautiful frontend.

## Features

- 🎨 **Beautiful Modern UI** - Eye-catching design with gradients, animations, and responsive layout
- 🔐 **User Authentication** - Register and login system
- 🏛️ **Auction Management** - Create, view, and manage auctions
- 💰 **Real-time Bidding** - Place bids on active auctions
- 🔍 **Search & Filter** - Search auctions and filter by status
- 📊 **Bid History** - View complete bidding history for each auction
- ⏰ **Time Tracking** - Real-time countdown for auction end times

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **RESTful APIs**

### Frontend
- **HTML5**
- **CSS3** (Modern gradients, animations)
- **Vanilla JavaScript**
- **Responsive Design**

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Installation & Running

1. **Navigate to the project directory:**
   ```bash
   cd /Users/priyanshdhingra/Desktop/Java
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   - Open your browser and go to: `http://localhost:8080`
   - H2 Console (for database inspection): `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:auctiondb`
     - Username: `sa`
     - Password: (leave empty)

## Sample Data

The application comes with sample data:
- **Users:**
  - `john_doe` / `password123`
  - `jane_smith` / `password123`
- **Sample Auctions:** 3 pre-loaded auctions for testing

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/session/{sessionId}` - Check session validity

### Auctions
- `GET /api/auctions` - Get all auctions (supports `?status=active` and `?search=query`)
- `GET /api/auctions/{id}` - Get auction by ID
- `POST /api/auctions` - Create new auction (requires session)
- `GET /api/auctions/{id}/bids` - Get bids for an auction
- `POST /api/auctions/{id}/bid` - Place a bid (requires session)
- `GET /api/auctions/seller/{sellerId}` - Get auctions by seller

## Usage

1. **Register/Login:** Click "Register" or "Login" to create an account or sign in
2. **Browse Auctions:** View all active auctions on the home page
3. **Search:** Use the search bar to find specific auctions
4. **Create Auction:** Click "Create Auction" to list a new item
5. **Place Bids:** Click on any auction card to view details and place bids
6. **View My Auctions:** Click "My Auctions" to see auctions you've created

## Project Structure

```
Java/
├── src/
│   ├── main/
│   │   ├── java/com/auction/
│   │   │   ├── model/          # Entity models (User, Auction, Bid)
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── service/        # Business logic
│   │   │   ├── controller/     # REST controllers
│   │   │   ├── dto/            # Data transfer objects
│   │   │   └── config/         # Configuration classes
│   │   └── resources/
│   │       ├── static/         # Frontend files (HTML, CSS, JS)
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Features in Detail

### Frontend
- **Hero Section** with animated waves
- **Card-based Layout** for auctions
- **Modal Dialogs** for forms and details
- **Real-time Updates** for auction countdowns
- **Smooth Animations** and transitions
- **Responsive Design** for mobile and desktop

### Backend
- **RESTful API** design
- **Session Management** (in-memory for demo)
- **Transaction Management** for data consistency
- **Data Validation** and error handling
- **CORS Configuration** for frontend access

## Notes

- Session management is simplified for demo purposes. In production, use proper JWT tokens or Spring Security.
- Passwords are stored in plain text for demo. Use password hashing (BCrypt) in production.
- H2 database is in-memory and data is lost on restart. Use a persistent database for production.

## License

This project is open source and available for educational purposes.




