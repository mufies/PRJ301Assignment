function openLoginModal() {
            document.getElementById('loginModal').classList.add('active');
        }

        function closeLoginModal() {
            document.getElementById('loginModal').classList.remove('active');
        }

        function openForgotPassword() {
            document.getElementById('forgotPasswordModal').classList.add('active');
        }

        function closeForgotPassword() {
            document.getElementById('forgotPasswordModal').classList.remove('active');
            document.getElementById('enterForgotPasswordModal').classList.remove('active');
        }

        function openEnterForgotPassword() {
            document.getElementById('enterForgotPasswordModal').classList.add('active');
        }

        function openUserModal() {
            const jwt = localStorage.getItem('jwt');
            if (isJwtValid(jwt)) {
                document.getElementById('loggedModal').classList.add('active');
            } else {
                openLoginModal();
            }
        }

        function closeLoggedModal() {
            document.getElementById('loggedModal').classList.remove('active');
        }

        function logout() {
            localStorage.removeItem('jwt');
            const loginBtn = document.querySelector('.login-btn');
            if (loginBtn) {
                loginBtn.innerHTML = 'Login';
                loginBtn.onclick = openLoginModal;
            }
            closeLoggedModal();
            if (typeof updateCartCount === 'function') updateCartCount();
            window.location.reload();
        }

        function isJwtValid(token) {
            if (!token) return false;
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                return !payload.exp || (Date.now() / 1000 < payload.exp);
            } catch (e) {
                return false;
            }
        }

        // Handle modal clicks outside
        window.onclick = function(event) {
            document.querySelectorAll('.modal').forEach(modal => {
                if (event.target === modal) {
                    modal.classList.remove('active');
                }
            });
        };

        // Set up event handlers when DOM is loaded
        document.addEventListener('DOMContentLoaded', function() {
            // Update login button state
            const jwt = localStorage.getItem('jwt');
            const loginBtn = document.querySelector('.login-btn');

            if (loginBtn) {
                if (isJwtValid(jwt)) {
                    loginBtn.innerHTML = '<i class="fa-solid fa-user" style="font-size: 15px"></i>';
                    loginBtn.onclick = openUserModal;
                } else {
                    loginBtn.innerHTML = 'Login';
                    loginBtn.onclick = openLoginModal;
                }
            }

            // Set up login form handler
            const loginForm = document.querySelector('#loginModal form');
            if (loginForm) {
                loginForm.onsubmit = async function(e) {
                    e.preventDefault();

                    const submitBtn = this.querySelector('button[type="submit"]');
                    const errorMsg = this.querySelector('.error-msg') || (() => {
                        const p = document.createElement('p');
                        p.className = 'error-msg';
                        p.style.color = 'red';
                        p.style.fontSize = '14px';
                        p.style.marginTop = '8px';
                        this.appendChild(p);
                        return p;
                    })();

                    submitBtn.disabled = true;
                    submitBtn.textContent = "Đang đăng nhập...";

                    try {
                        const formData = new FormData(this);
                        const response = await fetch('login', {
                            method: 'POST',
                            body: formData
                        });

                        const data = await response.json();

                        if (data.success) {
                            localStorage.setItem('jwt', data.token);
                            closeLoginModal();

                            const loginBtn = document.querySelector('.login-btn');
                            if (loginBtn) {
                                loginBtn.innerHTML = '<i class="fa-solid fa-user" style="font-size: 15px"></i>';
                                loginBtn.onclick = openUserModal;
                            }

                            if (data.isAdmin) {
                                window.location.href = 'ayxkix';
                            } else if (data.isEmployee) {
                                window.location.href = 'employeeMsg';
                            } else {
                                window.location.href = 'menu';
                            }
                        } else {
                            errorMsg.textContent = data.errorMessage || 'Sai tài khoản hoặc mật khẩu.';
                        }
                    } catch (error) {
                        errorMsg.textContent = 'Lỗi khi kết nối server.';
                        console.error(error);
                    } finally {
                        submitBtn.disabled = false;
                        submitBtn.textContent = "Đăng nhập";
                    }
                };
            }

            // Set up forgot password form handler
            const forgotForm = document.querySelector('#forgotPasswordModal form');
            if (forgotForm) {
                forgotForm.onsubmit = async function(e) {
                    e.preventDefault();

                    const submitBtn = this.querySelector('button[type="submit"]');
                    const errorMsg = this.querySelector('.error-msg') || (() => {
                        const p = document.createElement('p');
                        p.className = 'error-msg';
                        p.style.color = 'red';
                        p.style.fontSize = '14px';
                        p.style.marginTop = '8px';
                        this.appendChild(p);
                        return p;
                    })();

                    submitBtn.disabled = true;
                    submitBtn.textContent = "Đang gửi...";

                    try {
                        const email = this.querySelector('#email').value;
                        const formData = new URLSearchParams();
                        formData.append('email', email);

                        const response = await fetch('forgotpassword', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: formData
                        });

                        const data = await response.json();

                        if (data.success) {
                            closeForgotPassword();
                            openEnterForgotPassword();
                        } else {
                            errorMsg.textContent = data.message;
                        }
                    } catch (error) {
                        errorMsg.textContent = 'Lỗi khi kết nối server.';
                        console.error(error);
                    } finally {
                        submitBtn.disabled = false;
                        submitBtn.textContent = "Gửi mã xác nhận";
                    }
                };
            }
        });