# 📄 Mô Tả Dự Án ACTEST Cho CV / Resume

> **Gợi ý sử dụng**: Tài liệu này cung cấp các phiên bản mô tả dự án **ACTEST** được chuẩn hóa theo tiêu chuẩn tuyển dụng IT (chuẩn định dạng STAR / Action-oriented), bao gồm cả **Tiếng Việt** và **Tiếng Anh**, phù hợp cho CV 1 trang, CV 2 trang hoặc hồ sơ LinkedIn / GitHub Portfolio.

---

## 🇻🇳 PHIÊN BẢN TIẾNG VIỆT

### 1. Bản Ngắn Gọn (Dành cho CV 1 trang / Tiết kiệm không gian)

**ACTEST – Hệ Thống Thi Trực Tuyến & Giám Sát Phòng Máy Đa Nền Tảng** | *Core Developer*  
*Công nghệ: Java 17/21, JavaFX, Maven, Sockets (TCP/UDP), SQLite, PostgreSQL, Jackson, Apache POI, JUnit 5, GitHub Actions*
- Xây dựng hệ thống quản lý thi và giám sát chống gian lận đa module, hỗ trợ đồng thời chế độ **Offline LAN (Phòng máy)** và **Online Central Server**.
- Thiết kế giao thức mạng thời gian thực: Tự động tìm kiếm phòng thi qua **UDP Broadcast (Zero-config Discovery)**, đồng bộ thời gian làm bài đếm ngược và xử lý tin nhắn qua **TCP Socket**.
- Phát triển module chống gian lận (Anti-Cheat): Tự động phát hiện thí sinh mất focus/rời màn hình làm bài, chụp ảnh màn hình bằng chứng và gửi về máy giám thị tức thời.
- Tối ưu đóng gói Fat JAR đa nền tảng chứa native libraries cho cả **Linux** (`.so`) và **Windows** (`.dll`), thiết lập CI/CD ma trận kiểm thử và phát hành tự động qua **GitHub Actions**.

---

### 2. Bản Chi Tiết (Dành cho CV 2 trang / Nhấn mạnh kỹ năng Backend & System)

**Dự Án: ACTEST – Automated Computerized Testing & LAN Proctoring System**  
- **Vị trí**: Core Software Engineer / Developer  
- **Quy mô dự án**: Hệ thống Client - Admin - Central Server đa module (Maven Multi-module)  
- **Công nghệ sử dụng**:
  - *Ngôn ngữ & Framework*: Java 17/21 LTS, JavaFX 21, FXML
  - *Mạng & Giao thức*: TCP Socket, UDP Broadcast, HTTP RESTful API (HttpServer)
  - *Cơ sở dữ liệu*: SQLite (phòng máy nội bộ), PostgreSQL (máy chủ trung tâm)
  - *Thư viện*: Jackson Databind, Apache POI (Excel), Webcam-Capture & BridJ, Jakarta Mail
  - *DevOps & Testing*: JUnit 5, GitHub Actions (CI/CD Matrix cho Ubuntu & Windows), Maven Shade Plugin
- **Trách nhiệm & Thành tựu chính**:
  - **Kiến trúc mạng phân tán**: Thiết kế cơ chế tự động tìm kiếm phòng thi không cần cấu hình IP (Zero-config UDP Discovery), xử lý kết nối song song hàng chục thí sinh ổn định với độ trễ thấp.
  - **Giám sát thời gian thực & Chống gian lận**: Xây dựng cơ chế Fullscreen Lock và Focus Tracking để phát hiện hành vi gian lận; tự động chụp ảnh màn hình vi phạm mã hóa Base64 gửi về giám sát viên.
  - **Khả năng chịu lỗi & Khôi phục phiên thi**: Hiện thực hóa tính năng tạm dừng (Pause) và khôi phục bài thi (Resume Exam) theo IP của thí sinh khi gặp sự cố ngắt kết nối mạng hoặc tắt nguồn máy tính.
  - **Đóng gói & Phân phối liên nền tảng**: Cấu hình dual-native classifier cho OpenJFX, tạo file Fat JAR thực thi độc lập chạy mượt mà trên cả Linux và Windows mà không cần cài đặt thêm JavaFX runtime.
  - **Tự động hóa CI/CD**: Xây dựng pipeline kiểm thử tự động trên ma trận OS (Ubuntu/Windows) kết hợp Java 17/21 và tự động đóng gói xuất bản GitHub Release đính kèm file chạy.

---

## 🇬🇧 PHIÊN BẢN TIẾNG ANH (INTERNATIONAL FORMAT)

### 1. Concise Version (For 1-page Resume)

**ACTEST – Cross-Platform Computerized Exam & Proctoring System** | *Core Developer*  
*Technologies: Java 17/21, JavaFX, Maven, Sockets (TCP/UDP), SQLite, PostgreSQL, Jackson, Apache POI, JUnit 5, GitHub Actions*
- Architected a multi-module exam administration and real-time proctoring desktop application supporting both **LAN Offline Computer Labs** and **Centralized Online Server** architectures.
- Implemented network protocols: **Zero-configuration UDP Broadcast** for local server discovery, countdown timer synchronization, and full-duplex **TCP Socket** communication for answers and commands.
- Built an automated anti-cheat engine detecting window focus loss, capturing timestamped screenshot evidence, and streaming violation alerts to proctors in real-time.
- Configured universal cross-platform fat JAR packaging with bundled **Linux (.so)** and **Windows (.dll)** native OpenJFX binaries; established multi-OS matrix CI/CD with **GitHub Actions**.

---

### 2. Action-Oriented Detailed Version (Bullet Points for TopCV / LinkedIn / Portfolio)

**ACTEST – Automated Computerized Exam Management & LAN Proctoring Platform**  
*Role: Software Engineer / Core Developer*

- **Distributed Networking**: Designed custom networking layers using **UDP Broadcast** for automatic local proctor discovery and **TCP Sockets** for robust, real-time message exchange between proctor dashboard and student test-taking clients.
- **Anti-Cheat & Proctoring Engine**: Engineered fullscreen containment and focus-tracking listeners that flag off-screen navigation, capture proof screenshots, and serialize them into Base64 for instant remote audit.
- **Session Resilience & State Recovery**: Designed an IP-based session persistence mechanism allowing interrupted or disconnected exam sessions to seamlessly restore questions, answers, and elapsed time.
- **Cross-Platform Engineering**: Resolved cross-platform encoding disparities with strict UTF-8 handling and bundled dual-OS JavaFX native binaries into self-contained executable JARs with one-click launch scripts (`.sh` & `.bat`).
- **Data Persistence & Reporting**: Integrated **SQLite** for zero-dependency local classroom testing and **PostgreSQL** for central server synchronization; automated batch question import/export using **Apache POI**.
- **Quality Assurance & DevOps**: Authored automated test suites with **JUnit 5** and established a complete **GitHub Actions CI/CD** workflow running multi-OS matrix builds and publishing automated release packages.

---

## 💡 Gợi Ý Từ Khóa (Keywords) Bổ Trợ Thu Hút Nhà Tuyển Dụng / ATS Scanner
`Java Core`, `JavaFX`, `Multithreading`, `Socket Programming (TCP/UDP)`, `Distributed Systems`, `Network Protocols`, `PostgreSQL`, `SQLite`, `Jackson JSON`, `Apache POI`, `Clean Architecture`, `Cross-Platform Deployment`, `CI/CD Pipeline`, `GitHub Actions`, `Unit Testing (JUnit 5)`, `Anti-Cheat Mechanism`.
