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
    $(document).on('click', '.view-order', function() {
        const orderId = $(this).data('id');
        console.log('View order clicked for ID:', orderId);

        // Clear previous modal data
        $('#modalOrderId').text('');
        $('#modalOrderDate').text('');
        $('#modalOrderStatus').html('');
        $('#modalCustomerName').text('');
        $('#modalCustomerEmail').text('');
        $('#modalCustomerPhone').text('');
        $('#modalOrderItems').html('');
        $('#modalOrderTotal').text('');

        // Fetch order details
        $.ajax({
            url: contextPath + '/admin/orders/details/' + orderId,
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                console.log('Order details response:', response);

                // Check if response data is valid
                if (!response || typeof response !== 'object') {
                    console.error('Invalid response format');
                    Swal.fire('Error', 'Invalid response format', 'error');
                    return;
                }

                if (!response.orderId) {
                    console.error('Invalid order data received');
                    Swal.fire('Error', 'Invalid order data received', 'error');
                    return;
                }

                // Update modal with order details
                $('#modalOrderId').text('#' + response.orderId);
                $('#modalOrderDate').text(response.orderDate || 'N/A');
                $('#modalOrderStatus').html(`
                    <span class="badge bg-${response.status === 'completed' ? 'success' :
                                          response.status === 'pending' ? 'warning' : 'danger'}">
                        ${response.status || 'Unknown'}
                    </span>
                `);

                // Update customer info
                const customer = response.customer || {};
                const customerName = customer.fullName || customer.username || 'N/A';
                const customerEmail = customer.email || 'N/A';
                const customerPhone = customer.phone || 'N/A';

                $('#modalCustomerName').text(customerName);
                $('#modalCustomerEmail').text(customerEmail);
                $('#modalCustomerPhone').text(customerPhone);

                // Update order items
                let itemsHtml = '';
                let total = 0;

                console.log('Items array:', response.items);

                // Check if items exist and is an array
                if (response.items && Array.isArray(response.items) && response.items.length > 0) {
                    response.items.forEach(item => {
                        const price = parseFloat(item.price) || 0;
                        const quantity = parseInt(item.quantity) || 0;
                        const subtotal = price * quantity;
                        total += subtotal;

                        const productName = (item.product && item.product.name) ? item.product.name : 'Unknown Product';

                        console.log('Adding item:', productName, 'Price:', price, 'Qty:', quantity);

                        itemsHtml += `
                            <tr>
                                <td>${productName}</td>
                                <td>₫${price.toLocaleString('vi-VN')}</td>
                                <td>${quantity}</td>
                                <td>₫${subtotal.toLocaleString('vi-VN')}</td>
                            </tr>
                        `;
                    });
                } else {
                    console.log('No items found in response');
                    itemsHtml = '<tr><td colspan="4" class="text-center">No items found</td></tr>';
                }

                console.log('Items HTML:', itemsHtml);
                console.log('Total amount:', total);

                // Set the items HTML
                $('#modalOrderItems').html(itemsHtml);

                // Set the total with proper formatting
                $('#modalOrderTotal').text('₫' + total.toLocaleString('vi-VN'));

                // Show modal
                console.log('Showing modal');
                const modal = new bootstrap.Modal(document.getElementById('viewOrderModal'));
                modal.show();
            },
            error: function(xhr, status, error) {
                console.error('AJAX error:', status, error);
                console.error('Response text:', xhr.responseText);

                let errorMessage = 'Failed to load order details';

                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse.message) {
                        errorMessage = errorResponse.message;
                    }
                } catch (e) {
                    console.error('Error parsing error response:', e);
                }

                Swal.fire('Error', errorMessage, 'error');
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