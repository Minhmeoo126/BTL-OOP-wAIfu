
# Library Management System wAIfu

## 📖 Mô tả
Dự án **Library Management System wAIfu** là một hệ thống quản lý thư viện, giúp quản lý sách, người dùng, và các giao dịch mượn trả sách, hỗ trợ AI tâm sự, phân tích văn.

## 📝 Nhóm tác giả:
1. **Trưởng nhóm**: Dương Quang Minh - 24022401
2. **Thành viên**: Lê Thanh Lâm - 24022377
3. **Thành viên**: Nguyễn Tân Hoàng Minh - 24022407

## Tính năng

### Login mutil-user: Admin | User
- **Tìm kiếm sách**: Tìm kiếm sách theo từ khóa.
- **Chi tiết sách**: Xem thông tin chi tiết về sách đã chọn, bao gồm:
    - Tiêu đề.
    - Tác giả(s).
    - Mô tả.
    - Bìa sách.
- **Mượn sách**: Mượn sách theo id hoặc name hoặc mượn nhanh bằng button.
- **Trả sách**: Trả sách theo id hoặc name hoặc trả nhanh bằng button.
- **AI**: Tâm sự và bàn luận về các cuốn sách.

### **Quản lý thư viện <Admin>**
- Thêm sách vào thư viện bằng scan.
- Xóa sách khỏi thư viện.
- Quản lí người dùng.
- Quản lí sách.

### **Giao diện người dùng tương tác**:
- Xây dựng bằng JavaFX và FXML cho giao diện người dùng dễ sử dụng.

## Công nghệ sử dụng

- **Java**: Logic cốt lõi và tích hợp API.
- **JavaFX**: Phát triển giao diện người dùng và xử lý sự kiện.
- **CSS**: Tùy chỉnh giao diện của các thành phần JavaFX.
- **Google Books API**: Dùng để lấy dữ liệu sách.
- **OPEN CV + Zxing**: Dùng Scan mã vạch lấy ISBN.
- **OPENAI API model gpt-4o**: Trợ lí ảo thủ thư viện.
- **FXML**: Tách biệt giao diện người dùng khỏi mã Java.

## Thiết kế hệ thống và Cơ sở dữ liệu của dự án

**Thiết kế hệ thống**  
![Mô tả hình ảnh](/com/example/libapp/README_IMG/ULM.png)

**Cơ sở dữ liệu**  
![Mô tả hình ảnh](com/example/libapp/README_IMG/DataBase.png)

## Giới thiệu về ứng dụng

### **ĐĂNG NHẬP**: Đăng nhập và đăng ký
![Mô tả hình ảnh](./imageforreadme/Signin.png)  
![Mô tả hình ảnh](./imageforreadme/Signup.png)

### **BẢNG TIN**: Tổng quan về ứng dụng
- Sách mượn nhiều nhất, thu nhập, số lần mượn  
  ![Mô tả hình ảnh](./imageforreadme/Dashboard.png)
- Cài đặt âm nhạc  
  ![Mô tả hình ảnh](./imageforreadme/MusicSetting.png)
- Thông báo  
  ![Mô tả hình ảnh](./imageforreadme/Notification.png)
- Chỉnh sửa thông tin Admin, thay đổi avatar  
  ![Mô tả hình ảnh](./imageforreadme/Edit_Admin.png)

### **SÁCH**: Thanh tìm kiếm, thêm sách mới vào thư viện, và xem trước sách
- Danh sách sách:  
  ![Mô tả hình ảnh](./imageforreadme/Book.png)
- Tìm kiếm sách:  
  ![Mô tả hình ảnh](./imageforreadme/Search_book.png)
- Xem trước sách, thêm sách vào thư viện, lấy mã QR sách:  
  ![Mô tả hình ảnh](./imageforreadme/View_book.png)

### **THÀNH VIÊN**: Xem thông tin thành viên, số lượng sách đã mượn, chỉnh sửa thành viên
- Danh sách thành viên, thêm và chỉnh sửa thành viên:  
  ![Mô tả hình ảnh](./imageforreadme/List_Member.png)
- Hiển thị thông tin thành viên khi nhấp vào dòng, tìm kiếm theo bộ lọc:  
  ![Mô tả hình ảnh](./imageforreadme/Information.png)
- Thêm thành viên mới và xóa thành viên đã thêm:  
  ![Mô tả hình ảnh](./imageforreadme/Add_Member.png)  
  ![Mô tả hình ảnh](./imageforreadme/Remove_Member.png)
- Chỉnh sửa thành viên đã thêm:  
  ![Mô tả hình ảnh](./imageforreadme/Edit_Member.png)

### **MƯỢN SÁCH**: Hiển thị thông tin sách đang mượn
- Danh sách sách mượn, thêm mượn mới, lọc:  
  ![Mô tả hình ảnh](./imageforreadme/List_Borrow.png)
- Thêm mượn mới và quy tắc mượn:  
  ![Mô tả hình ảnh](./imageforreadme/Add_Borrow.png)

### **THU NHẬP**: Hiển thị thông tin giao dịch
- Danh sách giao dịch, thêm giao dịch mới, lọc:  
  ![Mô tả hình ảnh](./imageforreadme/List_transaction.png)
- Thêm giao dịch mới:  
  ![Mô tả hình ảnh](./imageforreadme/add_transaction.png)

### **ĐĂNG XUẤT**: Quay lại trang đăng nhập
![Mô tả hình ảnh](./imageforreadme/Logout.png)

## Cài đặt

### Yêu cầu:
- Java 8+
- JavaFX 8+ (nếu không được tích hợp trong JDK của bạn)
- Google Books API key (tùy chọn, nhưng cần thiết cho chức năng tìm kiếm sách)

### Các bước cài đặt:

```bash
# Clone dự án
git clone https://github.com/username/library-management-system-html.git

# Di chuyển vào thư mục dự án
cd library-management-system-html

# Cài đặt các dependencies (nếu có)
# Đảm bảo rằng JavaFX được cấu hình đúng trong IDE của bạn

# Chạy dự án
# Trong IDE của bạn, chạy lớp `Main.java` để khởi động ứng dụng
