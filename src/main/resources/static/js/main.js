// Main JavaScript file for Apistry

// Global variables
let currentUser = null;

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

// Initialize the application
function initializeApp() {
    // Check if we're on the homepage (public) or dashboard (authenticated)
    const isHomepage = document.querySelector('.hero') !== null;
    const isDashboard = document.querySelector('.main-container') !== null;
    
    if (isHomepage) {
        initializeHomepage();
    } else if (isDashboard) {
        initializeDashboard();
    }
    
    // Initialize common functionality
    initializeCommon();
}

// Initialize homepage functionality
function initializeHomepage() {
    console.log('Initializing homepage...');
    
    // Add smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
    
    // Add animation on scroll for feature cards
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Observe feature cards and action cards
    document.querySelectorAll('.feature-card, .action-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(card);
    });
}

// Initialize dashboard functionality
function initializeDashboard() {
    console.log('Initializing dashboard...');
    
    // Initialize sidebar functionality
    initializeSidebar();
    
    // Initialize modals
    initializeModals();
    
    // Initialize quick actions
    initializeQuickActions();
}

// Initialize common functionality
function initializeCommon() {
    // Initialize notifications
    initializeNotifications();
    
    // Initialize mobile menu toggle
    initializeMobileMenu();
    
    // Initialize theme
    initializeTheme();
}

// Initialize sidebar functionality
function initializeSidebar() {
    const sidebar = document.querySelector('.sidebar');
    if (!sidebar) return;
    
    // Add workspace button functionality
    const addWorkspaceBtn = sidebar.querySelector('[title="Add Workspace"]');
    if (addWorkspaceBtn) {
        addWorkspaceBtn.addEventListener('click', () => {
            showNotification('Create Workspace functionality coming soon!', 'info');
        });
    }
    
    // Add collection button functionality
    const addCollectionBtn = sidebar.querySelector('[title="Add Collection"]');
    if (addCollectionBtn) {
        addCollectionBtn.addEventListener('click', () => {
            showNotification('Create Collection functionality coming soon!', 'info');
        });
    }
    
    // Clear history button functionality
    const clearHistoryBtn = sidebar.querySelector('[title="Clear History"]');
    if (clearHistoryBtn) {
        clearHistoryBtn.addEventListener('click', () => {
            if (!confirm('Are you sure you want to clear all history?')) {
                return;
            }

            fetch('/requests/history', { method: 'DELETE' })
                .then(async res => {
                    const text = await res.text().catch(() => '');
                    if (!res.ok) {
                        console.error('Clear history failed, status:', res.status, text);
                        throw new Error(text || 'Failed to clear history');
                    }
                    showNotification('History cleared successfully!', 'success');
                    setTimeout(() => window.location.reload(), 500);
                })
                .catch(err => {
                    console.error('Error clearing history', err);
                    showNotification('Failed to clear history', 'error');
                });
        });
    }

    // Clear saved button functionality
    const clearSavedBtn = sidebar.querySelector('[title="Clear Saved"]');
    if (clearSavedBtn) {
        clearSavedBtn.addEventListener('click', () => {
            if (!confirm('Are you sure you want to clear all saved requests?')) {
                return;
            }

            fetch('/requests/saved', { method: 'DELETE' })
                .then(async res => {
                    const text = await res.text().catch(() => '');
                    if (!res.ok) {
                        console.error('Clear saved failed, status:', res.status, text);
                        throw new Error(text || 'Failed to clear saved requests');
                    }
                    showNotification('Saved requests cleared successfully!', 'success');
                    setTimeout(() => window.location.reload(), 500);
                })
                .catch(err => {
                    console.error('Error clearing saved requests', err);
                    showNotification('Failed to clear saved requests', 'error');
                });
        });
    }
    
    // Create workspace button functionality
    const createWorkspaceBtn = sidebar.querySelector('.sidebar-empty .btn-primary');
    if (createWorkspaceBtn) {
        createWorkspaceBtn.addEventListener('click', () => {
            showNotification('Create Workspace functionality coming soon!', 'info');
        });
    }
    
    // Create collection button functionality
    const createCollectionBtn = sidebar.querySelectorAll('.sidebar-empty .btn-primary')[1];
    if (createCollectionBtn) {
        createCollectionBtn.addEventListener('click', () => {
            showNotification('Create Collection functionality coming soon!', 'info');
        });
    }
}

// Initialize modals
function initializeModals() {
    // Modal functionality
    const modals = document.querySelectorAll('.modal');
    
    modals.forEach(modal => {
        const closeButtons = modal.querySelectorAll('.modal-close');
        const modalContent = modal.querySelector('.modal-content');
        
        // Close modal when clicking close buttons
        closeButtons.forEach(button => {
            button.addEventListener('click', () => {
                closeModal(modal);
            });
        });
        
        // Close modal when clicking outside
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeModal(modal);
            }
        });
        
        // Close modal with Escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal.classList.contains('show')) {
                closeModal(modal);
            }
        });
        
        // Prevent modal content clicks from closing modal
        modalContent.addEventListener('click', (e) => {
            e.stopPropagation();
        });
    });
}

// Initialize quick actions
function initializeQuickActions() {
    // Quick action buttons are regular links/buttons that already navigate
    // to working pages like /request/new, /collection/new, /workspace/new.
    // We intentionally do not override their behavior with "coming soon"
    // notifications now that the features are implemented.
}

// Initialize notifications
function initializeNotifications() {
    // Create notification container if it doesn't exist
    if (!document.querySelector('.notification-container')) {
        const notificationContainer = document.createElement('div');
        notificationContainer.className = 'notification-container';
        document.body.appendChild(notificationContainer);
    }
}

// Initialize mobile menu
function initializeMobileMenu() {
    const header = document.querySelector('.header');
    if (!header) return;
    
    // Add mobile menu toggle button for small screens
    if (window.innerWidth <= 768) {
        const mobileMenuBtn = document.createElement('button');
        mobileMenuBtn.className = 'mobile-menu-btn';
        mobileMenuBtn.innerHTML = '<i class="fas fa-bars"></i>';
        mobileMenuBtn.style.display = 'none';
        
        // Insert before the nav-menu
        const navMenu = header.querySelector('.nav-menu');
        if (navMenu) {
            navMenu.parentNode.insertBefore(mobileMenuBtn, navMenu);
            
            mobileMenuBtn.addEventListener('click', () => {
                navMenu.classList.toggle('show');
            });
        }
    }
}

// Show modal
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('show');
        document.body.style.overflow = 'hidden';
    }
}

// Close modal
function closeModal(modal) {
    modal.classList.remove('show');
    document.body.style.overflow = '';
}

// Show notification
function showNotification(message, type = 'info', duration = 5000) {
    const notificationContainer = document.querySelector('.notification-container');
    if (!notificationContainer) return;
    
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <span class="notification-message">${message}</span>
            <button class="notification-close">
                <i class="fas fa-times"></i>
            </button>
        </div>
    `;
    
    // Add notification to container
    notificationContainer.appendChild(notification);
    
    // Show notification with animation
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);
    
    // Auto-remove notification
    const autoRemove = setTimeout(() => {
        removeNotification(notification);
    }, duration);
    
    // Close button functionality
    const closeBtn = notification.querySelector('.notification-close');
    closeBtn.addEventListener('click', () => {
        clearTimeout(autoRemove);
        removeNotification(notification);
    });
    
    return notification;
}

// Remove notification
function removeNotification(notification) {
    notification.classList.remove('show');
    setTimeout(() => {
        if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
        }
    }, 300);
}

// Create request function (placeholder)
function createRequest() {
    const form = document.getElementById('newRequestForm');
    const formData = new FormData(form);
    
    // Get form values
    const method = formData.get('method');
    const url = formData.get('url');
    const name = formData.get('name');
    
    // Validate form
    if (!method || !url) {
        showNotification('Please fill in all required fields', 'error');
        return;
    }
    
    // TODO: Implement actual request creation
    showNotification('Request created successfully!', 'success');
    
    // Close modal
    const modal = document.getElementById('newRequestModal');
    closeModal(modal);
    
    // Reset form
    form.reset();
}

// Utility functions
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Handle window resize
window.addEventListener('resize', debounce(() => {
    // Reinitialize mobile menu on resize
    initializeMobileMenu();
}, 250));

// Theme functionality
function initializeTheme() {
    // Load saved theme from localStorage
    const savedTheme = localStorage.getItem('apistry-theme');
    if (savedTheme) {
        document.documentElement.setAttribute('data-theme', savedTheme);
        updateThemeIcon(savedTheme);
    }
}

function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    
    // Apply new theme
    document.documentElement.setAttribute('data-theme', newTheme);
    
    // Save to localStorage
    localStorage.setItem('apistry-theme', newTheme);
    
    // Update icon
    updateThemeIcon(newTheme);
    
    // Show notification
    showNotification(`Switched to ${newTheme} theme`, 'success');
}

function updateThemeIcon(theme) {
    const themeToggle = document.querySelector('.theme-toggle i');
    if (themeToggle) {
        themeToggle.className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    }
}

// Export functions for global access
window.Apistry = {
    showNotification,
    showModal,
    closeModal,
    createRequest,
    toggleTheme
};
