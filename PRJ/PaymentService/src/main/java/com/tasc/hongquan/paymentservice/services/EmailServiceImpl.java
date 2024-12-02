package com.tasc.hongquan.paymentservice.services;

import com.tasc.hongquan.paymentservice.models.Order;
import com.tasc.hongquan.paymentservice.models.OrderDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public String generateEmailContent(Order order, String paymentMethod) {
        BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
        BigDecimal discount = order.getDiscount() != null ? order.getDiscount().getAmount() : BigDecimal.ZERO;
        BigDecimal discountAmount = totalAmount.multiply(discount).divide(BigDecimal.valueOf(100));

        order.getOrderDetails().forEach((item) -> {
            totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        });
        BigDecimal totalAmountAfter = totalAmount.subtract(discountAmount);
        Context context = new Context();
        context.setVariable("customer_name", order.getUser().getFullName());
        context.setVariable("order_details", generateOrderDetails(order.getOrderDetails()));
        context.setVariable("customer_email", order.getUser().getEmail());
        context.setVariable("shipping_address", order.getAddressBook().getAddress());
        context.setVariable("payment_method", paymentMethod);
        context.setVariable("total_amount", totalAmountAfter);
        context.setVariable("order_id", order.getId());
        context.setVariable("discount", discountAmount);
        context.setVariable("date_order", formatDate(order.getCreatedAt()));
        return templateEngine.process("order-confirmation", context);
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        System.out.println("Email sent to: " + to);
    }

    public void sendOrderConfirmation(Order order, String paymentMethod) throws MessagingException {
        String subject = "Xác nhận đơn hàng #" + order.getId();

        // Gọi hàm generateEmailContent để tạo nội dung email
        String emailContent = generateEmailContent(order, paymentMethod);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(order.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(emailContent, true);
        helper.setFrom("sudesteam@gmail.com");

        mailSender.send(mimeMessage);
    }

    public void sendRetryEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setFrom("sudesteam@gmail.com");

        mailSender.send(mimeMessage);
    }

    // Hàm hỗ trợ tạo chuỗi HTML chi tiết sản phẩm
    private String generateOrderDetails(List<OrderDetail> orderDetails) {
        StringBuilder detailsBuilder = new StringBuilder();
        int i = 1;
        for (OrderDetail detail : orderDetails) {
            detailsBuilder.append("<tr>")
                    .append("<td>").append(i).append("</td>")
                    .append("<td>")
                    .append("<div class=\"product-container\">")
                    .append("<img src=\"").append(detail.getProduct().getImageUrl()).append("\" alt=\"product-image\" width=\"60px\">")
                    .append("<div> <p>").append(detail.getProduct().getName()).append("</p> </div>")
                    .append("</div>")
                    .append("</td>")
                    .append("<td>").append(detail.getQuantity()).append("</td>")
                    .append("<td>").append(detail.getPrice()).append(" VND</td>")
                    .append("<td>").append(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()))).append(" VND</td>")
                    .append("</tr>");
            i++;
        }
        return detailsBuilder.toString();
    }

    //value return same as: 20/07/2024
    private String formatDate(Instant date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDateTime.format(formatter);
    }
}
