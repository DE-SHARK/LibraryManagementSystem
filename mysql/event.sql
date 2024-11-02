CREATE EVENT update_overdue_status
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    UPDATE book_borrow
    SET status = 'OVERDUE'
    WHERE status = 'BORROWED' 
      AND borrow_date IS NOT NULL 
      AND return_date IS NULL 
      AND DATE_ADD(borrow_date, INTERVAL 14 DAY) < CURDATE();
END;