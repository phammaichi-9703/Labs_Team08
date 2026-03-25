[lab_team08 app
****](https://docs.google.com/document/copy?copyDestination=1HslGLzuPiBHam8c95xHXlKXdD4O0h_WM&copySourceId=13m6hcVpFE_I06mqImHJcWubKhrOyzgcm&copiedFromTrash&redirect=true&usp=docs_web)
Đề bài: Ứng dụng quản lý bán hàng (Shopping App)
Xây dựng ứng dụng Android cho phép người dùng:
Đăng nhập
Xem danh sách sản phẩm
Xem danh sách danh mục sản phẩm
Xem chi tiết sản phẩm
Tạo hóa đơn (phải đăng nhập)
Dữ liệu được lưu bằng Room Database, trạng thái đăng nhập lưu bằng SharedPreferences.
Có 5 bảng: Users, Categories, Products, OrderDetails, Orders
Trong đó các mối quan hệ có thể như sau:
Có thể thêm cứng 1 số dữ liệu cho các bảng.
Luồng dữ liệu trong hệ thống:
Start App
   │
   ▼
Home Screen
   │
   ├── Login
   │
   ├── Xem danh sách Products
   │
   ├── Xem Categories
   │
   └── Xem chi tiết Product
            │
            ▼
        Chọn sản phẩm (Nhặt hàng)
            │
            ▼
    Kiểm tra đăng nhập?
        │
   ┌────┴────┐
   │         │
  No        Yes
   │         │
   ▼         ▼
Login     Tạo Order (nếu chưa có)
   │         │
   ▼         ▼
Login thành công
   │
   ▼
Tạo Order (nếu chưa có)
   │
   ▼
Tạo OrderDetails (thêm sản phẩm đã chọn)
   │
   ▼
Có tiếp tục chọn sản phẩm?
   │
   ├── Yes → quay lại danh sách Products
   │
   └── No
          │
          ▼
      Thanh toán (Checkout)
          │
          ▼
   Cập nhật trạng thái Order (Paid)
          │
          ▼
Hiển thị hóa đơn

