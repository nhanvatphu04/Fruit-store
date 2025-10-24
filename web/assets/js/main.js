// Lấy contextPath cho các request AJAX
const contextPath = document.querySelector('meta[name="contextPath"]')?.getAttribute('content') || '';

// Khởi tạo autocomplete cho search input
$(document).ready(function() {
    $('.search-input').autocomplete({
        source: function(request, response) {
            $.ajax({
                url: contextPath + '/api/search-suggest',
                dataType: 'json',
                data: {
                    q: request.term
                },
                success: function(data) {
                    response(data.map(function(item) {
                        return {
                            label: item.name,
                            value: item.name,
                            item: item
                        };
                    }));
                }
            });
        },
        minLength: 2,
        select: function(event, ui) {
            // Chuyển đến trang sản phẩm khi chọn item
            window.location.href = contextPath + '/product?id=' + ui.item.item.id;
            return false;
        }
    }).data('ui-autocomplete')._renderItem = function(ul, item) {
        // Tùy chỉnh giao diện của mỗi item trong danh sách gợi ý
        return $('<li>')
            .append('<div class="search-item">' +
                '<img src="' + item.item.image + '" alt="' + item.label + '">' +
                '<div class="search-item-info">' +
                '<div class="search-item-name">' + item.label + '</div>' +
                '<div class="search-item-price">' + 
                new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
                    .format(item.item.price) + 
                '</div>' +
                '</div>' +
                '</div>')
            .appendTo(ul);
    };

    // Tự động cuộn slider nếu có nhiều sản phẩm
    var sliders = document.querySelectorAll('.slider');
    sliders.forEach(function(slider) {
        // Có thể thêm hiệu ứng hoặc nút điều hướng ở đây
    });
});
