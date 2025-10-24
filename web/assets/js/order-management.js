$(document).ready(function() {
    // Initialize DataTable
    const ordersTable = $('#ordersTable').DataTable({
        order: [[4, 'desc']], // Sort by date column descending
        pageLength: 10,
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]]
    });

    // Filter buttons functionality
    $('.filter-btn').click(function() {
        const status = $(this).data('status');
        
        // Update active state of buttons
        $('.filter-btn').removeClass('active');
        $(this).addClass('active');
        
        // Apply filter
        if (status === 'all') {
            ordersTable.column(3).search('').draw();
        } else {
            ordersTable.column(3).search(status).draw();
        }
    });

    // View Order Details
    $('.view-order').click(function() {
        const orderId = $(this).data('id');

        // Fetch order details
        $.ajax({
            url: contextPath + '/admin/orders/details/' + orderId,
            type: 'GET',
            dataType: 'json',
            success: function(order) {
                console.log('Order data:', order); // Debug log

                // Check if order data is valid
                if (!order || !order.orderId) {
                    Swal.fire('Error', 'Invalid order data received', 'error');
                    return;
                }

                // Update modal with order details
                $('#modalOrderId').text('#' + order.orderId);
                $('#modalOrderDate').text(order.orderDate);
                $('#modalOrderStatus').html(`
                    <span class="badge bg-${order.status === 'completed' ? 'success' :
                                          order.status === 'pending' ? 'warning' : 'danger'}">
                        ${order.status}
                    </span>
                `);

                // Update customer info - use correct field names from User model
                const customerName = order.customer && (order.customer.fullName || order.customer.username) ?
                                    (order.customer.fullName || order.customer.username) : 'N/A';
                const customerEmail = order.customer && order.customer.email ? order.customer.email : 'N/A';
                const customerPhone = order.customer && order.customer.phone ? order.customer.phone : 'N/A';

                $('#modalCustomerName').text(customerName);
                $('#modalCustomerEmail').text(customerEmail);
                $('#modalCustomerPhone').text(customerPhone);

                // Update order items
                let itemsHtml = '';
                let total = 0;

                // Check if items exist and is an array
                if (order.items && Array.isArray(order.items) && order.items.length > 0) {
                    order.items.forEach(item => {
                        const subtotal = item.price * item.quantity;
                        total += subtotal;

                        const productName = item.product && item.product.name ? item.product.name : 'Unknown Product';

                        itemsHtml += `
                            <tr>
                                <td>${productName}</td>
                                <td>₫${parseFloat(item.price).toLocaleString('vi-VN')}</td>
                                <td>${item.quantity}</td>
                                <td>₫${parseFloat(subtotal).toLocaleString('vi-VN')}</td>
                            </tr>
                        `;
                    });
                } else {
                    itemsHtml = '<tr><td colspan="4" class="text-center">No items found</td></tr>';
                }

                $('#modalOrderItems').html(itemsHtml);
                $('#modalOrderTotal').text('₫' + parseFloat(total).toLocaleString('vi-VN'));

                // Show modal
                $('#viewOrderModal').modal('show');
            },
            error: function(xhr, status, error) {
                console.error('Error loading order details:', error);
                console.error('Response:', xhr.responseText);
                Swal.fire('Error', 'Failed to load order details: ' + error, 'error');
            }
        });
    });

    // Update Order Status
    $('.update-status').click(function() {
        const orderId = $(this).data('id');
        const currentStatus = $(this).closest('tr').find('.badge').text().trim();
        
        Swal.fire({
            title: 'Update Order Status',
            input: 'select',
            inputOptions: {
                'pending': 'Pending',
                'completed': 'Completed',
                'cancelled': 'Cancelled'
            },
            inputValue: currentStatus.toLowerCase(),
            showCancelButton: true,
            confirmButtonText: 'Update',
            showLoaderOnConfirm: true,
            preConfirm: (status) => {
                return $.post(contextPath + '/admin/orders/update-status', {
                    orderId: orderId,
                    status: status
                })
                .then(response => {
                    if (!response.success) {
                        throw new Error(response.message || 'Failed to update status');
                    }
                    return response;
                })
                .catch(error => {
                    Swal.showValidationMessage(`Request failed: ${error}`);
                });
            },
            allowOutsideClick: () => !Swal.isLoading()
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    icon: 'success',
                    title: 'Status Updated',
                    text: 'The order status has been updated successfully.'
                }).then(() => {
                    location.reload();
                });
            }
        });
    });
});