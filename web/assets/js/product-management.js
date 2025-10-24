$(document).ready(function() {
    // Initialize DataTable
    const productsTable = $('#productsTable').DataTable({
        order: [[0, 'desc']], // Sort by ID descending
        pageLength: 10,
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]]
    });

    // Back to Top button functionality
    $(window).scroll(function() {
        if ($(this).scrollTop() > 100) {
            $('#backToTop').fadeIn();
        } else {
            $('#backToTop').fadeOut();
        }
    });

    $('#backToTop').click(function() {
        $('html, body').animate({scrollTop: 0}, 'slow');
        return false;
    });

    // Validate form input
    function validateProductForm(formId) {
        const form = $(formId);
        const name = form.find('[name="name"]').val().trim();
        const price = parseFloat(form.find('[name="price"]').val());
        const stockQuantity = parseInt(form.find('[name="stockQuantity"]').val());
        const discountPercent = parseInt(form.find('[name="discountPercent"]').val()) || 0;

        if (!name) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Tên sản phẩm không được để trống'
            });
            return false;
        }

        if (isNaN(price) || price <= 0) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Giá sản phẩm phải là số dương'
            });
            return false;
        }

        if (isNaN(stockQuantity) || stockQuantity < 0) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Số lượng tồn kho phải là số nguyên không âm'
            });
            return false;
        }

        if (isNaN(discountPercent) || discountPercent < 0 || discountPercent > 100) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Phần trăm giảm giá phải từ 0 đến 100'
            });
            return false;
        }

        return true;
    }

    // Add Product Form Submit
    $('#addProductForm').on('submit', function(e) {
        e.preventDefault();
        
        if (!validateProductForm('#addProductForm')) {
            return;
        }

        // Tạo FormData mới thay vì lấy từ form
        const formData = new FormData();
        
        // Lấy tất cả input thông thường
        formData.append('name', $('#addProductForm [name="name"]').val());
        formData.append('description', $('#addProductForm [name="description"]').val());
        formData.append('price', $('#addProductForm [name="price"]').val());
        formData.append('stockQuantity', $('#addProductForm [name="stockQuantity"]').val());
        formData.append('categoryId', $('#addProductForm [name="categoryId"]').val());
        formData.append('discountPercent', $('#addProductForm [name="discountPercent"]').val());
        
        // Xử lý file
        const imageFile = $('#addProductForm [name="image"]')[0].files[0];
        if (imageFile) {
            formData.append('image', imageFile);
        }
        
        // Xử lý checkbox một cách rõ ràng
        const isNew = $('#isNew').is(':checked');
        const isBestSeller = $('#isBestSeller').is(':checked');
        
        // Gửi giá trị dưới dạng string "true"/"false"
        formData.append('isNew', isNew ? 'true' : 'false');
        formData.append('isBestSeller', isBestSeller ? 'true' : 'false');
        
        // Debug log
        console.log('Add product form data:', {
            isNew: formData.get('isNew'),
            isBestSeller: formData.get('isBestSeller')
        });
        
        $.ajax({
            url: contextPath + '/admin/products/add',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success!',
                        text: 'Product added successfully.'
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error!',
                        text: response.message || 'Failed to add product.'
                    });
                }
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'An error occurred while adding the product.'
                });
            }
        });
    });

    // Edit Product Button Click
    $('.edit-product').click(function() {
        const productId = $(this).data('id');
        
        // Fetch product data
        $.get(contextPath + '/admin/products/get/' + productId, function(product) {
            const form = $('#editProductForm');
            form.find('[name="productId"]').val(product.productId);
            form.find('[name="name"]').val(product.name);
            form.find('[name="description"]').val(product.description);
            form.find('[name="price"]').val(product.price);
            form.find('[name="stockQuantity"]').val(product.stockQuantity);
            form.find('[name="categoryId"]').val(product.categoryId);
            form.find('[name="discountPercent"]').val(product.discountPercent);
            form.find('[name="isNew"]').prop('checked', product.isNew);
            form.find('[name="isBestSeller"]').prop('checked', product.isBestSeller);
            
            $('#editProductModal').modal('show');
        });
    });

    // Edit Product Form Submit
    $('#editProductForm').on('submit', function(e) {
        e.preventDefault();
        
        if (!validateProductForm('#editProductForm')) {
            return;
        }

        // Tạo FormData mới thay vì lấy từ form
        const formData = new FormData();
        
        // Lấy tất cả input thông thường
        formData.append('productId', $('#editProductForm [name="productId"]').val());
        formData.append('name', $('#editProductForm [name="name"]').val());
        formData.append('description', $('#editProductForm [name="description"]').val());
        formData.append('price', $('#editProductForm [name="price"]').val());
        formData.append('stockQuantity', $('#editProductForm [name="stockQuantity"]').val());
        formData.append('categoryId', $('#editProductForm [name="categoryId"]').val());
        formData.append('discountPercent', $('#editProductForm [name="discountPercent"]').val());
        
        // Xử lý file nếu có
        const imageFile = $('#editProductForm [name="image"]')[0].files[0];
        if (imageFile) {
            formData.append('image', imageFile);
        }
        
        // Xử lý checkbox một cách rõ ràng
        const isNew = $('#editIsNew').is(':checked');
        const isBestSeller = $('#editIsBestSeller').is(':checked');
        
        // Gửi giá trị dưới dạng string "true"/"false"
        formData.append('isNew', isNew ? 'true' : 'false');
        formData.append('isBestSeller', isBestSeller ? 'true' : 'false');
        
        // Debug log
        console.log('Form data before send:', {
            isNew: formData.get('isNew'),
            isBestSeller: formData.get('isBestSeller')
        });
        
        const productId = formData.get('productId');
        
        $.ajax({
            url: contextPath + '/admin/products/update/' + productId,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success!',
                        text: 'Product updated successfully.'
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error!',
                        text: response.message || 'Failed to update product.'
                    });
                }
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'An error occurred while updating the product.'
                });
            }
        });
    });

    // Delete Product Button Click
    $('.delete-product').click(function() {
        const productId = $(this).data('id');
        
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $.post(contextPath + '/admin/products/delete/' + productId, function(response) {
                    if (response.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Deleted!',
                            text: 'Product has been deleted.'
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error!',
                            text: response.message || 'Failed to delete product.'
                        });
                    }
                });
            }
        });
    });
});