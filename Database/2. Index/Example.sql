# Sử dụng Expain để kiểm tra kế hoạch thực thi,
# nó cho biết chỉ mục có được dùng hay ko
EXPLAIN SELECT * FROM employees WHERE id = 123;

# Trong MySQL có thể sử dụng "Actual Execution Plan" hoặc "Estimated Execution Plan"
# Kiểm tra các chỉ mục hiện có và kết hợp với 
# lệnh EXPLAIN để xem chỉ mục nào đang được sử dụng trong query.
SHOW INDEXES FROM table_name;

