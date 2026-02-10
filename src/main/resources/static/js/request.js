// Request Page JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeRequestPage();
});

function initializeRequestPage() {
    // Initialize tabs
    initializeTabs();
    
    // Initialize body type selector
    initializeBodyTypeSelector();
    
    // Initialize parameter and header management
    initializeDynamicFields();
    
    // Initialize form validation
    initializeFormValidation();
}

// Tab functionality
function initializeTabs() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');
            
            // Remove active class from all tabs
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));
            
            // Add active class to clicked tab
            this.classList.add('active');
            document.getElementById(targetTab).classList.add('active');
        });
    });
}

// Body type selector
function initializeBodyTypeSelector() {
    const bodyTypeButtons = document.querySelectorAll('.body-type-btn');
    
    bodyTypeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const type = this.getAttribute('data-type');
            
            // Remove active class from all buttons
            bodyTypeButtons.forEach(btn => btn.classList.remove('active'));
            
            // Add active class to clicked button
            this.classList.add('active');
            
            // Handle body type change
            handleBodyTypeChange(type);
        });
    });
}

function handleBodyTypeChange(type) {
    const bodyEditor = document.querySelector('.body-editor');
    
    switch(type) {
        case 'none':
            bodyEditor.style.display = 'none';
            break;
        case 'formdata':
            bodyEditor.innerHTML = createFormDataEditor();
            break;
        case 'xwwwform':
            bodyEditor.innerHTML = createXWWWFormEditor();
            break;
        case 'raw':
            bodyEditor.innerHTML = createRawEditor();
            break;
        case 'binary':
            bodyEditor.innerHTML = createBinaryEditor();
            break;
        case 'graphql':
            bodyEditor.innerHTML = createGraphQLEditor();
            break;
    }
}

function createFormDataEditor() {
    return `
        <div class="form-data-editor">
            <div class="form-data-list" id="formDataList">
                <div class="form-data-item">
                    <input type="text" placeholder="Key" class="form-data-key">
                    <select class="form-data-type">
                        <option value="text">Text</option>
                        <option value="file">File</option>
                    </select>
                    <input type="text" placeholder="Value" class="form-data-value">
                    <button class="btn-icon form-data-remove" onclick="removeFormDataItem(this)">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <button class="btn btn-sm btn-secondary" onclick="addFormDataItem()">
                <i class="fas fa-plus"></i>
                Add Field
            </button>
        </div>
    `;
}

function createXWWWFormEditor() {
    return `
        <div class="xwwwform-editor">
            <div class="xwwwform-list" id="xwwwformList">
                <div class="xwwwform-item">
                    <input type="text" placeholder="Key" class="xwwwform-key">
                    <input type="text" placeholder="Value" class="xwwwform-value">
                    <button class="btn-icon xwwwform-remove" onclick="removeXWWWFormItem(this)">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            <button class="btn btn-sm btn-secondary" onclick="addXWWWFormItem()">
                <i class="fas fa-plus"></i>
                Add Field
            </button>
        </div>
    `;
}

function createRawEditor() {
    return `
        <textarea id="bodyEditor" placeholder="Enter request body (JSON, XML, etc.)" style="width: 100%; min-height: 200px; padding: 16px; border: none; resize: vertical; font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace; font-size: 14px; line-height: 1.5;"></textarea>
    `;
}

function createBinaryEditor() {
    return `
        <div class="binary-editor">
            <input type="file" id="binaryFile" class="binary-file-input">
            <div class="binary-info">
                <p>Select a file to upload</p>
                <small>Supported formats: All file types</small>
            </div>
        </div>
    `;
}

function createGraphQLEditor() {
    return `
        <div class="graphql-editor">
            <div class="graphql-section">
                <label>Query:</label>
                <textarea id="graphqlQuery" placeholder="Enter GraphQL query" style="width: 100%; min-height: 120px; padding: 16px; border: 1px solid #dee2e6; border-radius: 6px; font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace; font-size: 14px; line-height: 1.5; resize: vertical;"></textarea>
            </div>
            <div class="graphql-section">
                <label>Variables:</label>
                <textarea id="graphqlVariables" placeholder="Enter GraphQL variables (JSON)" style="width: 100%; min-height: 80px; padding: 16px; border: 1px solid #dee2e6; border-radius: 6px; font-family: 'Monaco', 'Monaco', 'Ubuntu Mono', monospace; font-size: 14px; line-height: 1.5; resize: vertical;"></textarea>
            </div>
        </div>
    `;
}

// Dynamic field management
function initializeDynamicFields() {
    // Initialize remove buttons for existing fields
    document.querySelectorAll('.param-remove, .header-remove').forEach(button => {
        button.addEventListener('click', function() {
            removeField(this);
        });
    });
}

function addParam() {
    const paramsList = document.getElementById('paramsList');
    const paramItem = document.createElement('div');
    paramItem.className = 'param-item';
    paramItem.innerHTML = `
        <input type="text" placeholder="Key" class="param-key">
        <input type="text" placeholder="Value" class="param-value">
        <button class="btn-icon param-remove" onclick="removeField(this)">
            <i class="fas fa-trash"></i>
        </button>
    `;
    paramsList.appendChild(paramItem);
}

function addHeader() {
    const headersList = document.getElementById('headersList');
    const headerItem = document.createElement('div');
    headerItem.className = 'header-item';
    headerItem.innerHTML = `
        <input type="text" placeholder="Key" class="header-key">
        <input type="text" placeholder="Value" class="header-value">
        <button class="btn-icon header-remove" onclick="removeField(this)">
            <i class="fas fa-trash"></i>
        </button>
    `;
    headersList.appendChild(headerItem);
}

function addFormDataItem() {
    const formDataList = document.getElementById('formDataList');
    if (formDataList) {
        const formDataItem = document.createElement('div');
        formDataItem.className = 'form-data-item';
        formDataItem.innerHTML = `
            <input type="text" placeholder="Key" class="form-data-key">
            <select class="form-data-type">
                <option value="text">Text</option>
                <option value="file">File</option>
            </select>
            <input type="text" placeholder="Value" class="form-data-value">
            <button class="btn-icon form-data-remove" onclick="removeFormDataItem(this)">
                <i class="fas fa-trash"></i>
            </button>
        `;
        formDataList.appendChild(formDataItem);
    }
}

function addXWWWFormItem() {
    const xwwwformList = document.getElementById('xwwwformList');
    if (xwwwformList) {
        const xwwwformItem = document.createElement('div');
        xwwwformItem.className = 'xwwwform-item';
        xwwwformItem.innerHTML = `
            <input type="text" placeholder="Key" class="xwwwform-key">
            <input type="text" placeholder="Value" class="xwwwform-value">
            <button class="btn-icon xwwwform-remove" onclick="removeXWWWFormItem(this)">
                <i class="fas fa-trash"></i>
            </button>
        `;
        xwwwformList.appendChild(xwwwformItem);
    }
}

function removeField(button) {
    const fieldItem = button.closest('.param-item, .header-item');
    if (fieldItem) {
        fieldItem.remove();
    }
}

function removeFormDataItem(button) {
    const fieldItem = button.closest('.form-data-item');
    if (fieldItem) {
        fieldItem.remove();
    }
}

function removeXWWWFormItem(button) {
    const fieldItem = button.closest('.xwwwform-item');
    if (fieldItem) {
        fieldItem.remove();
    }
}

function addTest() {
    const testsEditor = document.getElementById('testsEditor');
    if (testsEditor) {
        const defaultTest = `
// Add your tests here
pm.test('Status code is 200', function () {
    pm.response.to.have.status(200);
});

pm.test('Response time is less than 200ms', function () {
    pm.expect(pm.response.responseTime).to.be.below(200);
});`;
        
        testsEditor.value = testsEditor.value + defaultTest;
    }
}

// Form validation
function initializeFormValidation() {
    const urlInput = document.getElementById('urlInput');
    const methodSelect = document.getElementById('methodSelect');
    
    if (urlInput) {
        urlInput.addEventListener('input', validateUrl);
    }
    
    if (methodSelect) {
        methodSelect.addEventListener('change', validateMethod);
    }
}

function validateUrl() {
    const urlInput = document.getElementById('urlInput');
    const url = urlInput.value.trim();
    
    if (url && !isValidUrl(url)) {
        urlInput.style.borderColor = '#dc3545';
        showNotification('Please enter a valid URL', 'error');
    } else {
        urlInput.style.borderColor = '#dee2e6';
    }
}

function validateMethod() {
    const methodSelect = document.getElementById('methodSelect');
    const method = methodSelect.value;
    
    // Show body tab for methods that typically have a body
    const bodyTab = document.querySelector('[data-tab="body"]');
    if (bodyTab) {
        if (['POST', 'PUT', 'PATCH'].includes(method)) {
            bodyTab.style.display = 'block';
        } else {
            bodyTab.style.display = 'none';
        }
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

// Send request functionality
function sendRequest() {
    // Validate form
    if (!validateRequestForm()) {
        return;
    }
    
    // Show loading state
    showLoadingState();
    
    // Collect request data
    const requestData = collectRequestData();
    
    const payload = toBackendPayload(requestData);

    fetch('/requests/execute', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(async res => {
        if (!res.ok) {
            // Try to read error body, but still pass status
            const text = await res.text();
            return Promise.reject({ statusCode: res.status, body: text || 'Request failed', headers: {}, responseTime: 0 });
        }
        return res.json();
    })
    .then(data => {
        // data: { statusCode, body, headers, responseTime }
        displayResponse({
            statusCode: data.statusCode,
            body: typeof data.body === 'string' ? data.body : JSON.stringify(data.body, null, 2),
            headers: data.headers || {},
            responseTime: data.responseTime || 0
        });
    })
    .catch(err => {
        displayResponse({
            statusCode: err.statusCode || 500,
            body: typeof err.body === 'string' ? err.body : 'Unexpected error',
            headers: err.headers || {},
            responseTime: err.responseTime || 0
        });
    })
    .finally(() => hideLoadingState());
}

// Save the current request definition so it appears in history and can be reopened
function saveRequest() {
    // Reuse validation from sendRequest
    if (!validateRequestForm()) {
        return;
    }

    const requestData = collectRequestData();
    const payload = toBackendPayload(requestData);
    // Mark as explicitly saved/favorited
    payload.saved = true;

    fetch('/requests', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(text => {
                    throw new Error(text || 'Failed to save request');
                });
            }
            return res.json();
        })
        .then(saved => {
            showNotification('Request saved successfully!', 'success');
            if (saved && saved.id) {
                // Navigate to the saved request so URL is canonical and history refreshes on reload
                window.location.href = '/request/' + saved.id;
            }
        })
        .catch(err => {
            console.error('Error saving request', err);
            showNotification('Failed to save request', 'error');
        });
}

function validateRequestForm() {
    const urlInput = document.getElementById('urlInput');
    const url = urlInput.value.trim();
    
    if (!url) {
        showNotification('Please enter a URL', 'error');
        urlInput.focus();
        return false;
    }
    
    if (!isValidUrl(url)) {
        showNotification('Please enter a valid URL', 'error');
        urlInput.focus();
        return false;
    }
    
    return true;
}

function collectRequestData() {
    const method = document.getElementById('methodSelect').value;
    const url = document.getElementById('urlInput').value.trim();
    const body = document.getElementById('bodyEditor')?.value || '';
    const idInput = document.getElementById('requestId');
    const id = idInput && idInput.value ? parseInt(idInput.value, 10) : null;
    
    // Collect parameters
    const params = {};
    document.querySelectorAll('.param-item').forEach(item => {
        const key = item.querySelector('.param-key').value.trim();
        const value = item.querySelector('.param-value').value.trim();
        if (key && value) {
            params[key] = value;
        }
    });
    
    // Collect headers
    const headers = {};
    document.querySelectorAll('.header-item').forEach(item => {
        const key = item.querySelector('.header-key').value.trim();
        const value = item.querySelector('.header-value').value.trim();
        if (key && value) {
            headers[key] = value;
        }
    });
    
    return {
        id,
        method,
        url,
        params,
        headers,
        body
    };
}

function toBackendPayload(requestData) {
    const urlWithParams = buildUrlWithParams(requestData.url, requestData.params);
    const payload = {
        method: requestData.method,
        url: urlWithParams,
        headers: JSON.stringify(requestData.headers || {}),
        body: requestData.body || ''
    };

    if (requestData.id) {
        payload.id = requestData.id;
    }

    return payload;
}

function buildUrlWithParams(url, params) {
    try {
        const u = new URL(url);
        Object.entries(params || {}).forEach(([k, v]) => {
            if (k && v) u.searchParams.append(k, v);
        });
        return u.toString();
    } catch (e) {
        // Fallback for relative URLs
        const query = Object.entries(params || {})
            .filter(([k, v]) => k && v)
            .map(([k, v]) => encodeURIComponent(k) + '=' + encodeURIComponent(v))
            .join('&');
        if (!query) return url;
        return url.includes('?') ? url + '&' + query : url + '?' + query;
    }
}

function showLoadingState() {
    const sendButton = document.querySelector('.btn-primary');
    if (sendButton) {
        sendButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';
        sendButton.disabled = true;
    }
}

function hideLoadingState() {
    const sendButton = document.querySelector('.btn-primary');
    if (sendButton) {
        sendButton.innerHTML = '<i class="fas fa-paper-plane"></i> Send';
        sendButton.disabled = false;
    }
}

function displayResponse(response) {
    const responseSection = document.getElementById('responseSection');
    const statusBadge = document.getElementById('statusBadge');
    const responseTime = document.getElementById('responseTime');
    const responseSize = document.getElementById('responseSize');
    const responseBody = document.getElementById('responseBody');
    
    if (responseSection && statusBadge && responseTime && responseSize && responseBody) {
        // Show response section
        responseSection.style.display = 'block';
        
        // Update response info
        statusBadge.textContent = `${response.statusCode} ${getStatusText(response.statusCode)}`;
        statusBadge.className = `status-badge ${getStatusClass(response.statusCode)}`;
        responseTime.textContent = `${response.responseTime}ms`;
        responseSize.textContent = formatBytes(response.body.length);
        
        // Update response body
        responseBody.textContent = response.body;
        
        // Update response headers
        updateResponseHeaders(response.headers);
        
        // Show success notification
        showNotification('Request sent successfully!', 'success');
        
        // Scroll to response section
        responseSection.scrollIntoView({ behavior: 'smooth' });
    }
}

function getStatusText(statusCode) {
    const statusTexts = {
        200: 'OK',
        201: 'Created',
        204: 'No Content',
        400: 'Bad Request',
        401: 'Unauthorized',
        403: 'Forbidden',
        404: 'Not Found',
        500: 'Internal Server Error'
    };
    return statusTexts[statusCode] || 'Unknown';
}

function getStatusClass(statusCode) {
    if (statusCode >= 200 && statusCode < 300) return 'status-success';
    if (statusCode >= 400 && statusCode < 500) return 'status-client-error';
    if (statusCode >= 500) return 'status-server-error';
    return 'status-info';
}

function formatBytes(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

function updateResponseHeaders(headers) {
    const responseHeaders = document.getElementById('responseHeaders');
    if (responseHeaders) {
        let headersHtml = '';
        Object.entries(headers).forEach(([key, value]) => {
            headersHtml += `
                <div class="header-display-item">
                    <span class="header-key">${key}:</span>
                    <span class="header-value">${value}</span>
                </div>
            `;
        });
        responseHeaders.innerHTML = headersHtml;
    }
}

// Export functions for global use
window.RequestPage = {
    addParam,
    addHeader,
    addFormDataItem,
    addXWWWFormItem,
    removeField,
    removeFormDataItem,
    removeXWWWFormItem,
    addTest,
    sendRequest,
    saveRequest
};
