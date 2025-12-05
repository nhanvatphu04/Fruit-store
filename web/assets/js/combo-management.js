// DataTable initialization
$(document).ready(function() {
    $('#comboTable').DataTable({
        order: [[3, 'desc']], // Sort by valid period
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]]
    });

    // Modal initialization
    comboModal = new bootstrap.Modal(document.getElementById('comboModal'));
    comboItemsModal = new bootstrap.Modal(document.getElementById('comboItemsModal'));

    // Event listeners for price calculation
    const originalPriceInput = document.getElementById('originalPrice');
    const discountPercentageInput = document.getElementById('discountPercentage');
    if (originalPriceInput) {
        originalPriceInput.addEventListener('input', calculateFinalPrice);
    }
    if (discountPercentageInput) {
        discountPercentageInput.addEventListener('input', calculateFinalPrice);
    }
});

// Modal handling
let comboModal;
let comboItemsModal;
const baseUrl = window.location.origin + window.location.pathname.substring(0, window.location.pathname.indexOf('/admin'));

// Hàm validation form
function validateComboForm(formData) {
    // Kiểm tra tên combo
    const name = formData.get('name').trim();
    if (!name) {
        showError('Tên combo không được để trống');
        return false;
    }

    // Kiểm tra URL hình ảnh
    const imageUrl = formData.get('imageUrl').trim();
    if (!imageUrl) {
        showError('URL hình ảnh không được để trống');
        return false;
    }

    // Kiểm tra giá
    const originalPrice = parseFloat(formData.get('originalPrice'));
    const comboPrice = parseFloat(formData.get('comboPrice'));

    if (isNaN(originalPrice) || originalPrice <= 0) {
        showError('Giá gốc phải là số dương');
        return false;
    }

    if (isNaN(comboPrice) || comboPrice <= 0) {
        showError('Giá combo phải là số dương');
        return false;
    }

    if (comboPrice >= originalPrice) {
        showError('Giá combo phải nhỏ hơn giá gốc');
        return false;
    }

    // Kiểm tra ngày
    const rawStartDate = formData.get('startDate'); // Định dạng: 'YYYY-MM-DDTHH:mm'
    const rawEndDate = formData.get('endDate');
    
    if (!rawStartDate || !rawEndDate) {
        showError('Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc');
        return false;
    }

    const startDateTime = rawStartDate.replace('T', ' ');
    const endDateTime = rawEndDate.replace('T', ' ');

    if (endDateTime <= startDateTime) {
        showError('Thời gian kết thúc phải sau thời gian bắt đầu');
        return false;
    }

    return true;
}

// Hàm tính giá cuối cùng dựa trên giá gốc và phần trăm giảm giá
function calculateFinalPrice() {
    const originalPrice = parseFloat(document.getElementById('originalPrice').value) || 0;
    const discountPercent = parseFloat(document.getElementById('discountPercentage').value) || 0;

    if (originalPrice > 0 && discountPercent >= 0 && discountPercent <= 100) {
        const discountAmount = (originalPrice * discountPercent) / 100;
        const finalPrice = originalPrice - discountAmount;
        document.getElementById('comboPrice').value = finalPrice.toFixed(2);
    }
}

// Store for combo products in the modal (temporary storage)
let comboProducts = [];
let originalComboProducts = []; // Track original products for detecting deletions

// Mở modal thêm mới combo
function openComboModal() {
    document.getElementById('comboForm').reset();
    document.getElementById('comboId').value = '';
    document.getElementById('modalTitle').textContent = 'Add New Combo';
    document.getElementById('comboProductsTable').innerHTML = '';
    comboProducts = [];
    calculateFinalPrice();
    comboModal.show();
}

// Mở modal sửa combo
function editCombo(id) {
    fetch(`${baseUrl}/admin/combos/list`)
        .then(response => response.json())
        .then(combos => {
            const combo = combos.find(c => parseInt(c.comboId) === parseInt(id));
            if (combo) {
                document.getElementById('comboId').value = combo.comboId;
                document.getElementById('name').value = combo.name;
                document.getElementById('description').value = combo.description;
                document.getElementById('imageUrl').value = combo.imageUrl;
                document.getElementById('originalPrice').value = combo.originalPrice;
                document.getElementById('comboPrice').value = combo.comboPrice;
                document.getElementById('discountPercentage').value = combo.discountPercentage || 0;
                document.getElementById('isActive').checked = combo.isActive;

                if (combo.startDate) {
                    document.getElementById('startDate').value = formatDateForInput(combo.startDate);
                }
                
                if (combo.endDate) {
                    document.getElementById('endDate').value = formatDateForInput(combo.endDate);
                }

                document.getElementById('modalTitle').textContent = 'Edit Combo';
                
                // Reset product select and quantity
                document.getElementById('productSelect').value = '';
                document.getElementById('productQuantity').value = '1';
                
                // Load existing combo products
                loadComboProductsInModal(combo.comboId);
                
                comboModal.show();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showError('Failed to load combo details');
        });
}

// Hàm thêm sản phẩm vào combo trong modal
function addProductToCombo() {
    const productSelect = document.getElementById('productSelect');
    const selectedOption = productSelect.options[productSelect.selectedIndex];
    const productId = selectedOption.value;
    
    if (!productId) {
        showError('Vui lòng chọn sản phẩm');
        return;
    }

    const productName = selectedOption.text.split(' - ')[0];
    const productPrice = parseFloat(selectedOption.getAttribute('data-price'));
    const quantity = parseInt(document.getElementById('productQuantity').value) || 1;

    if (quantity <= 0) {
        showError('Số lượng phải lớn hơn 0');
        return;
    }

    if (isNaN(productPrice) || productPrice <= 0) {
        showError('Giá sản phẩm không hợp lệ');
        return;
    }

    // Check if product already exists in combo
    const existingProduct = comboProducts.find(p => p.productId == productId);
    if (existingProduct) {
        existingProduct.quantity = quantity;
        showError('Sản phẩm đã tồn tại trong combo, số lượng đã được cập nhật');
    } else {
        comboProducts.push({
            productId: productId,
            productName: productName,
            productPrice: productPrice,
            quantity: quantity
        });
    }

    // Reset form
    productSelect.value = '';
    document.getElementById('productQuantity').value = '1';

    // Refresh table
    refreshComboProductsTable();
}

// Hàm xóa sản phẩm khỏi combo trong modal
function removeProductFromCombo(productId) {
    comboProducts = comboProducts.filter(p => p.productId != productId);
    refreshComboProductsTable();
}

// Hàm refresh bảng sản phẩm trong combo
function refreshComboProductsTable() {
    const tbody = document.getElementById('comboProductsTable');
    tbody.innerHTML = '';

    if (comboProducts.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No products added yet</td></tr>';
        // Reset original price when no products
        document.getElementById('originalPrice').value = '';
        return;
    }

    let totalPrice = 0;
    comboProducts.forEach(product => {
        const subtotal = product.productPrice * product.quantity;
        totalPrice += subtotal;
        const row = `
            <tr>
                <td>${product.productName}</td>
                <td>${product.productPrice.toFixed(2)}đ</td>
                <td>${product.quantity}</td>
                <td>${subtotal.toFixed(2)}đ</td>
                <td>
                    <button type="button" class="btn btn-sm btn-danger" onclick="removeProductFromCombo('${product.productId}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
        tbody.innerHTML += row;
    });
    
    // Auto-calculate and set original price
    document.getElementById('originalPrice').value = totalPrice.toFixed(2);
    // Recalculate final price based on new original price
    calculateFinalPrice();
}

// Hàm load combo products trong modal để edit
function loadComboProductsInModal(comboId) {
    fetch(`${baseUrl}/admin/combos/${comboId}/items`)
        .then(response => response.json())
        .then(items => {
            // Get all products to fetch prices
            const productSelect = document.getElementById('productSelect');
            const products = {};
            
            // Create a map of product prices from select options
            for (let option of productSelect.options) {
                if (option.value) {
                    products[option.value] = parseFloat(option.getAttribute('data-price') || 0);
                }
            }
            
            comboProducts = items.map(item => ({
                productId: item.productId,
                productName: item.productName,
                productPrice: products[item.productId] || 0, // Get actual price from product map
                quantity: item.quantity,
                comboItemId: item.comboItemId
            }));
            
            // Store a copy of original products for tracking deletions
            originalComboProducts = JSON.parse(JSON.stringify(comboProducts));
            
            refreshComboProductsTable();
        })
        .catch(error => {
            console.error('Error loading combo products:', error);
        });
}

// Lưu combo
function saveCombo() {
    const formData = new FormData(document.getElementById('comboForm'));
    const comboId = formData.get('comboId');
    const url = `${baseUrl}/admin/combos/${comboId ? 'update' : 'add'}`;

    // Kiểm tra validation
    if (!validateComboForm(formData)) {
        return;
    }

    // Format dates
    const formattedStartDate = formatDateForServer(formData.get('startDate'));
    const formattedEndDate = formatDateForServer(formData.get('endDate'));
    
    if (!formattedStartDate || !formattedEndDate) {
        showError('Ngày bắt đầu và ngày kết thúc phải hợp lệ');
        return;
    }
    
    formData.set('startDate', formattedStartDate);
    formData.set('endDate', formattedEndDate);
    formData.set('isActive', document.getElementById('isActive').checked);
    formData.set('discountPercentage', document.getElementById('discountPercentage').value);

    fetch(url, {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            // Save combo products if it's a new combo
            if (!comboId && comboProducts.length > 0) {
                saveComboProducts(result.comboId);
            } else if (!comboId && comboProducts.length === 0) {
                // New combo without products
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Combo created successfully',
                    timer: 1500
                }).then(() => {
                    comboModal.hide();
                    window.location.reload();
                });
            } else if (comboId) {
                // Editing existing combo - handle deletions and new products
                const currentProductIds = comboProducts.map(p => p.productId);
                const removedProducts = originalComboProducts.filter(p => !currentProductIds.includes(p.productId));
                const newProducts = comboProducts.filter(p => !p.comboItemId);
                
                if (removedProducts.length > 0 || newProducts.length > 0) {
                    // Delete removed products first
                    if (removedProducts.length > 0) {
                        deleteRemovedProducts(comboId, removedProducts, newProducts);
                    } else if (newProducts.length > 0) {
                        // Save only the new products
                        saveNewComboProducts(comboId, newProducts);
                    } else {
                        // No changes in products
                        Swal.fire({
                            icon: 'success',
                            title: 'Success',
                            text: 'Combo updated successfully',
                            timer: 1500
                        }).then(() => {
                            comboModal.hide();
                            window.location.reload();
                        });
                    }
                } else {
                    // No products changed
                    Swal.fire({
                        icon: 'success',
                        title: 'Success',
                        text: 'Combo updated successfully',
                        timer: 1500
                    }).then(() => {
                        comboModal.hide();
                        window.location.reload();
                    });
                }
            } else {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: result.message,
                    timer: 1500
                }).then(() => {
                    comboModal.hide();
                    window.location.reload();
                });
            }
        } else {
            showError(result.message);
        }
    })
    .catch(error => {
        showError('Failed to save combo: ' + error.message);
    });
}

// Hàm xóa sản phẩm đã gỡ khỏi combo khi chỉnh sửa
function deleteRemovedProducts(comboId, removedProducts, newProducts) {
    let deletedCount = 0;
    let deleteErrorCount = 0;
    
    removedProducts.forEach(product => {
        if (product.comboItemId) {
            fetch(`${baseUrl}/admin/combos/items/delete`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `comboItemId=${product.comboItemId}`
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    deletedCount++;
                } else {
                    deleteErrorCount++;
                }
                
                if (deletedCount + deleteErrorCount === removedProducts.length) {
                    // After deletions, save new products if any
                    if (newProducts.length > 0) {
                        saveNewComboProducts(comboId, newProducts);
                    } else {
                        Swal.fire({
                            icon: 'success',
                            title: 'Success',
                            text: 'Combo updated successfully',
                            timer: 1500
                        }).then(() => {
                            comboModal.hide();
                            window.location.reload();
                        });
                    }
                }
            })
            .catch(error => {
                deleteErrorCount++;
                if (deletedCount + deleteErrorCount === removedProducts.length) {
                    if (newProducts.length > 0) {
                        saveNewComboProducts(comboId, newProducts);
                    } else {
                        Swal.fire({
                            icon: 'success',
                            title: 'Success',
                            text: 'Combo updated successfully',
                            timer: 1500
                        }).then(() => {
                            comboModal.hide();
                            window.location.reload();
                        });
                    }
                }
            });
        }
    });
}

// Hàm lưu sản phẩm mới vào combo khi chỉnh sửa
function saveNewComboProducts(comboId, newProducts) {
    if (newProducts.length === 0) {
        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Combo updated successfully',
            timer: 1500
        }).then(() => {
            comboModal.hide();
            window.location.reload();
        });
        return;
    }

    let savedCount = 0;
    let errorCount = 0;
    
    newProducts.forEach(product => {
        const itemData = new FormData();
        itemData.set('itemComboId', comboId);
        itemData.set('productId', product.productId);
        itemData.set('quantity', product.quantity);

        fetch(`${baseUrl}/admin/combos/items/add`, {
            method: 'POST',
            body: new URLSearchParams(itemData)
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                savedCount++;
            } else {
                errorCount++;
            }
            
            if (savedCount + errorCount === newProducts.length) {
                if (errorCount === 0) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success',
                        text: 'Combo and products updated successfully',
                        timer: 1500
                    }).then(() => {
                        comboModal.hide();
                        window.location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Partial Success',
                        text: `${savedCount} products added successfully, but ${errorCount} failed`,
                        timer: 2000
                    }).then(() => {
                        comboModal.hide();
                        window.location.reload();
                    });
                }
            }
        })
        .catch(error => {
            errorCount++;
            if (savedCount + errorCount === newProducts.length) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Failed to save some products'
                }).then(() => {
                    window.location.reload();
                });
            }
        });
    });
}

// Hàm lưu sản phẩm vào combo
function saveComboProducts(comboId) {
    if (comboProducts.length === 0) {
        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Combo created successfully',
            timer: 1500
        }).then(() => {
            comboModal.hide();
            window.location.reload();
        });
        return;
    }

    let savedCount = 0;
    comboProducts.forEach(product => {
        const itemData = new FormData();
        itemData.set('itemComboId', comboId);
        itemData.set('productId', product.productId);
        itemData.set('quantity', product.quantity);

        fetch(`${baseUrl}/admin/combos/items/add`, {
            method: 'POST',
            body: new URLSearchParams(itemData)
        })
        .then(response => response.json())
        .then(result => {
            savedCount++;
            if (savedCount === comboProducts.length) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Combo and products created successfully',
                    timer: 1500
                }).then(() => {
                    comboModal.hide();
                    window.location.reload();
                });
            }
        });
    });
}

// Xóa combo
function deleteCombo(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${baseUrl}/admin/combos/delete`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `comboId=${id}`
            })
            .then(response => response.json())
            .then(result => {
                if (result.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Deleted!',
                        text: result.message,
                        timer: 1500
                    }).then(() => {
                        window.location.reload();
                    });
                } else {
                    showError(result.message);
                }
            })
            .catch(error => {
                showError('Failed to delete combo');
            });
        }
    });
}

// Quản lý sản phẩm trong combo
function manageComboItems(comboId) {
    document.getElementById('itemComboId').value = comboId;
    loadComboItems(comboId);
    comboItemsModal.show();
}

// Load danh sách sản phẩm trong combo
function loadComboItems(comboId) {
    fetch(`${baseUrl}/admin/combos/${comboId}/items`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const contentType = response.headers.get('content-type');
            if (!contentType || !contentType.includes('application/json')) {
                throw new TypeError("Oops, we haven't got JSON!");
            }
            return response.json();
        })
        .then(items => {
            const tbody = document.getElementById('comboItemsList');
            tbody.innerHTML = '';
            
            // Check if items is valid and is an array
            if (!items || !Array.isArray(items)) {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center">No items found</td></tr>';
                return;
            }
            
            if (items.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center">No items in this combo</td></tr>';
                return;
            }
            
            items.forEach(item => {
                const row = `
                    <tr>
                        <td>${item.productName || 'Unknown Product'}</td>
                        <td>${item.quantity || 0}</td>
                        <td>
                            <button type="button" class="btn btn-sm btn-danger" onclick="removeComboItem(${item.comboItemId})">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `;
                tbody.innerHTML += row;
            });
        })
        .catch(error => {
            showError(`Failed to load combo items: ${error.message}`);
        });
}

// Thêm sản phẩm vào combo
function addComboItem() {
    const formData = new FormData(document.getElementById('comboItemForm'));
    fetch(`${baseUrl}/admin/combos/items/add`, {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            loadComboItems(formData.get('itemComboId'));
            document.getElementById('quantity').value = '';
        } else {
            showError(result.message);
        }
    })
    .catch(error => {
        showError('Failed to add combo item');
    });
}

// Xóa sản phẩm khỏi combo
function removeComboItem(comboItemId) {
    const comboId = document.getElementById('itemComboId').value;
    fetch(`${baseUrl}/admin/combos/items/delete`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `comboItemId=${comboItemId}`
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            loadComboItems(comboId);
        } else {
            showError(result.message);
        }
    })
    .catch(error => {
        showError('Failed to remove combo item');
    });
}

// Hàm tiện ích
function formatDateForInput(dateString) {
    if (!dateString) return '';
    try {
        // Parse date string to Date object
        const date = new Date(dateString);
        if (isNaN(date.getTime())) {
            return '';
        }

        // Format date to YYYY-MM-DDTHH:mm
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${year}-${month}-${day}T${hours}:${minutes}`;
    } catch (error) {
        return '';
    }
}

function formatDateForServer(dateString) {
    if (!dateString) return null;
    try {
        // Đổi từ 'YYYY-MM-DDTHH:mm' thành 'YYYY-MM-DD HH:mm:ss'
        return dateString.replace('T', ' ') + ':00';
    } catch (error) {
        return null;
    }
}

function showError(message) {
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: message
    });
}