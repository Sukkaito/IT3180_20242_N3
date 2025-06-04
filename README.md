# Hệ thống quản lý thư viện

## Giới thiệu
WIP
## Tính năng
WIP
## Cấu trúc
WIP
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
## Hướng dẫn sử dụng
WIP
## Tài liệu tham khảo
WIP
