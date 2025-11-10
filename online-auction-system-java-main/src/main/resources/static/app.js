// API Base URL
const API_BASE = `${window.location.origin}/api`;

// Global State
let currentUser = null;
let sessionId = null;
let currentFilter = 'all';

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
    checkSession();
    loadAuctions();
    setupEventListeners();
    setInterval(updateAuctionTimes, 1000);
});

// Event Listeners
function setupEventListeners() {
    // Navigation
    document.getElementById('homeBtn').addEventListener('click', () => {
        currentFilter = 'all';
        loadAuctions();
        updateActiveNav('homeBtn');
    });
    
    document.getElementById('myAuctionsBtn').addEventListener('click', () => {
        if (!currentUser) {
            showModal('loginModal');
            return;
        }
        currentFilter = 'my';
        loadMyAuctions();
        updateActiveNav('myAuctionsBtn');
    });
    
    document.getElementById('createAuctionBtn').addEventListener('click', () => {
        if (!currentUser) {
            showModal('loginModal');
            return;
        }
        showModal('createAuctionModal');
    });
    
    // Auth Buttons
    document.getElementById('loginBtn').addEventListener('click', () => showModal('loginModal'));
    document.getElementById('registerBtn').addEventListener('click', () => showModal('registerModal'));
    document.getElementById('logoutBtn').addEventListener('click', logout);
    
    // Forms
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    document.getElementById('createAuctionForm').addEventListener('submit', handleCreateAuction);
    
    // Search
    document.getElementById('searchBtn').addEventListener('click', handleSearch);
    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') handleSearch();
    });
    
    // Filter Tabs
    document.querySelectorAll('.filter-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            currentFilter = tab.dataset.filter;
            document.querySelectorAll('.filter-tab').forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            
            if (currentFilter === 'active') {
                loadAuctions('active');
            } else if (currentFilter === 'my') {
                if (!currentUser) {
                    showModal('loginModal');
                    return;
                }
                loadMyAuctions();
            } else {
                loadAuctions();
            }
        });
    });
    
    // Modal Close
    document.querySelectorAll('.close').forEach(closeBtn => {
        closeBtn.addEventListener('click', (e) => {
            e.target.closest('.modal').style.display = 'none';
        });
    });
    
    // Close modal on outside click
    window.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal')) {
            e.target.style.display = 'none';
        }
    });
}

// Session Management
async function checkSession() {
    const storedSession = localStorage.getItem('sessionId');
    if (storedSession) {
        try {
            const response = await fetch(`${API_BASE}/auth/session/${storedSession}`);
            const data = await response.json();
            if (data.valid) {
                sessionId = storedSession;
                currentUser = data.user;
                updateUI();
            } else {
                localStorage.removeItem('sessionId');
            }
        } catch (error) {
            console.error('Session check failed:', error);
        }
    }
}

// Authentication
async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    const errorEl = document.getElementById('loginError');
    
    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            sessionId = data.sessionId;
            currentUser = data.user;
            localStorage.setItem('sessionId', sessionId);
            updateUI();
            hideModal('loginModal');
            document.getElementById('loginForm').reset();
            errorEl.textContent = '';
            showNotification('Login successful!', 'success');
        } else {
            errorEl.textContent = data.message || 'Login failed';
        }
    } catch (error) {
        errorEl.textContent = 'Connection error. Please try again.';
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const fullName = document.getElementById('registerFullName').value;
    const username = document.getElementById('registerUsername').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    const errorEl = document.getElementById('registerError');
    
    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ fullName, username, email, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            errorEl.textContent = '';
            document.getElementById('registerForm').reset();
            hideModal('registerModal');
            showNotification('Registration successful! Please login.', 'success');
            setTimeout(() => showModal('loginModal'), 500);
        } else {
            errorEl.textContent = data.message || 'Registration failed';
        }
    } catch (error) {
        errorEl.textContent = 'Connection error. Please try again.';
    }
}

function logout() {
    sessionId = null;
    currentUser = null;
    localStorage.removeItem('sessionId');
    updateUI();
    showNotification('Logged out successfully', 'success');
    loadAuctions();
}

function updateUI() {
    const authButtons = document.getElementById('authButtons');
    const userMenu = document.getElementById('userMenu');
    
    if (currentUser) {
        authButtons.style.display = 'none';
        userMenu.style.display = 'flex';
        document.getElementById('userName').textContent = currentUser.username;
    } else {
        authButtons.style.display = 'flex';
        userMenu.style.display = 'none';
    }
}

// Auctions
async function loadAuctions(status = null) {
    showLoading(true);
    try {
        const url = status ? `${API_BASE}/auctions?status=${status}` : `${API_BASE}/auctions`;
        const response = await fetch(url);
        const auctions = await response.json();
        displayAuctions(auctions);
    } catch (error) {
        console.error('Error loading auctions:', error);
        showNotification('Failed to load auctions', 'error');
    } finally {
        showLoading(false);
    }
}

async function loadMyAuctions() {
    if (!currentUser) return;
    
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/auctions/seller/${currentUser.id}`);
        const auctions = await response.json();
        displayAuctions(auctions);
    } catch (error) {
        console.error('Error loading my auctions:', error);
        showNotification('Failed to load your auctions', 'error');
    } finally {
        showLoading(false);
    }
}

async function handleSearch() {
    const query = document.getElementById('searchInput').value.trim();
    if (!query) {
        loadAuctions();
        return;
    }
    
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/auctions?search=${encodeURIComponent(query)}`);
        const auctions = await response.json();
        displayAuctions(auctions);
    } catch (error) {
        console.error('Search error:', error);
        showNotification('Search failed', 'error');
    } finally {
        showLoading(false);
    }
}

function displayAuctions(auctions) {
    const container = document.getElementById('auctionsContainer');
    
    if (auctions.length === 0) {
        container.innerHTML = '<div style="text-align: center; padding: 3rem; color: var(--gray);">No auctions found</div>';
        return;
    }
    
    container.innerHTML = auctions.map(auction => createAuctionCard(auction)).join('');
    
    // Add click listeners
    container.querySelectorAll('.auction-card').forEach(card => {
        const auctionId = card.getAttribute('data-id');
        card.addEventListener('click', () => showAuctionDetail(auctionId));
    });
}

function createAuctionCard(auction) {
    const timeRemaining = getTimeRemaining(auction.endTime);
    const status = new Date(auction.endTime) > new Date() ? 'ACTIVE' : 'ENDED';
    const statusClass = status === 'ACTIVE' ? 'status-active' : 'status-ended';
    
    return `
        <div class="auction-card" data-id="${auction.id}">
            <img src="${auction.imageUrl || 'https://via.placeholder.com/400x220?text=' + encodeURIComponent(auction.title)}" 
                 alt="${auction.title}" 
                 class="auction-image"
                 onerror="this.src='https://via.placeholder.com/400x220?text=No+Image'">
            <div class="auction-content">
                <h3 class="auction-title">${auction.title}</h3>
                <p class="auction-description">${auction.description || 'No description'}</p>
                <div class="auction-price">$${parseFloat(auction.currentPrice).toFixed(2)}</div>
                <div class="auction-meta">
                    <span class="auction-status ${statusClass}">${status}</span>
                    <span>${timeRemaining}</span>
                </div>
            </div>
        </div>
    `;
}

async function showAuctionDetail(auctionId) {
    try {
        const [auctionRes, bidsRes] = await Promise.all([
            fetch(`${API_BASE}/auctions/${auctionId}`),
            fetch(`${API_BASE}/auctions/${auctionId}/bids`)
        ]);
        
        const auction = await auctionRes.json();
        const bids = await bidsRes.json();
        
        const modal = document.getElementById('auctionDetailModal');
        const content = document.getElementById('auctionDetailContent');
        
        const timeRemaining = getTimeRemaining(auction.endTime);
        const status = new Date(auction.endTime) > new Date() ? 'ACTIVE' : 'ENDED';
        const canBid = status === 'ACTIVE' && currentUser && auction.seller.id !== currentUser.id;
        
        content.innerHTML = `
            <div class="auction-detail">
                <div>
                    <img src="${auction.imageUrl || 'https://via.placeholder.com/600x400?text=' + encodeURIComponent(auction.title)}" 
                         alt="${auction.title}" 
                         class="auction-detail-image"
                         onerror="this.src='https://via.placeholder.com/600x400?text=No+Image'">
                </div>
                <div>
                    <h2>${auction.title}</h2>
                    <p style="color: var(--gray); margin: 1rem 0;">${auction.description || 'No description'}</p>
                    <div style="margin: 2rem 0;">
                        <div style="font-size: 2.5rem; font-weight: 800; background: var(--gradient-1); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">
                            $${parseFloat(auction.currentPrice).toFixed(2)}
                        </div>
                        <div style="color: var(--gray); margin-top: 0.5rem;">
                            Starting: $${parseFloat(auction.startingPrice).toFixed(2)}
                        </div>
                    </div>
                    <div style="margin: 1rem 0;">
                        <strong>Seller:</strong> ${auction.seller.username}
                    </div>
                    <div style="margin: 1rem 0;">
                        <strong>Ends:</strong> ${new Date(auction.endTime).toLocaleString()}
                    </div>
                    <div style="margin: 1rem 0;">
                        <strong>Time Remaining:</strong> <span id="detailTimeRemaining">${timeRemaining}</span>
                    </div>
                    <div style="margin: 1rem 0;">
                        <strong>Bids:</strong> ${bids.length}
                    </div>
                    
                    ${canBid ? `
                        <div class="bid-section">
                            <h3>Place a Bid</h3>
                            <div class="bid-form">
                                <input type="number" 
                                       id="bidAmount" 
                                       class="bid-input" 
                                       step="0.01" 
                                       min="${parseFloat(auction.currentPrice) + 0.01}" 
                                       placeholder="Enter bid amount"
                                       value="${(parseFloat(auction.currentPrice) + 1).toFixed(2)}">
                                <button class="btn-primary" onclick="placeBid(${auction.id})">Place Bid</button>
                            </div>
                            <p id="bidError" class="error-message"></p>
                        </div>
                    ` : status === 'ENDED' ? '<p style="color: var(--danger); margin-top: 1rem;">This auction has ended</p>' : 
                      !currentUser ? '<p style="color: var(--gray); margin-top: 1rem;">Please login to place a bid</p>' : 
                      '<p style="color: var(--gray); margin-top: 1rem;">You cannot bid on your own auction</p>'}
                    
                    <div class="bids-list">
                        <h3 style="margin-bottom: 1rem;">Bid History</h3>
                        ${bids.length > 0 ? bids.map(bid => `
                            <div class="bid-item">
                                <div>
                                    <strong>${bid.bidder.username}</strong>
                                    <div style="font-size: 0.85rem; color: var(--gray);">${new Date(bid.bidTime).toLocaleString()}</div>
                                </div>
                                <div class="bid-amount">$${parseFloat(bid.amount).toFixed(2)}</div>
                            </div>
                        `).join('') : '<p style="color: var(--gray);">No bids yet</p>'}
                    </div>
                </div>
            </div>
        `;
        
        showModal('auctionDetailModal');
        
        // Update time remaining
        const interval = setInterval(() => {
            const timeEl = document.getElementById('detailTimeRemaining');
            if (timeEl) {
                timeEl.textContent = getTimeRemaining(auction.endTime);
            } else {
                clearInterval(interval);
            }
        }, 1000);
        
    } catch (error) {
        console.error('Error loading auction detail:', error);
        showNotification('Failed to load auction details', 'error');
    }
}

async function placeBid(auctionId) {
    if (!currentUser) {
        showNotification('Please login to place a bid', 'error');
        return;
    }
    
    const amount = parseFloat(document.getElementById('bidAmount').value);
    const errorEl = document.getElementById('bidError');
    
    if (!amount || amount <= 0) {
        errorEl.textContent = 'Please enter a valid bid amount';
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/auctions/${auctionId}/bid`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Session-Id': sessionId
            },
            body: JSON.stringify({ auctionId, amount })
        });
        
        const data = await response.json();
        
        if (data.success) {
            errorEl.textContent = '';
            showNotification('Bid placed successfully!', 'success');
            setTimeout(() => {
                hideModal('auctionDetailModal');
                loadAuctions();
            }, 1000);
        } else {
            errorEl.textContent = data.message || 'Failed to place bid';
        }
    } catch (error) {
        errorEl.textContent = 'Connection error. Please try again.';
    }
}

// Create Auction
async function handleCreateAuction(e) {
    e.preventDefault();
    if (!currentUser) {
        showNotification('Please login to create an auction', 'error');
        return;
    }
    
    const title = document.getElementById('auctionTitle').value;
    const description = document.getElementById('auctionDescription').value;
    const startingPrice = parseFloat(document.getElementById('auctionStartingPrice').value);
    const endTime = document.getElementById('auctionEndTime').value;
    const imageUrl = document.getElementById('auctionImageUrl').value;
    const errorEl = document.getElementById('createAuctionError');
    
    const auction = {
        title,
        description,
        startingPrice,
        startTime: new Date().toISOString(),
        endTime: new Date(endTime).toISOString(),
        imageUrl: imageUrl || null
    };
    
    try {
        const response = await fetch(`${API_BASE}/auctions`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Session-Id': sessionId
            },
            body: JSON.stringify(auction)
        });
        
        const data = await response.json();
        
        if (data.success) {
            errorEl.textContent = '';
            document.getElementById('createAuctionForm').reset();
            hideModal('createAuctionModal');
            showNotification('Auction created successfully!', 'success');
            loadAuctions();
        } else {
            errorEl.textContent = data.message || 'Failed to create auction';
        }
    } catch (error) {
        errorEl.textContent = 'Connection error. Please try again.';
    }
}

// Utility Functions
function showModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function hideModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function showLoading(show) {
    document.getElementById('loadingSpinner').style.display = show ? 'flex' : 'none';
}

function updateActiveNav(activeId) {
    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
    document.getElementById(activeId)?.classList.add('active');
}

function getTimeRemaining(endTime) {
    const now = new Date();
    const end = new Date(endTime);
    const diff = end - now;
    
    if (diff <= 0) return 'Ended';
    
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((diff % (1000 * 60)) / 1000);
    
    if (days > 0) return `${days}d ${hours}h`;
    if (hours > 0) return `${hours}h ${minutes}m`;
    return `${minutes}m ${seconds}s`;
}

function updateAuctionTimes() {
    document.querySelectorAll('.auction-meta span:last-child').forEach(el => {
        const card = el.closest('.auction-card');
        if (card) {
            const auctionId = card.dataset.id;
            // This would ideally fetch updated time, but for now we'll skip
        }
    });
}

function scrollToAuctions() {
    document.querySelector('.main-content').scrollIntoView({ behavior: 'smooth' });
}

function showNotification(message, type = 'info') {
    // Simple notification - could be enhanced with a toast library
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 100px;
        right: 20px;
        background: ${type === 'success' ? 'var(--success)' : type === 'error' ? 'var(--danger)' : 'var(--primary)'};
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 10px;
        box-shadow: var(--shadow-lg);
        z-index: 3000;
        animation: slideDown 0.3s ease;
    `;
    notification.textContent = message;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'fadeIn 0.3s ease reverse';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// Make placeBid available globally
window.placeBid = placeBid; 