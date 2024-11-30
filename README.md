# Dự án Xây dựng website bán gốm sứ Online: 
#### Được triển khai theo mô hình microservice, trong đó em có được giao cho làm 2 service:product, order và payment(em được chỉ định dùng MoMo để xử lý thanh toán), caching product và lưu cart.

#### 1: Kafka (Dùng gửi email cho khách hàng)
Khi khách hàng đặt hàng thành công:
- Kafka sử dụng để gửi thông báo email tới người dùng. Producer gửi thông điệp về việc đặt hàng thành công vào Kafka, và Consumer xử lý thông điệp đó để gửi email.
Khi hệ thống lỗi:
- Nếu có lỗi trong hệ thống, Kafka ghi nhận lỗi và Consumer gửi email thông báo lỗi tới khách hàng.

Khi kho không đủ hàng:
- Nếu không đủ hàng trong kho, Kafka lưu thông điệp và Consumer gửi email thông báo hết hàng đến khách hàng.
Lưu trữ thông điệp khi có sự cố: Kafka sẽ lưu trữ tất cả các thông điệp trong khi hệ thống có thể xử lý lại sau, nếu có sự cố xảy ra.
Xử lý nhiều email cùng lúc: Kafka giúp xử lý hàng loạt email một cách song song, giảm tải cho hệ thống và tăng cường hiệu suất.
Ghi nhận và xử lý lại thông điệp khi có sự cố: Nếu một sự cố xảy ra, Kafka sẽ đảm bảo thông điệp không bị mất và có thể được xử lý lại khi hệ thống ổn định.

2: Multi-Thread (Khi thanh toán không nhận được phản hồi từ MoMo)
- Em tạo một job chạy định kỳ Mỗi 1 phút để quét các giao dịch đã tạo mà vẫn trong trạng thái chờ đã hơn 15 phút mà chưa nhận được callback.
Job sẽ kiểm tra các giao dịch có trạng thái pending và xác định xem các giao dịch này có vượt quá thời gian chờ hay không.
Nếu có, hệ thống sẽ thực hiện kiểm tra lại hoặc thử lại các giao dịch đó. Việc sử dụng multi-threading thông qua ExecutorService (Scheduled Thread Pool) giúp xử lý đồng thời nhiều giao dịch mà không tạo ra quá nhiều thread dư thừa.
(Em sẽ giới hạn số lượng thread tối thiểu và tối đa dựa trên số luồng CPU của máy),
từ đó tối ưu hóa hiệu suất mà không làm quá tải tài nguyên hệ thống.

- synchronized : Khi thanh toán thành công và em khi quantity sản phẩm thì em dùng để đồng bộ dữ liệu

3.caching product em lưu trên redis vì redis lưu trong ram thay vì ổ cứng
- Em tạo ra 2 job chạy định kì, 1 job 1 ngày 1 lần để cache lại dữ liệu của product 
- Job 2 em chạy 10p 1 lần để tìm những product nào đã update thì cập nhật lại lên redis

4.Lưu cart: em lưu cart vào redis để giảm tải cho database vì giỏ hàng thường được update sửa xóa liên tục
- Em tạo 1 job chạy định kì, 10p một lần để lưu dữ liệu giỏ hàng vào trong database
- khi xóa giỏ hàng em xóa trong database trước sau đó mới xóa ở redis để đồng nhất dữ liệu
- Em giới hạn giỏ hàng chỉ lưu tối đa 150 sản phẩm, nếu vượt quá sẽ ko được thêm
- Em lưu cả vào database phòng trường hợp lỗi kết nối đến redis thì mình sẽ lấy dữ liệu từ database ra để hiển thị cho người dùng














