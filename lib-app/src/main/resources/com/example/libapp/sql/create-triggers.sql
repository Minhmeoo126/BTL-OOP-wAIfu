-- Check total valid book when INSERT
CREATE TRIGGER before_book_insert
    BEFORE INSERT ON Book
    FOR EACH ROW
BEGIN
    SELECT CASE
               WHEN NEW.total_copies < 0 OR NEW.available_copies < 0 THEN
                   RAISE(ABORT, 'Total copies and available copies must be non-negative')
               WHEN NEW.available_copies > NEW.total_copies THEN
                   RAISE(ABORT, 'Available copies cannot exceed total copies')
               END;
END;

-- Check total books when UPDATE
CREATE TRIGGER before_book_update
    BEFORE UPDATE ON Book
    FOR EACH ROW
BEGIN
    SELECT CASE
               WHEN NEW.total_copies < 0 OR NEW.available_copies < 0 THEN
                   RAISE(ABORT, 'Total copies and available copies must be non-negative')
               WHEN NEW.available_copies > NEW.total_copies THEN
                   RAISE(ABORT, 'Available copies cannot exceed total copies')
               END;
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
    WHEN NEW.return_date IS NOT NULL AND OLD.return_date IS NULL
BEGIN
    UPDATE Book
    SET available_copies = available_copies + 1
    WHERE id = NEW.book_id;
END;
