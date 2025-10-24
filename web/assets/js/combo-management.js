// DataTable initialization
$(document).ready(function() {
    $('#comboTable').DataTable({
        order: [[3, 'desc']], // Sort by valid period
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]]
    });
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

$(document).ready(function() {
    comboModal = new bootstrap.Modal(document.getElementById('comboModal'));
    comboItemsModal = new bootstrap.Modal(document.getElementById('comboItemsModal'));
});

// Mở modal thêm mới combo
function openComboModal() {
    document.getElementById('comboForm').reset();
    document.getElementById('comboId').value = '';
    document.getElementById('modalTitle').textContent = 'Add New Combo';
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
                document.getElementById('isActive').checked = combo.isActive;

                if (combo.startDate) {
                    document.getElementById('startDate').value = formatDateForInput(combo.startDate);
                }
                
                if (combo.endDate) {
                    document.getElementById('endDate').value = formatDateForInput(combo.endDate);
                }

                document.getElementById('modalTitle').textContent = 'Edit Combo';
                comboModal.show();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showError('Failed to load combo details');
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

    fetch(url, {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.success) {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: result.message,
                timer: 1500
            }).then(() => {
                comboModal.hide();
                window.location.reload();
            });
        } else {
            showError(result.message);
        }
    })
    .catch(error => {
        showError('Failed to save combo');
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