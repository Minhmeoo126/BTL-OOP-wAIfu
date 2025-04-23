-- Insert data into Users
INSERT OR IGNORE INTO Users (username, password, role, email, full_name) VALUES
                ('admin', 'admin123', 'ADMIN', 'admin@example.com', 'Admin User'),
                ('john_doe', 'password123', 'USER', 'john.doe@example.com', 'John Doe'),
                ('jane_smith', 'password123', 'USER', 'jane.smith@example.com', 'Jane Smith');