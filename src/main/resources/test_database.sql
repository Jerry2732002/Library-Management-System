-- H2 Database SQL dump

-- Table structure for table Books
DROP TABLE IF EXISTS Books;
CREATE TABLE Books (
  BookID INT AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(100) UNIQUE,
  Author VARCHAR(100),
  Category VARCHAR(100),
  Rare BOOLEAN,
  CopiesAvailable INT
);

-- Dumping data for table Books
INSERT INTO Books (BookID, Title, Author, Category, Rare, CopiesAvailable) VALUES
(1, 'The Great Adventure', 'John Doe', 'FICTION', TRUE, 6),
(2, 'Space Odyssey', 'Arthur Clarke', 'SCIENCE_FICTION', FALSE, 4),
(4, 'The Art of Cooking', 'Chef Marie', 'COOKING', FALSE, 1),
(5, 'Shadows of the Past', 'Jane Smith', 'MYSTERY', FALSE, 5),
(6, 'The Galactic Journey', 'John Adams', 'SCIENCE_FICTION', TRUE, 10);

-- Table structure for table Borrows
DROP TABLE IF EXISTS Borrows;
CREATE TABLE Borrows (
  BorrowID INT AUTO_INCREMENT PRIMARY KEY,
  UserID INT,
  BookID INT,
  BorrowDate DATE,
  ReturnDate DATE,
  Fine DECIMAL(6,2) DEFAULT 0.00,
  FOREIGN KEY (UserID) REFERENCES Users(UserID),
  FOREIGN KEY (BookID) REFERENCES Books(BookID)
);

-- Dumping data for table Borrows
INSERT INTO Borrows (BorrowID, UserID, BookID, BorrowDate, ReturnDate, Fine) VALUES
(13, 1, 1, '2025-01-21', '2025-01-21', 0.00),
(14, 1, 1, '2025-01-21', '2025-01-21', 0.00),
(15, 1, 1, '2025-01-21', '2025-01-21', 0.00),
(16, 1, 1, '2025-01-21', '2025-01-21', 0.00),
(17, 1, 1, '2025-01-21', '2025-04-21', 0.00),
(18, 1, 1, '2025-01-21', '2025-04-21', 0.00),
(19, 1, 1, '2025-01-21', '2025-04-21', 0.00),
(20, 1, 1, '2025-01-21', '2025-04-21', 83.00);

-- Table structure for table Sessions
DROP TABLE IF EXISTS Sessions;
CREATE TABLE Sessions (
  UserID INT PRIMARY KEY,
  SessionID VARCHAR(100) UNIQUE
);

-- Dumping data for table Sessions
INSERT INTO Sessions (UserID, SessionID) VALUES
(1, 'D0755F23E30D9825519386C739EC49CC');

-- Table structure for table Users
DROP TABLE IF EXISTS Users;
CREATE TABLE Users (
  UserID INT AUTO_INCREMENT PRIMARY KEY,
  Email VARCHAR(100) UNIQUE,
  Password VARCHAR(100),
  Firstname VARCHAR(30),
  Lastname VARCHAR(30),
  Membership VARCHAR(40),
  Role VARCHAR(10)
);

-- Dumping data for table Users
INSERT INTO Users (UserID, Email, Password, Firstname, Lastname, Membership, Role) VALUES
(1, 'jerry@gmail.com', '$2a$12$7sKoknBV0ypKJiHzTCEBQuSluXK36WdYKitv4bGYX/VL96Hs5c.9S', 'Jerry', 'Sebastian', 'GOLD', 'USER'),
(2, 'admin@gmail.com', '$2a$12$Jc/W2jitBXavPg9OCV9DOeKnLS6R5N9./8MP/CMEVl7P9amhjpGCe', NULL, NULL, NULL, 'ADMIN');
