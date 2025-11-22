<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Tài khoản - FruitStore</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
            <style>
                .profile-section {
                    padding: 2rem 0;
                    background-color: #f8f9fa;
                }

                .profile-card {
                    background: white;
                    padding: 2rem;
                    border-radius: 10px;
                    box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
                }

                .avatar-wrapper {
                    width: 150px;
                    height: 150px;
                    margin: 0 auto 1rem;
                    position: relative;
                }

                .avatar-wrapper img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                    border-radius: 50%;
                    border: 3px solid #28a745;
                }

                .avatar-upload {
                    position: absolute;
                    bottom: 0;
                    right: 0;
                    background: #28a745;
                    width: 35px;
                    height: 35px;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    cursor: pointer;
                    color: white;
                }

                .btn-success {
                    background-color: #28a745;
                    border-color: #28a745;
                }

                .btn-success:hover {
                    background-color: #218838;
                    border-color: #1e7e34;
                }

                .avatar-option {
                    cursor: pointer;
                    padding: 5px;
                    border: 2px solid transparent;
                    transition: all 0.3s;
                }

                .avatar-option:hover {
                    border-color: #28a745;
                }

                .avatar-option.selected {
                    border-color: #28a745;
                    box-shadow: 0 0 10px rgba(40, 167, 69, 0.3);
                }
            </style>
        </head>

        <body>
            <!-- Include Header -->
            <jsp:include page="/jsp/common/header.jsp" />

            <main>
                <section class="profile-section">
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-md-8">
                                <div class="profile-card">
                                    <h2 class="text-center mb-4">Thông tin tài khoản</h2>

                                    <c:if test="${not empty success}">
                                        <div class="alert alert-success" role="alert">
                                            ${success}
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger" role="alert">
                                            ${error}
                                        </div>
                                    </c:if>

                                    <form action="${pageContext.request.contextPath}/profile" method="POST"
                                        id="profileForm">
                                        <div class="avatar-wrapper mb-4">
                                            <img src="${pageContext.request.contextPath}${empty sessionScope.user.avatarUrl ? '/assets/images/avatars/default.png' : sessionScope.user.avatarUrl}"
                                                alt="Avatar" id="avatarPreview">
                                            <button type="button" class="avatar-upload" data-bs-toggle="modal"
                                                data-bs-target="#avatarModal">
                                                <i class="fas fa-image"></i>
                                            </button>
                                            <input type="hidden" name="selectedAvatar" id="selectedAvatar"
                                                value="${sessionScope.user.avatarUrl}">
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="username" class="form-label">Tên đăng nhập</label>
                                                <input type="text" class="form-control" id="username"
                                                    value="${sessionScope.user.username}" readonly>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="email" class="form-label">Email</label>
                                                <input type="email" class="form-control" id="email" name="email"
                                                    value="${sessionScope.user.email}">
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="fullName" class="form-label">Họ và tên</label>
                                            <input type="text" class="form-control" id="fullName" name="fullName"
                                                value="${sessionScope.user.fullName}">
                                        </div>

                                        <div class="mb-3">
                                            <label for="phone" class="form-label">Số điện thoại</label>
                                            <input type="tel" class="form-control" id="phone" name="phone"
                                                pattern="[0-9]{10}" title="Số điện thoại phải có 10 chữ số"
                                                value="${sessionScope.user.phone}">
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-4">
                                                <label for="province" class="form-label">Tỉnh/Thành phố</label>
                                                <select class="form-select" id="province" name="province">
                                                    <option value="">Chọn Tỉnh/Thành phố</option>
                                                </select>
                                            </div>
                                            <div class="col-md-4">
                                                <label for="district" class="form-label">Quận/Huyện</label>
                                                <select class="form-select" id="district" name="district" disabled>
                                                    <option value="">Chọn Quận/Huyện</option>
                                                </select>
                                            </div>
                                            <div class="col-md-4">
                                                <label for="ward" class="form-label">Phường/Xã</label>
                                                <select class="form-select" id="ward" name="ward" disabled>
                                                    <option value="">Chọn Phường/Xã</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="detailedAddress" class="form-label">Địa chỉ chi tiết</label>
                                            <input type="text" class="form-control" id="detailedAddress"
                                                name="detailedAddress" placeholder="Số nhà, tên đường...">
                                        </div>

                                        <input type="hidden" id="address" name="address"
                                            value="${sessionScope.user.address}">

                                        <div class="mb-3">
                                            <label for="currentPassword" class="form-label">Mật khẩu hiện tại</label>
                                            <input type="password" class="form-control" id="currentPassword"
                                                name="currentPassword">
                                            <small class="text-muted">Chỉ cần nhập khi muốn đổi mật khẩu</small>
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                                <input type="password" class="form-control" id="newPassword"
                                                    name="newPassword" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"
                                                    title="Mật khẩu phải có ít nhất 8 ký tự, bao gồm cả chữ và số">
                                            </div>
                                            <div class="col-md-6">
                                                <label for="confirmPassword" class="form-label">Xác nhận mật khẩu
                                                    mới</label>
                                                <input type="password" class="form-control" id="confirmPassword"
                                                    name="confirmPassword">
                                            </div>
                                        </div>

                                        <div class="text-center">
                                            <button type="submit" class="btn btn-success">
                                                <i class="fas fa-save me-2"></i>Cập nhật
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </main>

            <!-- Include Footer -->
            <jsp:include page="/jsp/common/footer.jsp" />

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

            <!-- Modal Chọn Avatar -->
            <div class="modal fade" id="avatarModal" tabindex="-1" aria-labelledby="avatarModalLabel"
                aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="avatarModalLabel">Chọn Avatar</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row g-3">
                                <div class="col-4 col-md-3">
                                    <div class="avatar-option" data-avatar="/assets/images/avatars/avatar1.png">
                                        <img src="${pageContext.request.contextPath}/assets/images/avatars/avatar1.png"
                                            class="img-fluid rounded" alt="Avatar 1">
                                    </div>
                                </div>
                                <div class="col-4 col-md-3">
                                    <div class="avatar-option" data-avatar="/assets/images/avatars/avatar2.png">
                                        <img src="${pageContext.request.contextPath}/assets/images/avatars/avatar2.png"
                                            class="img-fluid rounded" alt="Avatar 2">
                                    </div>
                                </div>
                                <div class="col-4 col-md-3">
                                    <div class="avatar-option" data-avatar="/assets/images/avatars/avatar3.png">
                                        <img src="${pageContext.request.contextPath}/assets/images/avatars/avatar3.png"
                                            class="img-fluid rounded" alt="Avatar 3">
                                    </div>
                                </div>
                                <div class="col-4 col-md-3">
                                    <div class="avatar-option" data-avatar="/assets/images/avatars/avatar4.png">
                                        <img src="${pageContext.request.contextPath}/assets/images/avatars/avatar4.png"
                                            class="img-fluid rounded" alt="Avatar 4">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                $(document).ready(function () {
                    // Province Open API Integration
                    const API_BASE_URL = 'https://provinces.open-api.vn/api';

                    // Load provinces on page load
                    loadProvinces();

                    // Parse existing address if available
                    parseExistingAddress();

                    function loadProvinces() {
                        $.ajax({
                            url: API_BASE_URL + '/p/',
                            method: 'GET',
                            success: function (data) {
                                $('#province').empty().append('<option value="">Chọn Tỉnh/Thành phố</option>');
                                data.forEach(function (province) {
                                    $('#province').append(
                                        $('<option></option>')
                                            .attr('value', province.code)
                                            .attr('data-name', province.name)
                                            .text(province.name)
                                    );
                                });
                            },
                            error: function () {
                                console.error('Không thể tải danh sách tỉnh/thành phố');
                            }
                        });
                    }

                    // Province change event
                    $('#province').change(function () {
                        const provinceCode = $(this).val();

                        // Reset district and ward
                        $('#district').empty().append('<option value="">Chọn Quận/Huyện</option>').prop('disabled', true);
                        $('#ward').empty().append('<option value="">Chọn Phường/Xã</option>').prop('disabled', true);

                        if (provinceCode) {
                            loadDistricts(provinceCode);
                            updateFullAddress();
                        }
                    });

                    function loadDistricts(provinceCode) {
                        $.ajax({
                            url: API_BASE_URL + '/p/' + provinceCode + '?depth=2',
                            method: 'GET',
                            success: function (data) {
                                $('#district').empty().append('<option value="">Chọn Quận/Huyện</option>');
                                if (data.districts) {
                                    data.districts.forEach(function (district) {
                                        $('#district').append(
                                            $('<option></option>')
                                                .attr('value', district.code)
                                                .attr('data-name', district.name)
                                                .text(district.name)
                                        );
                                    });
                                    $('#district').prop('disabled', false);
                                }
                            },
                            error: function () {
                                console.error('Không thể tải danh sách quận/huyện');
                            }
                        });
                    }

                    // District change event
                    $('#district').change(function () {
                        const districtCode = $(this).val();

                        // Reset ward
                        $('#ward').empty().append('<option value="">Chọn Phường/Xã</option>').prop('disabled', true);

                        if (districtCode) {
                            loadWards(districtCode);
                            updateFullAddress();
                        }
                    });

                    function loadWards(districtCode) {
                        $.ajax({
                            url: API_BASE_URL + '/d/' + districtCode + '?depth=2',
                            method: 'GET',
                            success: function (data) {
                                $('#ward').empty().append('<option value="">Chọn Phường/Xã</option>');
                                if (data.wards) {
                                    data.wards.forEach(function (ward) {
                                        $('#ward').append(
                                            $('<option></option>')
                                                .attr('value', ward.code)
                                                .attr('data-name', ward.name)
                                                .text(ward.name)
                                        );
                                    });
                                    $('#ward').prop('disabled', false);
                                }
                            },
                            error: function () {
                                console.error('Không thể tải danh sách phường/xã');
                            }
                        });
                    }

                    // Ward change event
                    $('#ward').change(function () {
                        updateFullAddress();
                    });

                    // Detailed address change event
                    $('#detailedAddress').on('input', function () {
                        updateFullAddress();
                    });

                    // Update full address in hidden field
                    function updateFullAddress() {
                        const detailedAddress = $('#detailedAddress').val().trim();
                        const wardName = $('#ward').find('option:selected').data('name') || '';
                        const districtName = $('#district').find('option:selected').data('name') || '';
                        const provinceName = $('#province').find('option:selected').data('name') || '';

                        const addressParts = [detailedAddress, wardName, districtName, provinceName].filter(part => part);
                        const fullAddress = addressParts.join(', ');

                        $('#address').val(fullAddress);
                    }

                    // Parse existing address and populate fields
                    function parseExistingAddress() {
                        const existingAddress = $('#address').val();
                        if (!existingAddress || existingAddress.trim() === '') {
                            return;
                        }

                        // Split address: "123, Phường Tân Quang, Thành phố Tuyên Quang, Tỉnh Tuyên Quang"
                        const parts = existingAddress.split(',').map(p => p.trim());

                        if (parts.length === 0) {
                            return;
                        }

                        // First part is detailed address
                        $('#detailedAddress').val(parts[0]);

                        // Wait for provinces to load, then match
                        setTimeout(function () {
                            if (parts.length >= 2) {
                                const provinceName = parts[parts.length - 1];

                                // Find and select province
                                $('#province option').each(function () {
                                    const optionText = $(this).text();
                                    if (optionText && provinceName.includes(optionText.replace('Tỉnh ', '').replace('Thành phố ', ''))) {
                                        const provinceCode = $(this).val();
                                        $('#province').val(provinceCode);

                                        // Load and select district
                                        if (provinceCode && parts.length >= 3) {
                                            const districtName = parts[parts.length - 2];

                                            $.ajax({
                                                url: API_BASE_URL + '/p/' + provinceCode + '?depth=2',
                                                method: 'GET',
                                                success: function (data) {
                                                    $('#district').empty().append('<option value="">Chọn Quận/Huyện</option>');
                                                    if (data.districts) {
                                                        data.districts.forEach(function (district) {
                                                            $('#district').append(
                                                                $('<option></option>')
                                                                    .attr('value', district.code)
                                                                    .attr('data-name', district.name)
                                                                    .text(district.name)
                                                            );
                                                        });
                                                        $('#district').prop('disabled', false);

                                                        // Find and select district
                                                        $('#district option').each(function () {
                                                            const optionText = $(this).text();
                                                            if (optionText && districtName.includes(optionText.replace('Quận ', '').replace('Huyện ', '').replace('Thành phố ', '').replace('Thị xã ', ''))) {
                                                                const districtCode = $(this).val();
                                                                $('#district').val(districtCode);

                                                                // Load and select ward
                                                                if (districtCode && parts.length >= 4) {
                                                                    const wardName = parts[parts.length - 3];

                                                                    $.ajax({
                                                                        url: API_BASE_URL + '/d/' + districtCode + '?depth=2',
                                                                        method: 'GET',
                                                                        success: function (data) {
                                                                            $('#ward').empty().append('<option value="">Chọn Phường/Xã</option>');
                                                                            if (data.wards) {
                                                                                data.wards.forEach(function (ward) {
                                                                                    $('#ward').append(
                                                                                        $('<option></option>')
                                                                                            .attr('value', ward.code)
                                                                                            .attr('data-name', ward.name)
                                                                                            .text(ward.name)
                                                                                    );
                                                                                });
                                                                                $('#ward').prop('disabled', false);

                                                                                // Find and select ward
                                                                                $('#ward option').each(function () {
                                                                                    const optionText = $(this).text();
                                                                                    if (optionText && wardName.includes(optionText.replace('Phường ', '').replace('Xã ', '').replace('Thị trấn ', ''))) {
                                                                                        $('#ward').val($(this).val());
                                                                                        return false;
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                                return false;
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                        return false;
                                    }
                                });
                            }
                        }, 800);
                    }

                    // Xử lý chọn avatar
                    $('.avatar-option').click(function () {
                        $('.avatar-option').removeClass('selected');
                        $(this).addClass('selected');

                        const avatarPath = $(this).data('avatar');
                        $('#selectedAvatar').val(avatarPath);
                        $('#avatarPreview').attr('src', '${pageContext.request.contextPath}' + avatarPath);
                        $('#avatarModal').modal('hide');
                    });

                    // Validation khi submit form
                    $('#profileForm').submit(function (e) {
                        const newPassword = $('#newPassword').val();
                        const confirmPassword = $('#confirmPassword').val();
                        const currentPassword = $('#currentPassword').val();

                        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
                        if (newPassword || confirmPassword) {
                            if (!currentPassword) {
                                e.preventDefault();
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Lỗi!',
                                    text: 'Vui lòng nhập mật khẩu hiện tại!'
                                });
                                return;
                            }

                            if (newPassword !== confirmPassword) {
                                e.preventDefault();
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Lỗi!',
                                    text: 'Mật khẩu xác nhận không khớp!'
                                });
                                return;
                            }
                        }
                    });
                });
            </script>
        </body>

        </html>