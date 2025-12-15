// Collection Page JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeCollectionPage();
});

function initializeCollectionPage() {
    // Initialize environment selector
    initializeEnvironmentSelector();
    
    // Initialize animations
    addPageAnimations();
    
    // Initialize form validation
    initializeFormValidation();
}

// Environment selector functionality
function initializeEnvironmentSelector() {
    const environmentSelect = document.getElementById('environmentSelect');
    if (environmentSelect) {
        environmentSelect.addEventListener('change', function() {
            const selectedEnvironment = this.value;
            handleEnvironmentChange(selectedEnvironment);
        });
    }
}

function handleEnvironmentChange(environment) {
    console.log('Environment changed to:', environment);
    
    if (environment) {
        showNotification(`Switched to ${environment} environment`, 'info');
        // TODO: Load environment variables and update request URLs
    } else {
        showNotification('No environment selected', 'info');
    }
}

// Page animations
function addPageAnimations() {
    const elements = document.querySelectorAll('.collection-header, .requests-section, .collection-settings');
    elements.forEach((element, index) => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            element.style.transition = 'all 0.6s ease';
            element.style.opacity = '1';
            element.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

// Form validation
function initializeFormValidation() {
    // Add validation for collection settings
    const settingInputs = document.querySelectorAll('.setting-item input, .setting-item textarea');
    settingInputs.forEach(input => {
        input.addEventListener('blur', validateField);
        input.addEventListener('input', clearValidation);
    });
}

function validateField(event) {
    const field = event.target;
    const value = field.value.trim();
    
    if (field.hasAttribute('required') && !value) {
        showFieldError(field, 'This field is required');
    } else if (field.type === 'url' && value && !isValidUrl(value)) {
        showFieldError(field, 'Please enter a valid URL');
    } else {
        clearFieldError(field);
    }
}

function clearValidation(event) {
    clearFieldError(event.target);
}

function showFieldError(field, message) {
    clearFieldError(field);
    
    field.style.borderColor = '#dc3545';
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.textContent = message;
    errorDiv.style.color = '#dc3545';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '4px';
    
    field.parentNode.appendChild(errorDiv);
}

function clearFieldError(field) {
    field.style.borderColor = '#dee2e6';
    
    const errorDiv = field.parentNode.querySelector('.field-error');
    if (errorDiv) {
        errorDiv.remove();
    }
}

function isValidUrl(string) {
    try {
        new URL(string);
        return true;
    } catch (_) {
        return false;
    }
}

// Collection actions
function editCollection() {
    // TODO: Implement collection editing
    showNotification('Collection editing feature coming soon!', 'info');
}

function duplicateCollection() {
    // TODO: Implement collection duplication
    showNotification('Collection duplication feature coming soon!', 'info');
}

function shareCollection() {
    // TODO: Implement collection sharing
    showNotification('Collection sharing feature coming soon!', 'info');
}

function runCollection() {
    // TODO: Implement collection running
    showNotification('Collection running feature coming soon!', 'info');
}

function runAllRequests() {
    // TODO: Implement running all requests in collection
    showNotification('Running all requests...', 'info');
}

function bulkEdit() {
    // TODO: Implement bulk editing
    showNotification('Bulk editing feature coming soon!', 'info');
}

// Request actions
function addRequest() {
    openModal('addRequestModal');
}

function editRequest(requestId) {
    // TODO: Navigate to request edit page
    console.log('Editing request:', requestId);
    showNotification('Navigating to request editor...', 'info');
}

function duplicateRequest(requestId) {
    // TODO: Implement request duplication
    console.log('Duplicating request:', requestId);
    showNotification('Request duplication feature coming soon!', 'info');
}

function deleteRequest(requestId) {
    if (confirm('Are you sure you want to delete this request? This action cannot be undone.')) {
        // TODO: Implement request deletion
        console.log('Deleting request:', requestId);
        showNotification('Request deleted successfully!', 'success');
        
        // Remove from UI
        const requestItem = document.querySelector(`[data-request-id="${requestId}"]`);
        if (requestItem) {
            requestItem.remove();
        }
    }
}

function sendRequest(requestId) {
    // TODO: Navigate to request page and send
    console.log('Sending request:', requestId);
    showNotification('Navigating to request...', 'info');
}

// Create new request
function createRequest() {
    const form = document.getElementById('addRequestForm');
    const formData = new FormData(form);
    
    const requestData = {
        name: formData.get('name'),
        method: formData.get('method'),
        url: formData.get('url'),
        description: formData.get('description')
    };
    
    // Validate form
    if (!validateNewRequestForm(requestData)) {
        return;
    }
    
    // TODO: Send request to backend
    console.log('Creating new request:', requestData);
    
    // Show success message
    showNotification('Request created successfully!', 'success');
    
    // Close modal
    closeModal(document.getElementById('addRequestModal'));
    
    // Reset form
    form.reset();
    
    // Refresh page or add to UI
    setTimeout(() => {
        location.reload();
    }, 1000);
}

function validateNewRequestForm(data) {
    if (!data.name || !data.name.trim()) {
        showNotification('Please enter a request name', 'error');
        return false;
    }
    
    if (!data.url || !data.url.trim()) {
        showNotification('Please enter a URL', 'error');
        return false;
    }
    
    if (!isValidUrl(data.url)) {
        showNotification('Please enter a valid URL', 'error');
        return false;
    }
    
    return true;
}

// Utility functions
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('show');
        document.body.style.overflow = 'hidden';
        
        // Focus first input if exists
        const firstInput = modal.querySelector('input, select');
        if (firstInput) {
            firstInput.focus();
        }
    }
}

function closeModal(modal) {
    modal.classList.remove('show');
    document.body.style.overflow = '';
}

// Notification system (if not already defined in main.js)
if (typeof showNotification === 'undefined') {
    function showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <div class="notification-content">
                <i class="fas fa-${getNotificationIcon(type)}"></i>
                <span>${message}</span>
            </div>
            <button class="notification-close">
                <i class="fas fa-times"></i>
            </button>
        `;
        
        // Add notification styles if not already present
        if (!document.querySelector('#notification-styles')) {
            const style = document.createElement('style');
            style.id = 'notification-styles';
            style.textContent = `
                .notification {
                    position: fixed;
                    top: 80px;
                    right: 20px;
                    background: white;
                    border-radius: 8px;
                    padding: 16px 20px;
                    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                    border-left: 4px solid #007bff;
                    z-index: 3000;
                    display: flex;
                    align-items: center;
                    gap: 12px;
                    min-width: 300px;
                    transform: translateX(100%);
                    transition: transform 0.3s ease;
                }
                
                .notification.show {
                    transform: translateX(0);
                }
                
                .notification-success {
                    border-left-color: #28a745;
                }
                
                .notification-error {
                    border-left-color: #dc3545;
                }
                
                .notification-warning {
                    border-left-color: #ffc107;
                }
                
                .notification-info {
                    border-left-color: #17a2b8;
                }
                
                .notification-content {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    flex: 1;
                }
                
                .notification-close {
                    background: none;
                    border: none;
                    cursor: pointer;
                    color: #6c757d;
                    padding: 4px;
                    border-radius: 4px;
                    transition: all 0.2s ease;
                }
                
                .notification-close:hover {
                    background: #f8f9fa;
                    color: #495057;
                }
                
                @media (max-width: 768px) {
                    .notification {
                        right: 10px;
                        left: 10px;
                        min-width: auto;
                    }
                }
            `;
            document.head.appendChild(style);
        }
        
        document.body.appendChild(notification);
        
        // Show notification
        setTimeout(() => {
            notification.classList.add('show');
        }, 100);
        
        // Auto-hide after 5 seconds
        setTimeout(() => {
            hideNotification(notification);
        }, 5000);
        
        // Close button functionality
        const closeBtn = notification.querySelector('.notification-close');
        closeBtn.addEventListener('click', () => {
            hideNotification(notification);
        });
    }
    
    function hideNotification(notification) {
        notification.classList.remove('show');
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }
    
    function getNotificationIcon(type) {
        const icons = {
            success: 'check-circle',
            error: 'exclamation-circle',
            warning: 'exclamation-triangle',
            info: 'info-circle'
        };
        return icons[type] || 'info-circle';
    }
}

// Export functions for global use
window.CollectionPage = {
    addRequest,
    editRequest,
    duplicateRequest,
    deleteRequest,
    sendRequest,
    createRequest,
    editCollection,
    duplicateCollection,
    shareCollection,
    runCollection,
    runAllRequests,
    bulkEdit
};
