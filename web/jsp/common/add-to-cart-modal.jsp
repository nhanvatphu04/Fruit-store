<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Script xử lý thêm vào giỏ hàng -->
<script>
function showAddToCartModal(productId, productName, productPrice, stockQuantity, originalPrice = null, discountPercent = 0) {
    // Tạo HTML động dựa trên thông tin sản phẩm
    let priceHtml = '';
    if (originalPrice && discountPercent > 0) {
        priceHtml = `
            <div class="d-flex align-items-center gap-2">
                <span class="text-muted text-decoration-line-through">${originalPrice}</span>
                <span class="text-success fw-bold">${productPrice}</span>
                <span class="badge bg-warning text-dark">-${discountPercent}%</span>
            </div>
        `;
    } else {
        priceHtml = `<p class="text-success fw-bold">${productPrice}</p>`;
    }
    
    Swal.fire({
        title: 'Thêm vào giỏ hàng',
        html: `
            <div class="mb-3">
                <h6>${productName}</h6>
                ${priceHtml}
            </div>
            <div class="mb-3">
                <label class="form-label">Số lượng:</label>
                <div class="input-group">
                    <button class="btn btn-outline-secondary" type="button" onclick="decreaseQty()">-</button>
                    <input type="number" id="quantity" class="form-control text-center" value="1" min="1" max="${stockQuantity}" onchange="validateQty()">
                    <button class="btn btn-outline-secondary" type="button" onclick="increaseQty()">+</button>
                </div>
                <small class="text-muted">Còn lại: ${stockQuantity} sản phẩm</small>
            </div>
            <div class="mb-3">
                <p class="mb-1">Tổng tiền:</p>
                <h5 class="text-success" id="totalPrice">${productPrice}</h5>
            </div>
        `,
        showCancelButton: true,
        confirmButtonText: 'Thêm vào giỏ',
        cancelButtonText: 'Hủy',
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#dc3545',
        didOpen: () => {
            window.decreaseQty = function() {
                const qtyInput = document.getElementById('quantity');
                const currentQty = parseInt(qtyInput.value);
                if (currentQty > 1) {
                    qtyInput.value = currentQty - 1;
                    updateTotal();
                }
            };

            window.increaseQty = function() {
                const qtyInput = document.getElementById('quantity');
                const currentQty = parseInt(qtyInput.value);
                if (currentQty < stockQuantity) {
                    qtyInput.value = currentQty + 1;
                    updateTotal();
                }
            };

            window.validateQty = function() {
                const qtyInput = document.getElementById('quantity');
                let qty = parseInt(qtyInput.value);
                
                // Kiểm tra giới hạn
                if (qty < 1) qty = 1;
                if (qty > stockQuantity) qty = stockQuantity;
                
                qtyInput.value = qty;
                updateTotal();
            };

            window.updateTotal = function() {
                const qty = parseInt(document.getElementById('quantity').value);
                const basePrice = parseFloat(productPrice.replace(/[^0-9]/g, ''));
                const total = qty * basePrice;
                document.getElementById('totalPrice').textContent = 
                    new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(total);
            };
            
            // Cập nhật tổng tiền lần đầu khi modal mở
            updateTotal();
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const quantity = document.getElementById('quantity').value;
            // Gửi request thêm vào giỏ hàng
            console.log('Sending AJAX request:', {productId, quantity});
            $.ajax({
                url: '${pageContext.request.contextPath}/cart/add',
                type: 'POST',
                data: {
                    productId: productId,
                    quantity: quantity
                },
                success: function(response) {
                    if (response.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: 'Đã thêm sản phẩm vào giỏ hàng',
                            timer: 1500,
                            showConfirmButton: false
                        });
                        // Update cart count
                        if (typeof updateCartCountBadge === 'function') {
                            updateCartCountBadge();
                        }
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: response.message || 'Không thể thêm sản phẩm vào giỏ hàng'
                        });
                    }
                },
                error: function(xhr) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: xhr.responseJSON?.message || 'Không thể thêm sản phẩm vào giỏ hàng'
                    });
                }
            });
        }
    });
}
</script>