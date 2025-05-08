# 📚 Library Management System wAIfu

> Một hệ thống quản lý thư viện hiện đại, thân thiện, có tích hợp AI để hỗ trợ người dùng và thủ thư thông minh!

---

## 👨‍💻 Nhóm tác giả:

| Vai trò       | Họ và Tên                      | Mã sinh viên |
|---------------|-------------------------------|--------------|
| Trưởng nhóm   | Dương Quang Minh               | 24022401     |
| Thành viên    | Lê Thanh Lâm                   | 24022377     |
| Thành viên    | Nguyễn Tân Hoàng Minh          | 24022407     |

---

## 🌟 Tính năng chính

### 👥 Đăng nhập đa người dùng: **Admin / User**

- 🔍 Tìm kiếm sách theo từ khóa.
- 📘 Xem chi tiết sách: tiêu đề, tác giả, mô tả, ảnh bìa.
- 📥 Mượn sách qua ID, tên, hoặc nút "Borrow".
- 📤 Trả sách tương tự.
- 🤖 AI: Tâm sự và bàn luận về sách cùng thủ thư AI.

### 🔧 Tính năng cho Admin:

- 📚 Thêm sách (hỗ trợ scan mã vạch).
- 🗑️ Xoá sách.
- 👤 Quản lý người dùng.
- 🛠️ Quản lý toàn bộ kho sách.

### 🖥️ Giao diện người dùng:

- Xây dựng bằng **JavaFX + FXML**.
- Giao diện hiện đại, dễ sử dụng.

---

## 💻 Công nghệ sử dụng

- `Java`: Xử lý logic và backend.
- `JavaFX`: Giao diện người dùng.
- `CSS`: Tùy chỉnh UI.
- `Google Books API`: Lấy dữ liệu sách.
- `OpenCV + ZXing`: Scan mã vạch lấy ISBN.
- `OpenAI API (GPT-4o)`: Tạo trợ lý ảo thông minh.
- `FXML`: Tách biệt giao diện khỏi logic Java.

---

## 🧠 Thiết kế hệ thống & CSDL

### 📌 Thiết kế hệ thống
![Thiết kế hệ thống](lib-app/src/main/resources/com/example/libapp/README_IMG/ULM.png)

### 🗃️ Cơ sở dữ liệu
![Cơ sở dữ liệu](lib-app/src/main/resources/com/example/libapp/README_IMG/DataBase.png)

---

## 🔐 Đăng nhập & Đăng ký

| Đăng nhập | Đăng ký |
|----------|---------|
| ![Login](lib-app/src/main/resources/com/example/libapp/README_IMG/Login.png) | ![Register](lib-app/src/main/resources/com/example/libapp/README_IMG/register.png) |

---

## 🧾 DashBoard

- Hiển thị list sách mới, kệ sách chứa toàn bộ sách thư viện.
- Thanh tìm kiếm của thư viện.
- Hiển thị kết quả theo tên sách, ID, tác giả.

| DashBoard | Thanh tìm kiếm | Kết quả tìm kiếm |
|-----------|----------------|------------------|
| ![Dashboard](lib-app/src/main/resources/com/example/libapp/README_IMG/dashboard.png) | ![SearchBar](lib-app/src/main/resources/com/example/libapp/README_IMG/bar.png) | ![Search Result](lib-app/src/main/resources/com/example/libapp/README_IMG/search_Result.png) |

---

## 📚 Quản lý Sách

| Quản lý sách | Sửa sách | Thêm sách | Xem sách |
|--------------|----------|-----------|----------|
| ![Book MANA](lib-app/src/main/resources/com/example/libapp/README_IMG/bookMANA.png) | ![Change Book](lib-app/src/main/resources/com/example/libapp/README_IMG/changebook.png) | ![Add Book](lib-app/src/main/resources/com/example/libapp/README_IMG/addbook.png) | ![Book View](lib-app/src/main/resources/com/example/libapp/README_IMG/book_view.png) |

---

## 👥 Quản lý Thành viên

-Account cho phép trả sách nhanh qua nút return.

| Mượn sách | Trả sách | Quản lí Thành viên | Sửa thông tin  | Account của user |
|-----------|----------|--------------------|----------------|------------------|
| ![Borrow](lib-app/src/main/resources/com/example/libapp/README_IMG/borrowBook.png) | ![Return](lib-app/src/main/resources/com/example/libapp/README_IMG/returnbook.png) | ![User List](lib-app/src/main/resources/com/example/libapp/README_IMG/USERMANA.png) | ![Change User](lib-app/src/main/resources/com/example/libapp/README_IMG/changeuser.png) |![Change User](lib-app/src/main/resources/com/example/libapp/README_IMG/account.png)

---

## 🤖 Thủ thư AI

> Một thủ thư AI dễ thương sẵn sàng tâm sự với bạn. Nhưng đừng tỏ tình, vì em ấy sẽ từ chối 😢

![AI Assistant](lib-app/src/main/resources/com/example/libapp/README_IMG/AI.png)

---

## 🛠️ Cài đặt

### ✅ Yêu cầu:

- Java 8+
- JavaFX 8+ (nếu chưa tích hợp sẵn trong JDK)

### 🔧 Cách cài đặt:

```bash
# Clone dự án
git clone https://github.com/username/BTL-OOP-wAIfu.git

# Di chuyển vào thư mục
cd BTL-OOP-wAIfu

# Cài đặt thư viện & cấu hình VM options nếu dùng tính năng scan
# Tạo config với VM option sau:
# -Djava.library.path=D:/BTL-OOP-wAIfu/lib-app/native
