# Web Bán Gốm Sứ Online: Gốm sứ bát tràng

## Giới Thiệu

Đây là một dự án web bán gốm sứ trực tuyến, cho phép người dùng xem và mua các sản phẩm gốm sứ từ nhiều danh mục khác nhau. Trang web cung cấp tính năng giỏ hàng, thanh toán trực tuyến và hệ thống quản lý đơn hàng cho người dùng và quản trị viên.

## Tính Năng

- Xem danh sách sản phẩm gốm sứ với mô tả và hình ảnh chi tiết.
- Tìm kiếm sản phẩm theo danh mục hoặc từ khóa.
- Quản lý giỏ hàng: thêm, xóa, cập nhật sản phẩm trong giỏ.
- Thanh toán trực tuyến qua cổng thanh toán tích hợp.
- Hệ thống quản lý đơn hàng cho quản trị viên:
  - Xem và cập nhật trạng thái đơn hàng.
  - Quản lý sản phẩm và người dùng.
- Hệ thống cập nhật trạng thái đơn hàng cho shipper bằng cách tìm theo id đơn hàng
- Đăng nhập và đăng kí

## Cài Đặt

### Yêu Cầu Hệ Thống

- Angular 18
- MySQL >= 8.x
- Java 17

## Database
- Diagram: [Link] (https://dbdiagram.io/d/GomSuTASC-67210aa8b4216d5a289f8b94)

### Các Bước Cài Đặt

1. Clone dự án từ GitHub:

   ```bash
   git clone 

   ```

2. Install redis:
    
   ```bash
   docker run -d \
    --name redis \
    -p 6379:6379 \
    -e REDIS_PASSWORD=207205 \
    redis:7.2 \
    redis-server --requirepass 207205

   ```