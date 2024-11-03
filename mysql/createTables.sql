CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role ENUM('BORROWER', 'LIBRARIAN', 'ADMIN') NOT NULL
);

-- 图书表
CREATE TABLE book (
    isbn VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_date DATE,
    clc_number VARCHAR(20) NOT NULL,
    location VARCHAR(255)
);

-- 图书副本表
CREATE TABLE book_copy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(13) NOT NULL,
    status ENUM('AVAILABLE', 'RESERVED', 'BORROWED', 'LOST', 'DAMAGED') NOT NULL DEFAULT 'AVAILABLE',
    acquisition_date DATE NOT NULL,
    FOREIGN KEY (isbn) REFERENCES book(isbn)
);

-- 借阅记录表
CREATE TABLE book_borrow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_copy_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE', 'RESERVED') NOT NULL DEFAULT 'BORROWED',
    borrow_date DATETIME,
    due_date DATETIME,
    return_date DATETIME,
    expected_borrow_date DATE,
    FOREIGN KEY (book_copy_id) REFERENCES book_copy(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 图书信息视图
DROP VIEW IF EXISTS book_info;
CREATE VIEW book_info AS
SELECT 
    b.isbn,
    b.title,
    b.author,
    b.clc_number,
    b.location,
    COUNT(bc.id) as total_quantity,
    SUM(CASE WHEN bc.status = 'AVAILABLE' THEN 1 ELSE 0 END) as available_quantity
FROM book b
LEFT JOIN book_copy bc ON b.isbn = bc.isbn
GROUP BY b.isbn;
