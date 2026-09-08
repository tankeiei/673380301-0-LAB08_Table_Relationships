#  Lab 8: Table Relationships — Product Shop

เว็บแอปพลิเคชันจัดการข้อมูลสินค้า (CRUD) พร้อมการจัดการความสัมพันธ์ของฐานข้อมูลแบบ **One-to-One (1:1)** และ **One-to-Many (1:N)** พัฒนาด้วย **Spring Boot + Spring Data JPA + PostgreSQL + Thymeleaf** ตามหลักการออกแบบซอฟต์แวร์ (SOLID Principles) และ **Strategy Pattern**

---

##  ข้อมูลผู้จัดทำ

* **ชื่อ - นามสกุล:** นายแทนคุณ พันธ์นิกุล
* **รหัสนักศึกษา:** 673380301-0
* **กลุ่มเรียน:** SEC 1
* **วิชา:** CP353002 Principles of Software Design  
* **สาขาวิชา:** วิทยาการคอมพิวเตอร์และสารสนเทศ มหาวิทยาลัยขอนแก่น

---

##  สถาปัตยกรรมและหลักการออกแบบ

### 1. ความสัมพันธ์ของตาราง (Table Relationships)
* **One-to-One (1:1):** `Product` ⟷ `ProductDetail`
  * ฝั่ง Owner คือ `Product` เก็บ Foreign Key `detail_id` (`@OneToOne(cascade = ALL)`)
  * ฝั่ง Inverse คือ `ProductDetail` (`@OneToOne(mappedBy = "detail")`)
  * ใช้สำหรับแยกข้อมูลสเปกสินค้าและรายละเอียดการรับประกันตามหลัก **SRP**
* **One-to-Many (1:N):** `Product` ⟷ `Review`
  * ฝั่ง Many คือ `Review` เก็บ Foreign Key `product_id` (`@ManyToOne`)
  * ฝั่ง One คือ `Product` (`@OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true)`)
  * รองรับการเพิ่มรีวิวสินค้าได้ไม่จำกัดตามหลัก **OCP**

### 2. Strategy Pattern (การคำนวณส่วนลด)
แยกอัลกอริทึมการคำนวณส่วนลดออกจาก Business Logic ผ่าน `DiscountStrategy`:
* `NoDiscountStrategy`: ไม่มีส่วนลด (ลด 0%)
* `MemberDiscountStrategy`: ส่วนลดสมาชิก (ลด 10%)
* `SeasonalSaleStrategy`: ส่วนลดเทศกาล (ลด 20%)
* `DiscountContext`: ทำหน้าที่เลือก Strategy ตามประเภทส่วนลด (`discountType`)

### 3. SOLID Principles
* **SRP:** แยก Model, Service (Logic), และ Controller (HTTP) ออกจากกันชัดเจน
* **OCP:** เพิ่ม Review หรือโปรโมชั่นส่วนลดใหม่ได้โดยไม่ต้องแก้ไขโค้ดเดิม
* **LSP:** Concrete Strategies ทุกตัวสามารถใช้แทน Interface `DiscountStrategy` ได้อย่างถูกต้อง
* **ISP:** แยก Repository เฉพาะของแต่ละ Entity (`ProductRepository`, `ProductDetailRepository`, `ReviewRepository`)
* **DIP:** Service ขึ้นตรงกับ Interface และใช้ **Constructor Injection** ในการฉีด Dependencies

---

##  โครงสร้างโปรเจกต์ (Project Structure)

```text
src/main/
├── java/com/example/demo/
│   ├── DemoApplication.java
│   ├── model/          # Product, ProductDetail (1:1), Review (1:N)
│   ├── repository/     # ProductRepository, ProductDetailRepository, ReviewRepository
│   ├── strategy/       # DiscountStrategy, Context, และ Concrete Strategies
│   ├── service/        # ProductService (Business Logic & Relations)
│   └── controller/     # ProductController (HTTP Endpoints)
└── resources/
    ├── application.properties
    ├── static/css/     # style.css
    └── templates/      # Thymeleaf (list, add, edit, delete)
