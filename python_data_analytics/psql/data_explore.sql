-- Show table schema
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
 SELECT count(*) FROM retail;

-- number of clients (e.g. unique client ID)
SELECT count(DISTINCT customer_id) FROM retail;

--invoice date range (e.g. max/min dates)
SELECT MAX(invoice_date), MIN(invoice_date) FROM retail;

--number of SKU/merchants (e.g. unique stock code)
 SELECT count(DISTINCT stock_code) FROM retail;
 
 --Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
 SELECT avg(sum)FROM(SELECT sum(quantity * unit_price) FROM retail WHERE quantity * unit_price>0 GROUP BY invoice_no) as invoice_amt;
 
 --Calculate total revenue
 SELECT sum(sum)FROM(SELECT sum(quantity * unit_price) FROM retail GROUP BY invoice_no) as revenue;
 
 --Calculate total revenue by YYYYMM
 SELECT sum(quantity * unit_price) FROM retail GROUP BY DATE_FORMAT(invoice_date, "%y-%m")
 
 --Calculate total revenue by YYYYMM
 SELECT TO_CHAR(invoice_date, 'yyyymm') as yyyymm, sum(quantity * unit_price) FROM retail GROUP BY TO_CHAR(invoice_date, 'yyyymm') ORDER BY yyyymm;
