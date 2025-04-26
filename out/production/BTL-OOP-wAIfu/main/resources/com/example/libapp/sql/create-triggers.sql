CREATE TRIGGER IF NOT EXISTS before_book_insert
            BEFORE INSERT ON Book
            FOR EACH ROW
BEGIN
SELECT RAISE(ABORT, 'Total copies and available copies must be non-negative')
    WHERE NEW.total_copies < 0 OR NEW.available_copies < 0;

SELECT RAISE(ABORT, 'Available copies cannot exceed total copies')
    WHERE NEW.available_copies > NEW.total_copies;
END;

CREATE TRIGGER IF NOT EXISTS before_book_update
            BEFORE UPDATE ON Book
                                         FOR EACH ROW
BEGIN
SELECT RAISE(ABORT, 'Total copies and available copies must be non-negative')
    WHERE NEW.total_copies < 0 OR NEW.available_copies < 0;

SELECT RAISE(ABORT, 'Available copies cannot exceed total copies')
    WHERE NEW.available_copies > NEW.total_copies;
END;

CREATE TRIGGER IF NOT EXISTS after_borrow_insert
            AFTER INSERT ON BorrowingRecord
            FOR EACH ROW
BEGIN
SELECT RAISE(ABORT, 'No copies available to borrow')
    WHERE (SELECT available_copies FROM Book WHERE id = NEW.book_id) < 0;

UPDATE Book
SET available_copies = available_copies - 1
WHERE id = NEW.book_id;
END;

CREATE TRIGGER IF NOT EXISTS after_borrow_update
            AFTER UPDATE ON BorrowingRecord
                                        FOR EACH ROW
                                        WHEN NEW.return_date IS NOT NULL AND OLD.return_date IS NULL
BEGIN
UPDATE Book
SET available_copies = available_copies + 1
WHERE id = NEW.book_id;
END;