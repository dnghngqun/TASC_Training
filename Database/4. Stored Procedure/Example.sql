#Tạo Stored Procedure
CREATE PROCEDURE GetCustomerDetails(IN customer_id INT)
BEGIN
    SELECT * FROM customers WHERE id = customer_id;
END;

#Sử dụng trong java
#Bước 1: Tạo kết nối với cơ sở dữ liệu
Connection connection = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/mydatabase", "username", "password");

#Bước 2: Sử dụng CallableStatement để gọi stored procedure
   CallableStatement callableStatement = connection.prepareCall("{call GetCustomerDetails(?)}");

#Bước 3: Truyền tham số và thực thi
callableStatement.setInt(1, 123); // Truyền giá trị customer_id là 123
ResultSet resultSet = callableStatement.executeQuery(); // Thực thi stored procedure

#Bước 4: Lấy kết quả từ ResultSet
while (resultSet.next()) {
    System.out.println("Customer ID: " + resultSet.getInt("id"));
    System.out.println("Customer Name: " + resultSet.getString("name"));
}

#Bước 5: Đóng kết nối
resultSet.close();
callableStatement.close();
connection.close();

