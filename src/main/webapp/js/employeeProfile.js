window.onload = function() {
            const jwtToken = localStorage.getItem("jwt");
            if (!jwtToken) {
                alert("Bạn chưa đăng nhập hoặc token hết hạn!");
                return;
            }

            fetch("updateEmployee", {
                method: "POST",
                headers: {
                    "Authorization": "getData " + jwtToken
                }
            })
            .then(response => {
                if (response.ok) return response.json();
                else throw new Error("Không thể lấy thông tin nhân viên, mã lỗi: " + response.status);
            })
            .then(data => {
                document.getElementsByName("username")[0].value = data.username || "";
                document.getElementsByName("password")[0].value = data.password || "";
                document.getElementsByName("fullName")[0].value = data.fullName || "";
                document.getElementsByName("email")[0].value = data.email || "";
                document.getElementsByName("phone")[0].value = data.phone || "";
                document.getElementsByName("address")[0].value = data.address || "";


                document.getElementsByName("employee_id")[0].value = data.employeeId || "";
            })
            .catch(error => {
                console.error("Lỗi khi lấy thông tin:", error);
                alert("Có lỗi khi tải thông tin nhân viên.");
            });
        };

        function updateEmployeeInfo() {
            const jwtToken = localStorage.getItem("jwt");
            const employeeId = document.getElementsByName("employee_id")[0].value;
            const password = document.getElementsByName("password")[0].value.trim();
            const fullName = document.getElementsByName("fullName")[0].value.trim();
            const email = document.getElementsByName("email")[0].value.trim();
            const phone = document.getElementsByName("phone")[0].value.trim();
            const address = document.getElementsByName("address")[0].value.trim();

            fetch("updateEmployee", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "updateData " + jwtToken
                },
                body: JSON.stringify({
                    employee_id: employeeId,
                    password: password,
                    fullName: fullName,
                    email: email,
                    phone: phone,
                    address: address
                })
            })
            .then(response => {
                if (response.ok) {
                    alert("Cập nhật thành công!");
                    window.location.reload();
                } else {
                    throw new Error("Cập nhật thất bại, mã lỗi: " + response.status);
                }
            })
            .catch(error => {
                console.error("Lỗi khi cập nhật:", error);
                alert("Có lỗi xảy ra, vui lòng thử lại sau.");
            });
        }