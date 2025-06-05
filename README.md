# 📚 Hệ thống quản lý thư viện

## 🧩 Giới thiệu
Hệ thống quản lý thư viện được phát triển nhằm hỗ trợ quản lý sách, độc giả, nhân viên và các hoạt động mượn/trả sách, đặt chỗ và thống kê trong thư viện. Ứng dụng hướng tới việc tối ưu hóa quy trình vận hành, tăng hiệu quả phục vụ người dùng và giảm tải cho nhân viên thư viện.

---

## 👥 Nhóm sinh viên thực hiện

|STT | Tên               | MSSV     |
|----|-------------------|----------|
|1   | Trần Nam Phong    | 20225061 |
|2   | Đinh Văn Thương   | 20225098 |
|3   | Nguyễn Hoàng Nam  | 20225213 |
|4   | Vũ Tiến Chiến     | 20235279 |
|5   | Nguyễn Đăng Quân  | 20235406 |
|6   | Đặng Phan Bình    | 20235273 |
|7   | Trương Việt Hoàng | 20235337 |
|8   | Nguyễn Ngọc Sơn   | 20235416 |

---

## 🚀 Tính năng

1. **Quản lý sách**
   - Thêm, cập nhật, xóa sách.
   - Quản lý thông tin: tên sách, tác giả, thể loại, số lượng, v.v.

2. **Quản lý độc giả**
   - Tạo và chỉnh sửa tài khoản.
   - Theo dõi lịch sử mượn/trả.
   - Nhắc nhở khi gần đến hạn trả.
   - Hiển thị trạng thái tài khoản.

3. **Quản lý mượn/trả và phí phạt**
   - Ghi nhận việc mượn/trả sách.
   - Tính toán và xử lý phí phạt nếu quá hạn.

4. **Tìm kiếm sách**
   - Tìm kiếm sách theo tên, tác giả, thể loại...

5. **Quản lý đặt chỗ sách**
   - Đặt chỗ khi sách đang được mượn.
   - Thông báo khi sách có sẵn.

6. **Thống kê và báo cáo**
   - Thống kê số lượt mượn, thể loại phổ biến...
   - Biểu đồ trực quan và bảng dữ liệu tổng hợp.

7. **Quản lý nhân viên thư viện**
   - Tạo tài khoản nhân viên, phân quyền.
   - Ghi nhận hoạt động của nhân viên.

8. **Quản lý chi phí và thanh toán**
   - Theo dõi phí vận hành (điện, nước, dịch vụ...).
   - Cập nhật thông tin thanh toán.

9. **Quản lý hệ thống**
   - Cấu hình quyền truy cập.
   - Sao lưu, phục hồi dữ liệu.
   - Kiểm tra trạng thái máy chủ, xử lý lỗi hệ thống.

---

## 🛠️ Tech Stack

| **Component**         | **Technology**                           |
|-----------------------|-------------------------------------------|
| **Frontend**          | React.js, TypeScript, Vite, Tailwind CSS  |
| **Backend**           | Spring Boot                               |
| **Database**          | PostgreSQL                                |
| **Authentication**    | Spring Security, JWT                      |
| **Containerization**  | Docker                                    |
| **Deployment**        | Docker Compose                            |
| **Version Control**   | Git, GitHub                               |

---

## 📁 Cấu trúc
```plaintext
IT3180_20242_N3/
├── backend/               # Spring Boot Application
│   ├── src/
│   └── build.gradle
├── frontend/              # React + TypeScript + Vite
│   ├── src/
│   └── package.json
├── LICENSE                # Giấy phép dự án
└── README.md              # Tài liệu dự án
```
---

## Cài đặt
### I. Cài đặt backend và database (dev):
- Cài đặt [Docker](https://www.docker.com/) và khởi động.
- (Optional) Kiểm tra trạng thái hoạt động của Docker daemon:
  - Windows:
    ```batch
    tasklist | findstr docker
    ```
    ![image](https://github.com/user-attachments/assets/c18843ad-1ac8-4b9d-b6be-d78c27744273)

    Nếu hiện không đủ hoặc không có kết quả, hãy thử khởi động lại Docker Desktop.
  - Unix: [Tham khảo](https://docs.docker.com/engine/daemon/troubleshoot)
    ```bash
    sudo systemctl status docker
    ```
    ![image](https://github.com/user-attachments/assets/0fbe4bc7-6c1d-432d-9051-80a1d5193a10)
    Nếu không tìm thấy service, hoặc service không hiện tình trạng active, hãy thử khởi động lại Docker Destkop hoặc Docker Engine.

- PostgresSQL đã được cấu hình để chạy cùng với ứng dụng (Trong thực tế, có thể dùng PostgresSQL trên server khác.
Tham khảo: [Tài liệu](https://docs.spring.io/spring-boot/appendix/application-properties/index.html#appendix.application-properties.data))
- Clone repository về máy và chuyển đến thư mục `backend`:
```batch
git clone https://github.com/Sukkaito/IT3180_20242_N3.git
cd IT3180_20242_N3/backend
```
- Khởi chạy ứng dụng:
```batch
gradlew bootRun
```
### II. Cài đặt frontend:
- Cài đặt [NodeJS](https://nodejs.org/en/download) v22.14.0+
- (Optional) Kiểm tra tình trạng cài đặt NodeJS:
```batch
node --version
```
![image](https://github.com/user-attachments/assets/5dd61710-ce9e-4c25-b213-aa9191e96ac6)

Nếu hiện 1 dòng thông tin về số phiên bản NodeJS, cài đặt đã thành công.
- Chuyển đến thư mục `frontend`, tiến hành cài đặt các dependency:
```batch
npm install
```
- Khởi chạy ứng dụng:
```batch
npm run dev
```

---

## 🧭 Hướng dẫn sử dụng

- Truy cập ứng dụng tại địa chỉ: [http://localhost:5173](http://localhost:5173)

- Đăng nhập bằng tài khoản tương ứng với vai trò:

  - **Độc giả**: Có thể tìm kiếm sách, đặt chỗ sách, và thực hiện mượn sách.
  - **Nhân viên**: Xử lý các nghiệp vụ mượn và trả sách, thêm mới hoặc cập nhật thông tin sách.
  - **Admin**: Quản lý hệ thống toàn diện, bao gồm quản lý nhân viên, phân quyền, cũng như theo dõi và xuất báo cáo thống kê.

- Sau khi đăng nhập, sử dụng giao diện trực quan để thực hiện các chức năng phù hợp với quyền hạn của bạn.

- Giao diện hỗ trợ thao tác dễ dàng, thân thiện với người dùng giúp quản lý và sử dụng thư viện hiệu quả hơn.

---

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Vite](https://vitejs.dev/)
- [React + TypeScript](https://reactjs.org/docs/static-type-checking.html)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [JSON Web Tokens (JWT)](https://jwt.io/introduction)
