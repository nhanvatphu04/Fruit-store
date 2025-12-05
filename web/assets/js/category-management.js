$(document).ready(function() {
    // Initialize DataTable
    const categoriesTable = $('#categoriesTable').DataTable({
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
    function validateCategoryForm(formId) {
        const form = $(formId);
        const name = form.find('[name="name"]').val().trim();

        if (!name) {
            Swal.fire({
                icon: 'error',
                title: 'Error!',
                text: 'Category name cannot be empty'
            });
            return false;
        }

        return true;
    }

    // Add Category Form Submit
    $('#addCategoryForm').on('submit', function(e) {
        e.preventDefault();
        
        if (!validateCategoryForm('#addCategoryForm')) {
            return;
        }

        // Create FormData
        const formData = new FormData();
        formData.append('name', $('#addCategoryForm [name="name"]').val());
        formData.append('description', $('#addCategoryForm [name="description"]').val());
        formData.append('icon', $('#addCategoryForm [name="icon"]').val());
        formData.append('slug', $('#addCategoryForm [name="slug"]').val());
        
        // Handle file
        const imageFile = $('#addCategoryForm [name="image"]')[0].files[0];
        if (imageFile) {
            formData.append('image', imageFile);
        }
        
        $.ajax({
            url: contextPath + '/admin/categories/add',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success!',
                        text: 'Category added successfully.'
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error!',
                        text: response.message || 'Failed to add category.'
                    });
                }
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'An error occurred while adding the category.'
                });
            }
        });
    });

    // Edit Category Button Click
    $(document).on('click', '.edit-category', function() {
        const categoryId = $(this).data('id');
        
        // Fetch category data
        $.get(contextPath + '/admin/categories/get/' + categoryId, function(category) {
            const form = $('#editCategoryForm');
            form.find('[name="categoryId"]').val(category.categoryId);
            form.find('[name="name"]').val(category.name);
            form.find('[name="description"]').val(category.description);
            form.find('[name="icon"]').val(category.icon);
            form.find('[name="slug"]').val(category.slug);
            
            $('#editCategoryModal').modal('show');
        });
    });

    // Edit Category Form Submit
    $('#editCategoryForm').on('submit', function(e) {
        e.preventDefault();
        
        if (!validateCategoryForm('#editCategoryForm')) {
            return;
        }

        // Create FormData
        const formData = new FormData();
        formData.append('categoryId', $('#editCategoryForm [name="categoryId"]').val());
        formData.append('name', $('#editCategoryForm [name="name"]').val());
        formData.append('description', $('#editCategoryForm [name="description"]').val());
        formData.append('icon', $('#editCategoryForm [name="icon"]').val());
        formData.append('slug', $('#editCategoryForm [name="slug"]').val());
        
        // Handle file if provided
        const imageFile = $('#editCategoryForm [name="image"]')[0].files[0];
        if (imageFile) {
            formData.append('image', imageFile);
        }
        
        const categoryId = formData.get('categoryId');
        
        $.ajax({
            url: contextPath + '/admin/categories/update/' + categoryId,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.success) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Success!',
                        text: 'Category updated successfully.'
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error!',
                        text: response.message || 'Failed to update category.'
                    });
                }
            },
            error: function() {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'An error occurred while updating the category.'
                });
            }
        });
    });

    // Delete Category Button Click
    $(document).on('click', '.delete-category', function() {
        const categoryId = $(this).data('id');
        
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
                $.post(contextPath + '/admin/categories/delete/' + categoryId, function(response) {
                    if (response.success) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Deleted!',
                            text: 'Category has been deleted.'
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error!',
                            text: response.message || 'Failed to delete category.'
                        });
                    }
                });
            }
        });
    });
});
