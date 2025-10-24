// DataTable initialization
$(document).ready(function() {
    $('#discountTable').DataTable({
        order: [[6, 'desc']], // Sort by valid period
        pageLength: 10,
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]]
    });
});

// Modal và form handling
let discountModal;
const baseUrl = window.location.origin + window.location.pathname.substring(0, window.location.pathname.indexOf('/admin'));

// Hàm validation form
function validateDiscountForm(formData) {
    // Kiểm tra mã giảm giá
    const code = formData.get('code').trim();
    if (!code) {
        showError('Mã giảm giá không được để trống');
        return false;
    }

    // Kiểm tra giá trị giảm giá
    const discountType = formData.get('discountType');
    const discountValue = parseFloat(formData.get('discountValue'));

    if (isNaN(discountValue) || discountValue <= 0) {
        showError('Giá trị giảm giá phải là số dương');
        return false;
    }

    if (discountType === 'percentage' && discountValue > 100) {
        showError('Phần trăm giảm giá không thể lớn hơn 100%');
        return false;
    }

    // Kiểm tra giá trị đơn hàng tối thiểu
    const minOrderAmount = parseFloat(formData.get('minOrderAmount'));
    if (isNaN(minOrderAmount) || minOrderAmount < 0) {
        showError('Giá trị đơn hàng tối thiểu phải là số không âm');
        return false;
    }

    // Kiểm tra giá trị giảm tối đa
    const maxDiscountAmount = parseFloat(formData.get('maxDiscountAmount'));
    if (isNaN(maxDiscountAmount) || maxDiscountAmount <= 0) {
        showError('Giá trị giảm tối đa phải là số dương');
        return false;
    }

    // Kiểm tra giới hạn sử dụng
    const usageLimit = parseInt(formData.get('usageLimit'));
    if (isNaN(usageLimit) || usageLimit <= 0) {
        showError('Giới hạn sử dụng phải là số nguyên dương');
        return false;
    }

    // Kiểm tra ngày
    const rawStartDate = formData.get('startDate'); // Định dạng: 'YYYY-MM-DDTHH:mm'
    const rawEndDate = formData.get('endDate');
    
    if (!rawStartDate || !rawEndDate) {
        showError('Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc');
        return false;
    }

    // Tách ngày và giờ
    const [startDatePart, startTimePart] = rawStartDate.split('T');
    const [endDatePart, endTimePart] = rawEndDate.split('T');

    // So sánh trực tiếp chuỗi ngày và giờ
    const startDateTime = rawStartDate.replace('T', ' ');
    const endDateTime = rawEndDate.replace('T', ' ');

    if (endDateTime <= startDateTime) {
        showError('Thời gian kết thúc phải sau thời gian bắt đầu');
        return false;
    }

    return true;
}

$(document).ready(function() {
    discountModal = new bootstrap.Modal(document.getElementById('discountModal'));
});

// Mở modal thêm mới
function openDiscountModal() {
    document.getElementById('discountForm').reset();
    document.getElementById('discountId').value = '';
    document.getElementById('modalTitle').textContent = 'Add New Discount';
    discountModal.show();
}

// Mở modal sửa
function editDiscount(id) {
    fetch(`${baseUrl}/admin/discounts/list`)
        .then(response => response.json())
        .then(discounts => {
            const discount = discounts.find(d => parseInt(d.discountId) === parseInt(id));
            if (discount) {
                // Điền dữ liệu vào form
                document.getElementById('discountId').value = discount.discountId;
                document.getElementById('code').value = discount.code;
                document.getElementById('description').value = discount.description;
                document.getElementById('discountType').value = discount.discountType;
                document.getElementById('discountValue').value = discount.discountValue;
                document.getElementById('minOrderAmount').value = discount.minOrderAmount;
                document.getElementById('maxDiscountAmount').value = discount.maxDiscountAmount;
                document.getElementById('usageLimit').value = discount.usageLimit;
                document.getElementById('usedCount').value = discount.usedCount;
                document.getElementById('isActive').checked = discount.isActive || discount.active;

                // Format dates for datetime-local input
                if (discount.startDate) {
                    document.getElementById('startDate').value = formatDateForInput(discount.startDate);
                }
                if (discount.endDate) {
                    document.getElementById('endDate').value = formatDateForInput(discount.endDate);
                }

                document.getElementById('modalTitle').textContent = 'Edit Discount';
                discountModal.show();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showError('Failed to load discount details');
        });
}

// Lưu discount (thêm mới hoặc cập nhật)
function saveDiscount() {
    // Thu thập dữ liệu từ form
    const formData = new FormData(document.getElementById('discountForm'));
    const discountId = formData.get('discountId');
    const url = `${baseUrl}/admin/discounts/${discountId ? 'update' : 'add'}`;
    
    // Kiểm tra validation
    if (!validateDiscountForm(formData)) {
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
    
    // Add missing fields
    formData.set('usedCount', document.getElementById('discountId').value ? 
                 document.getElementById('usedCount').value || '0' : '0');
    formData.set('isActive', document.getElementById('isActive').checked);
    
    // Send request
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
                discountModal.hide();
                window.location.reload();
            });
        } else {
            showError(result.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showError('Failed to save discount');
    });
}

// Xóa discount
function deleteDiscount(id) {
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
            fetch(`${baseUrl}/admin/discounts/delete`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `discountId=${id}`
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
                console.error('Error:', error);
                showError('Failed to delete discount');
            });
        }
    });
}

// Hàm tiện ích

function formatDateForInput(dateString) {
    if (!dateString) return '';
    try {
        // Tách ngày giờ từ chuỗi 'YYYY-MM-DD HH:mm:ss'
        const [datePart, timePart] = dateString.split(' ');
        if (!datePart || !timePart) return '';
        
        // Ghép lại theo định dạng 'YYYY-MM-DDTHH:mm'
        const formattedDate = `${datePart}T${timePart.slice(0, 5)}`;
        return formattedDate;
    } catch (error) {
        console.error('Error formatting date for input:', error);
        return '';
    }
}

function formatDateForServer(dateString) {
    if (!dateString) return null;
    try {
        // Đổi từ 'YYYY-MM-DDTHH:mm' thành 'YYYY-MM-DD HH:mm:ss'
        const formatted = dateString.replace('T', ' ') + ':00';
        return formatted;
    } catch (error) {
        console.error('Error formatting date for server:', error);
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