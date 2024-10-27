# INNER JOIN
SELECT * FROM table1 INNER JOIN table2 ON table1.id = table2.id;

# LEFT JOIN
SELECT * FROM table1 LEFT JOIN table2 ON table1.id = table2.id;

#RIGHT JOIN
SELECT * FROM table1 RIGHT JOIN table2 ON table1.id = table2.id;

#FULL OUTER JOIN
SELECT * FROM table1 FULL OUTER JOIN table2 ON table1.id = table2.id;

#Subquery: truy vấn con
SELECT * FROM employees WHERE salary > (SELECT AVG(salary) FROM employees);

#CTE (CREATE TEMPORARY TABLE)
WITH EmployeeCTE AS (
  SELECT id, name, salary FROM employees WHERE department = 'HR'
)
SELECT * FROM EmployeeCTE WHERE salary > 5000;

#RANKING FUNCTION
	#ROW_NUMBER
SELECT name, salary, ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num FROM employees;

	#RANK
SELECT name, salary, RANK() OVER (ORDER BY salary DESC) AS rank FROM employees;

	#DENSE_RANK
SELECT name, salary, DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rank FROM employees;

#VIEW
CREATE VIEW HighSalary AS
SELECT name, salary FROM employees WHERE salary > 5000;

#PROCEDURE (SỬ DỤNG TRONG SQL)
CREATE PROCEDURE procedure_name (IN parameter1 datatype, OUT parameter2 datatype)
BEGIN
    -- SQL statements
END;


#PROCEDURE (SỬ DỤNG TRONG JAVA)
CallableStatement stmt = connection.prepareCall("{call my_procedure(?)}");
stmt.setInt(1, paramValue);
stmt.execute();




