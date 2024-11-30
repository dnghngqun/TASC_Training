package com.tasc.hongquan.paymentservice.task;

import com.tasc.hongquan.paymentservice.dto.PaymentDataDTO;
import com.tasc.hongquan.paymentservice.models.momo.PaymentResponse;
import com.tasc.hongquan.paymentservice.models.momo.QueryStatusTransactionResponse;
import com.tasc.hongquan.paymentservice.repositories.PaymentRepository;
import com.tasc.hongquan.paymentservice.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@AllArgsConstructor
public class PaymentSchedule {
    private final PaymentService paymentService;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
    private List<PaymentDataDTO> listTransaction;
    private List<Future<QueryStatusTransactionResponse>> futures;

    @Scheduled(fixedRate = 1000 * 60 * 5) //5 minutes
    public void checkTransaction() {

        // Lấy danh sách giao dịch cần kiểm tra
        List<PaymentDataDTO> listTransaction = paymentService.checkTransaction();
        List<Future<QueryStatusTransactionResponse>> futures = new ArrayList<>();

        if (!listTransaction.isEmpty()) {
            for (PaymentDataDTO transaction : listTransaction) {
                futures.add(
                        executorService.submit(() ->
                                paymentService.checkStatusPayment(transaction.getOrderId(), transaction.getPaymentId())
                        )
                );
            }
        }

        // Xử lý kết quả
        for (Future<QueryStatusTransactionResponse> future : futures) {
            try {
                QueryStatusTransactionResponse result = future.get();
                System.out.println("Kết quả: " + result);

                switch (result.getResultCode()) {
                    case 0 -> {
                        System.out.println("Giao dịch thành công.");
                        // TODO: Cập nhật trạng thái giao dịch trong hệ thống
                    }
                    case 10 -> System.out.println("Hệ thống đang được bảo trì. Vui lòng thử lại sau.");
                    case 11 -> System.out.println("Truy cập bị từ chối: Cấu hình tài khoản không phù hợp.");
                    case 12 -> System.out.println("Phiên bản API không được hỗ trợ.");
                    case 13 -> System.out.println("Xác thực doanh nghiệp thất bại.");
                    case 20 -> System.out.println("Yêu cầu sai định dạng.");
                    case 21, 22 -> System.out.println("Số tiền giao dịch không hợp lệ.");
                    case 40 -> System.out.println("RequestId bị trùng. Vui lòng thử lại với RequestId khác.");
                    case 41 -> System.out.println("OrderId bị trùng.");
                    case 42 -> System.out.println("OrderId không hợp lệ hoặc không được tìm thấy.");
                    case 43 -> System.out.println("Xung đột trong quá trình xử lý giao dịch.");
                    case 1000 -> System.out.println("Giao dịch đã được khởi tạo, chờ người dùng xác nhận thanh toán.");
                    case 1001 -> System.out.println("Thanh toán thất bại: Tài khoản người dùng không đủ tiền.");
                    case 1002 -> System.out.println("Thanh toán bị từ chối do nhà phát hành tài khoản.");
                    case 1003 -> System.out.println("Giao dịch bị hủy.");
                    case 1004 -> System.out.println("Số tiền vượt quá hạn mức thanh toán.");
                    case 1005 -> System.out.println("URL hoặc QR code đã hết hạn.");
                    case 1006 -> System.out.println("Người dùng từ chối thanh toán.");
                    case 1007 -> System.out.println("Tài khoản không tồn tại hoặc đang bị ngưng hoạt động.");
                    case 7000, 7002 -> System.out.println("Giao dịch đang được xử lý. Vui lòng chờ.");
                    case 9000 -> System.out.println("Giao dịch đã được xác nhận thành công.");
                    default -> System.out.println("Lỗi không xác định. Vui lòng kiểm tra lại.");
                }
            } catch (ExecutionException e) {
                System.err.println("Lỗi khi xử lý giao dịch: " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
