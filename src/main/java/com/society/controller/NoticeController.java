package com.society.controller;

import com.society.entity.Notice;
import com.society.entity.User;
import com.society.enums.Role;
import com.society.repository.NoticeRepository;
import com.society.repository.UserRepository;
import com.society.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    //  Admin posts notice + send email
    @PostMapping("/add/{userId}")
    public Notice addNotice(@PathVariable Long userId, @RequestBody Notice notice) {
        // ✅ Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Check if user has ADMIN role
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admin can post notices!");
        }

        notice.setCreatedAt(LocalDateTime.now());
        Notice savedNotice = noticeRepository.save(notice);

        // Fetch all resident emails
        List<String> emails = userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        // Send email to everyone
        emailService.sendNoticeEmail(
                "📢 New Society Notice: " + savedNotice.getTitle(),
                savedNotice.getDescription(),
                emails
        );

        return savedNotice;
    }



    //  Anyone can view notices
    @GetMapping("/all")
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    //  Admin deletes notice
    @DeleteMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeRepository.deleteById(id);
        return "Notice deleted!";
    }
}
