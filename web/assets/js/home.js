// Product slider functionality
function initProductSliders() {
    const sliders = {
        bestSellers: document.getElementById('bestSellersSlider'),
        newProducts: document.getElementById('newProductsSlider')
    };

    for (const [id, slider] of Object.entries(sliders)) {
        if (!slider) continue;

        const prevBtn = slider.parentElement.querySelector('.slider-nav.prev');
        const nextBtn = slider.parentElement.querySelector('.slider-nav.next');

        prevBtn?.addEventListener('click', () => slideProducts(id, 'prev'));
        nextBtn?.addEventListener('click', () => slideProducts(id, 'next'));
    }
}

function slideProducts(sliderId, direction) {
    const slider = document.getElementById(sliderId + 'Slider');
    if (!slider) return;

    const scrollAmount = direction === 'next' ? 800 : -800;
    slider.scrollBy({
        left: scrollAmount,
        behavior: 'smooth'
    });
}

// Flash sale countdown - removed as we use FlipDown library in JSP

// Cart functionality - Direct add to cart (for simple cases)
async function addToCartDirect(productId) {
    try {
        const response = await fetch(contextPath + '/cart/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'productId=' + productId
        });
        
        const data = await response.json();
        handleCartResponse(data);
    } catch (error) {
        showError('Không thể thêm sản phẩm vào giỏ hàng');
    }
}

async function addComboToCart(comboId) {
    try {
        const response = await fetch(contextPath + '/cart/add-combo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'comboId=' + comboId
        });
        
        const data = await response.json();
        handleCartResponse(data);
    } catch (error) {
        showError('Không thể thêm combo vào giỏ hàng');
    }
}

function handleCartResponse(data) {
    if (data.success) {
        Swal.fire({
            title: 'Thành công!',
            text: 'Đã thêm vào giỏ hàng',
            icon: 'success',
            timer: 1500,
            showConfirmButton: false
        });
        
        // Update cart count badge
        updateCartCountBadge();
    } else {
        showError(data.message || 'Có lỗi xảy ra');
    }
}

function showError(message) {
    Swal.fire({
        title: 'Lỗi!',
        text: message,
        icon: 'error'
    });
}

// Function to update cart count badge
function updateCartCountBadge() {
    fetch(contextPath + '/cart/count')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const badge = document.querySelector('.cart-count');
                if (badge) {
                    badge.textContent = data.count;
                }
            }
        })
        .catch(error => {
            console.error('Error updating cart count:', error);
        });
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Initialize components
    initProductSliders();

    // Add to cart buttons
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;
            if (productId) {
                addToCartDirect(productId);
            }
        });
    });

    // Add combo buttons
    document.querySelectorAll('.add-combo').forEach(button => {
        button.addEventListener('click', () => {
            const comboId = button.dataset.comboId;
            if (comboId) {
                addComboToCart(comboId);
            }
        });
    });
});