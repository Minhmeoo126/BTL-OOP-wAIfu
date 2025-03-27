USE lib;

DELIMITER //

-- Kiểm tra tổng số sách hợp lệ khi INSERT
CREATE TRIGGER check_total_copies_insert
    BEFORE INSERT ON Book
    FOR EACH ROW
BEGIN
    IF NEW.total_copies < NEW.available_copies THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error: total_copies must be greater than or equal to available_copies';
    END IF;
END //

-- Kiểm tra tổng số sách hợp lệ khi UPDATE
CREATE TRIGGER check_total_copies_update
    BEFORE UPDATE ON Book
    FOR EACH ROW
BEGIN
    IF NEW.total_copies < NEW.available_copies THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error: total_copies must be greater than or equal to available_copies';
    END IF;
END //

-- Giảm available_copies khi có sách được mượn (Chặn mượn nếu available_copies = 0)
CREATE TRIGGER after_borrow_insert
    BEFORE INSERT ON BorrowingRecord
    FOR EACH ROW
BEGIN
    DECLARE available INT;

    -- Lấy số sách còn lại
    SELECT available_copies INTO available FROM Book WHERE id = NEW.book_id;

    -- Nếu không còn sách để mượn, báo lỗi
    IF available <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error: No available copies to borrow';
    END IF;

    -- Giảm available_copies
    UPDATE Book
    SET available_copies = available_copies - 1
    WHERE id = NEW.book_id;
END //

-- Tăng available_copies khi sách được trả và cập nhật return_date
CREATE TRIGGER after_borrow_update
    AFTER UPDATE ON BorrowingRecord
    FOR EACH ROW
BEGIN
    IF NEW.status = 'returned' AND OLD.status != 'returned' THEN
        UPDATE Book
        SET available_copies = available_copies + 1
        WHERE id = NEW.book_id;

        -- Cập nhật return_date nếu chưa có
        IF NEW.return_date IS NULL THEN
            UPDATE BorrowingRecord
            SET return_date = CURRENT_DATE
            WHERE id = NEW.id;
        END IF;
    END IF;
END //

DELIMITER ;
