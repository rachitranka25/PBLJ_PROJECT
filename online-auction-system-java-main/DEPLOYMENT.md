# Deployment Guide

This guide explains how to deploy the Online Auction System with PostgreSQL database.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL 12+ (if deploying without Docker)

## Deployment Options

### Option 1: Docker Compose (Recommended - Easiest)

This is the simplest way to deploy the entire application with PostgreSQL.

#### Steps:

1. **Build and start all services:**
   ```bash
   docker-compose up -d
   ```

2. **Check logs:**
   ```bash
   docker-compose logs -f app
   ```

3. **Access the application:**
   - Application: http://localhost:8080
   - PostgreSQL: localhost:5432

4. **Stop services:**
   ```bash
   docker-compose down
   ```

5. **Stop and remove volumes (clean database):**
   ```bash
   docker-compose down -v
   ```

#### Environment Variables

You can customize the deployment by creating a `.env` file or setting environment variables:

```bash
# Database Configuration
DB_HOST=postgres
DB_PORT=5432
DB_NAME=auctiondb
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# Application Configuration
PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### Option 2: Manual Deployment with Docker

#### Step 1: Start PostgreSQL Container

```bash
docker run -d \
  --name auction-postgres \
  -e POSTGRES_DB=auctiondb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:15-alpine
```

#### Step 2: Build Application Docker Image

```bash
docker build -t auction-app:latest .
```

#### Step 3: Run Application Container

```bash
docker run -d \
  --name auction-app \
  --link auction-postgres:postgres \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=auctiondb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  -p 8080:8080 \
  auction-app:latest
```

### Option 3: Traditional Deployment (Without Docker)

#### Step 1: Install and Configure PostgreSQL

1. **Install PostgreSQL** (if not already installed):
   ```bash
   # macOS
   brew install postgresql@15
   brew services start postgresql@15

   # Ubuntu/Debian
   sudo apt-get update
   sudo apt-get install postgresql postgresql-contrib

   # Start PostgreSQL
   sudo systemctl start postgresql
   sudo systemctl enable postgresql
   ```

2. **Create Database and User:**
   ```bash
   sudo -u postgres psql
   ```
   
   Then in PostgreSQL shell:
   ```sql
   CREATE DATABASE auctiondb;
   CREATE USER auction_user WITH PASSWORD 'your_secure_password';
   GRANT ALL PRIVILEGES ON DATABASE auctiondb TO auction_user;
   \q
   ```

#### Step 2: Build the Application

```bash
mvn clean package -DskipTests
```

This creates a JAR file in `target/online-auction-system-1.0.0.jar`

#### Step 3: Run with Production Profile

```bash
java -jar target/online-auction-system-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/auctiondb \
  --spring.datasource.username=auction_user \
  --spring.datasource.password=your_secure_password
```

Or set environment variables:

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=auctiondb
export DB_USERNAME=auction_user
export DB_PASSWORD=your_secure_password

java -jar target/online-auction-system-1.0.0.jar
```

### Option 4: Cloud Deployment

#### Heroku

1. **Install Heroku CLI** and login:
   ```bash
   heroku login
   ```

2. **Create Heroku app:**
   ```bash
   heroku create your-app-name
   ```

3. **Add PostgreSQL addon:**
   ```bash
   heroku addons:create heroku-postgresql:hobby-dev
   ```

4. **Set production profile:**
   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=prod
   ```

5. **Deploy:**
   ```bash
   git push heroku main
   ```

#### AWS Elastic Beanstalk

1. **Install EB CLI:**
   ```bash
   pip install awsebcli
   ```

2. **Initialize EB:**
   ```bash
   eb init
   eb create auction-env
   ```

3. **Configure environment variables:**
   ```bash
   eb setenv SPRING_PROFILES_ACTIVE=prod \
     DB_HOST=your-rds-endpoint \
     DB_NAME=auctiondb \
     DB_USERNAME=your-username \
     DB_PASSWORD=your-password
   ```

4. **Deploy:**
   ```bash
   eb deploy
   ```

#### Google Cloud Run

1. **Build and push to Container Registry:**
   ```bash
   gcloud builds submit --tag gcr.io/PROJECT-ID/auction-app
   ```

2. **Deploy to Cloud Run:**
   ```bash
   gcloud run deploy auction-app \
     --image gcr.io/PROJECT-ID/auction-app \
     --platform managed \
     --region us-central1 \
     --set-env-vars SPRING_PROFILES_ACTIVE=prod,DB_HOST=your-cloud-sql-ip
   ```

## Database Migration

The application uses `spring.jpa.hibernate.ddl-auto=update`, which automatically creates/updates database schema on startup. For production, consider:

1. **Using Flyway or Liquibase** for version-controlled migrations
2. **Setting `ddl-auto=validate`** in production to prevent accidental schema changes

## Security Considerations

### Production Checklist

- [ ] Change default database passwords
- [ ] Use environment variables for sensitive data (never commit passwords)
- [ ] Enable HTTPS/SSL
- [ ] Configure proper CORS settings
- [ ] Implement proper authentication (JWT tokens)
- [ ] Hash passwords using BCrypt
- [ ] Set up database backups
- [ ] Configure firewall rules
- [ ] Use connection pooling (already configured)
- [ ] Set up monitoring and logging
- [ ] Configure proper session management

### Environment Variables Template

Create a `.env` file (never commit this):

```bash
# Database
DB_HOST=your-db-host
DB_PORT=5432
DB_NAME=auctiondb
DB_USERNAME=your-username
DB_PASSWORD=your-secure-password

# Application
SPRING_PROFILES_ACTIVE=prod
PORT=8080

# Optional: JVM Settings
JAVA_OPTS=-Xmx512m -Xms256m
```

## Monitoring and Maintenance

### Health Checks

The application includes a health check endpoint. For Docker:
```bash
docker ps  # Check container status
docker logs auction-app  # View logs
```

### Database Backup

```bash
# Backup PostgreSQL database
docker exec auction-postgres pg_dump -U postgres auctiondb > backup.sql

# Restore
docker exec -i auction-postgres psql -U postgres auctiondb < backup.sql
```

### Logs

```bash
# Docker Compose logs
docker-compose logs -f app

# Individual container logs
docker logs -f auction-app
```

## Troubleshooting

### Application won't start

1. **Check database connection:**
   ```bash
   docker exec auction-postgres psql -U postgres -d auctiondb -c "SELECT 1;"
   ```

2. **Check application logs:**
   ```bash
   docker-compose logs app
   ```

3. **Verify environment variables:**
   ```bash
   docker exec auction-app env | grep DB
   ```

### Database connection errors

- Ensure PostgreSQL is running and accessible
- Verify database credentials
- Check network connectivity between app and database
- Ensure database exists

### Port conflicts

If port 8080 is already in use:
```bash
# Change port in docker-compose.yml or set PORT environment variable
PORT=8081 docker-compose up
```

## Performance Tuning

### Database Connection Pool

Adjust in `application-prod.properties`:
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=10
```

### JVM Settings

For production, set JVM options:
```bash
JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC" java -jar app.jar
```

## Support

For issues or questions, check:
- Application logs: `docker-compose logs app`
- Database logs: `docker-compose logs postgres`
- Spring Boot actuator endpoints (if enabled)

