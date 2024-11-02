CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,
    role ENUM('BORROWER', 'LIBRARIAN', 'ADMIN') NOT NULL
);

CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    published_date DATE
);

CREATE TABLE borrowRecord (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrow_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
);