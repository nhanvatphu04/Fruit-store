<div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="orderDetailsModalLabel">
                    Chi tiết đơn hàng <span id="modalOrderId"></span>
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Order Info -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <h6><i class="fas fa-info-circle me-2"></i>Thông tin đơn hàng</h6>
                        <p class="mb-1"><strong>Mã đơn:</strong> <span id="detailOrderId"></span></p>
                        <p class="mb-1"><strong>Ngày đặt:</strong> <span id="detailOrderDate"></span></p>
                        <p class="mb-1"><strong>Trạng thái:</strong> <span id="detailOrderStatus"></span></p>
                    </div>
                    <div class="col-md-6">
                        <h6><i class="fas fa-user me-2"></i>Thông tin khách hàng</h6>
                        <p class="mb-1"><strong>Tên:</strong> <span id="detailCustomerName"></span></p>
                        <p class="mb-1"><strong>Email:</strong> <span id="detailCustomerEmail"></span></p>
                        <p class="mb-1"><strong>Điện thoại:</strong> <span id="detailCustomerPhone"></span></p>
                        <p class="mb-1"><strong>Địa chỉ giao hàng:</strong> <span id="detailCustomerAddress"></span></p>
                    </div>
                </div>

                <!-- Discount Info -->
                <div id="discountSection" class="alert alert-success mb-4" style="display: none;">
                    <h6><i class="fas fa-tag me-2"></i>Mã giảm giá</h6>
                    <p class="mb-1"><strong>Mã:</strong> <span id="detailDiscountCode"></span></p>
                    <p class="mb-0"><strong>Giảm giá:</strong> <span id="detailDiscountAmount"></span></p>
                </div>

                <!-- Order Items -->
                <h6><i class="fas fa-shopping-cart me-2"></i>Sản phẩm / Combo</h6>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>Sản phẩm / Combo</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody id="detailOrderItems">
                            <!-- Filled by JS -->
                        </tbody>
                        <tfoot>
                            <tr class="table-light">
                                <td colspan="3" class="text-end"><strong>Tổng cộng:</strong></td>
                                <td><strong id="detailOrderTotal" class="text-success"></strong></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>
