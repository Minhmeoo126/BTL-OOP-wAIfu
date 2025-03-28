-- Check total valid book when INSERT
CREATE TRIGGER before_book_insert
    BEFORE INSERT ON Book
    FOR EACH ROW
BEGIN
    IF NEW.total_copies < 0 OR NEW.available_copies < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Total copies and available copies must be non-negative';
    END IF;
    IF NEW.available_copies > NEW.total_copies THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Available copies cannot exceed total copies';
    END IF;
END;

-- Check total books when UPDATE
CREATE TRIGGER before_book_update
    BEFORE UPDATE ON Book
    FOR EACH ROW
BEGIN
    IF NEW.total_copies < 0 OR NEW.available_copies < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Total copies and available copies must be non-negative';
    END IF;
    IF NEW.available_copies > NEW.total_copies THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Available copies cannot exceed total copies';
    END IF;
END;

-- Update available_copies when borrow
CREATE TRIGGER after_borrow_insert
    AFTER INSERT ON BorrowingRecord
    FOR EACH ROW
BEGIN
    UPDATE Book
    SET available_copies = available_copies - 1
    WHERE id = NEW.book_id;
END;

-- Update available_copies when return
CREATE TRIGGER after_borrow_update
    AFTER UPDATE ON BorrowingRecord
    FOR EACH ROW
BEGIN
    IF NEW.return_date IS NOT NULL AND OLD.return_date IS NULL THEN
        UPDATE Book
        SET available_copies = available_copies + 1
        WHERE id = NEW.book_id;
    END IF;
END;