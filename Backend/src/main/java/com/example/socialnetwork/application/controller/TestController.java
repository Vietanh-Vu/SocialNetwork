package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.common.constant.Gender;
import com.example.socialnetwork.infrastructure.elasticsearch.UserDocument;
import com.example.socialnetwork.infrastructure.repository.UserElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController extends BaseController {
  private final UserElasticsearchRepository userElasticsearchRepository;
  @GetMapping("/test")
  public String test() {
    // Xóa các dữ liệu cũ trước khi thêm mới
    userElasticsearchRepository.deleteAll();

    // Thêm 10 user để minh họa
    List<UserDocument> users = new ArrayList<>();

    // User 1
    users.add(UserDocument.builder()
        .id("abc")
        .username("Vũ Nguyễn Quỳnh Anh")
        .email("vuquynhanh@example.com")
        .password("password123")
        .firstName("Vũ")
        .lastName("Nguyễn Quỳnh Anh")
        .gender(Gender.FEMALE)
        .visibility("public")
        .role("user")
        .bio("Tôi là một lập trình viên và thích đọc sách")
        .location("Hà Nội, Việt Nam")
        .work("Senior Software Developer tại Tech Corp")
        .education("Đại học Bách Khoa Hà Nội")
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .avatar("avatar1.jpg")
        .backgroundImage("background1.jpg")
        .dateOfBirth(LocalDate.of(1995, 6, 15))
        .isEmailVerified(true)
        .build());

    // User 2
    users.add(UserDocument.builder()
        .id("acdf")
        .username("Nguyễn Ngọc Anh")
        .email("nguyenngocanh@example.com")
        .password("password456")
        .firstName("Nguyễn")
        .lastName("Ngọc Anh")
        .gender(Gender.FEMALE)
        .visibility("private")
        .role("user")
        .bio("Chuyên viên phân tích dữ liệu với 5 năm kinh nghiệm")
        .location("Hồ Chí Minh, Việt Nam")
        .work("Data Scientist tại Analysis Vietnam")
        .education("Đại học Khoa học Tự nhiên TP.HCM")
        .createdAt(Instant.now().minus(10, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar2.jpg")
        .backgroundImage("background2.jpg")
        .dateOfBirth(LocalDate.of(1994, 11, 20))
        .isEmailVerified(true)
        .build());

    // User 3
    users.add(UserDocument.builder()
        .id("daf")
        .username("Trần Văn Anh")
        .email("trananh@example.com")
        .password("password789")
        .firstName("Trần")
        .lastName("Văn Anh")
        .gender(Gender.MALE)
        .visibility("public")
        .role("admin")
        .bio("Quản trị hệ thống và DevOps engineer")
        .location("Đà Nẵng, Việt Nam")
        .work("System Administrator tại CloudTech")
        .education("Đại học Đà Nẵng")
        .createdAt(Instant.now().minus(20, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar3.jpg")
        .backgroundImage("background3.jpg")
        .dateOfBirth(LocalDate.of(1992, 3, 8))
        .isEmailVerified(true)
        .build());

    // User 4
    users.add(UserDocument.builder()
        .id("asdfasdf")
        .username("Lê Thị Mai Anh")
        .email("maianh@example.com")
        .password("passwordabc")
        .firstName("Lê")
        .lastName("Thị Mai Anh")
        .gender(Gender.FEMALE)
        .visibility("public")
        .role("user")
        .bio("Giáo viên tiếng Anh và người sáng tạo nội dung")
        .location("Hải Phòng, Việt Nam")
        .work("English Teacher tại Language Center")
        .education("Đại học Ngoại ngữ Hà Nội")
        .createdAt(Instant.now().minus(15, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar4.jpg")
        .backgroundImage("background4.jpg")
        .dateOfBirth(LocalDate.of(1996, 9, 25))
        .isEmailVerified(true)
        .build());

    // User 5
    users.add(UserDocument.builder()
        .id("asfdasdf")
        .username("Phạm Đức Anh")
        .email("ducanh@example.com")
        .password("passworddef")
        .firstName("Phạm")
        .lastName("Đức Anh")
        .gender(Gender.MALE)
        .visibility("private")
        .role("user")
        .bio("Kỹ sư xây dựng và nhà thiết kế nội thất")
        .location("Cần Thơ, Việt Nam")
        .work("Civil Engineer tại Construction Group")
        .education("Đại học Xây dựng")
        .createdAt(Instant.now().minus(30, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar5.jpg")
        .backgroundImage("background5.jpg")
        .dateOfBirth(LocalDate.of(1991, 7, 12))
        .isEmailVerified(true)
        .build());

    // User 6
    users.add(UserDocument.builder()
        .id("rweqr")
        .username("Hoàng Thị Khánh Linh")
        .email("khanhlinh@example.com")
        .password("passwordghi")
        .firstName("Hoàng")
        .lastName("Thị Khánh Linh")
        .gender(Gender.FEMALE)
        .visibility("public")
        .role("user")
        .bio("Chuyên viên marketing với đam mê công nghệ")
        .location("Hà Nội, Việt Nam")
        .work("Marketing Manager tại Digital Agency")
        .education("Đại học Ngoại thương")
        .createdAt(Instant.now().minus(25, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar6.jpg")
        .backgroundImage("background6.jpg")
        .dateOfBirth(LocalDate.of(1993, 4, 18))
        .isEmailVerified(true)
        .build());

    // User 7
    users.add(UserDocument.builder()
        .id("gqertwr")
        .username("Vũ Hoàng Anh")
        .email("vuhoanganh@example.com")
        .password("passwordjkl")
        .firstName("Vũ")
        .lastName("Hoàng Anh")
        .gender(Gender.MALE)
        .visibility("public")
        .role("user")
        .bio("Nhiếp ảnh gia và nhà làm phim tài liệu")
        .location("Hồ Chí Minh, Việt Nam")
        .work("Photographer tại Creative Studio")
        .education("Đại học Sân khấu Điện ảnh TP.HCM")
        .createdAt(Instant.now().minus(40, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar7.jpg")
        .backgroundImage("background7.jpg")
        .dateOfBirth(LocalDate.of(1990, 5, 22))
        .isEmailVerified(true)
        .build());

    // User 8
    users.add(UserDocument.builder()
        .id("gqewfe")
        .username("Nguyễn Thị Minh Tuyết")
        .email("minhtuyet@example.com")
        .password("passwordmno")
        .firstName("Nguyễn")
        .lastName("Thị Minh Tuyết")
        .gender(Gender.FEMALE)
        .visibility("private")
        .role("user")
        .bio("Nhà nghiên cứu khoa học và giảng viên đại học")
        .location("Huế, Việt Nam")
        .work("Researcher tại University of Science")
        .education("Tiến sĩ tại Đại học Khoa học Huế")
        .createdAt(Instant.now().minus(35, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar8.jpg")
        .backgroundImage("background8.jpg")
        .dateOfBirth(LocalDate.of(1988, 12, 5))
        .isEmailVerified(true)
        .build());

    // User 9
    users.add(UserDocument.builder()
        .id("gqegasd")
        .username("Đặng Anh Tuấn")
        .email("anhtuan@example.com")
        .password("passwordpqr")
        .firstName("Đặng")
        .lastName("Anh Tuấn")
        .gender(Gender.MALE)
        .visibility("public")
        .role("user")
        .bio("Kỹ sư phần mềm với sở thích chơi guitar")
        .location("Đà Nẵng, Việt Nam")
        .work("Software Engineer tại Tech Solutions")
        .education("Đại học FPT")
        .createdAt(Instant.now().minus(45, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar9.jpg")
        .backgroundImage("background9.jpg")
        .dateOfBirth(LocalDate.of(1997, 2, 28))
        .isEmailVerified(false)
        .build());

    // User 10
    users.add(UserDocument.builder()
        .id("gqueih")
        .username("Trần Thị Hồng Nhung")
        .email("hongnhung@example.com")
        .password("passwordstu")
        .firstName("Trần")
        .lastName("Thị Hồng Nhung")
        .gender(Gender.FEMALE)
        .visibility("public")
        .role("user")
        .bio("Chuyên viên tư vấn tài chính và đam mê du lịch")
        .location("Hà Nội, Việt Nam")
        .work("Financial Advisor tại Finance Group")
        .education("Đại học Kinh tế Quốc dân")
        .createdAt(Instant.now().minus(50, ChronoUnit.DAYS))
        .updatedAt(Instant.now())
        .avatar("avatar10.jpg")
        .backgroundImage("background10.jpg")
        .dateOfBirth(LocalDate.of(1992, 10, 15))
        .isEmailVerified(true)
        .build());

    // Lưu tất cả users
    userElasticsearchRepository.saveAll(users);
    return "Hello World!";
  }
}
