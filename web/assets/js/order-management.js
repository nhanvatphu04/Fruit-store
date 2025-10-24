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
        $.get(contextPath + '/admin/orders/details/' + orderId, function(order) {
            // Update modal with order details
            $('#modalOrderId').text('#' + order.orderId);
            $('#modalOrderDate').text(order.orderDate);
            $('#modalOrderStatus').html(`
                <span class="badge bg-${order.status === 'completed' ? 'success' : 
                                      order.status === 'pending' ? 'warning' : 'danger'}">
                    ${order.status}
                </span>
            `);
            
            // Update customer info
            $('#modalCustomerName').text(order.customerName);
            $('#modalCustomerEmail').text(order.customerEmail);
            $('#modalCustomerPhone').text(order.customerPhone);
            
            // Update order items
            let itemsHtml = '';
            let total = 0;
            
            order.items.forEach(item => {
                const subtotal = item.price * item.quantity;
                total += subtotal;
                
                itemsHtml += `
                    <tr>
                        <td>${item.productName}</td>
                        <td>$${item.price}</td>
                        <td>${item.quantity}</td>
                        <td>$${subtotal}</td>
                    </tr>
                `;
            });
            
            $('#modalOrderItems').html(itemsHtml);
            $('#modalOrderTotal').text('$' + total);
            
            // Show modal
            $('#viewOrderModal').modal('show');
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