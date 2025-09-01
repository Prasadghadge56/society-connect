package com.society.controller;

import com.society.entity.Notice;
import com.society.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    //  Admin posts notice
    @PostMapping("/add")
    public Notice addNotice(@RequestBody Notice notice) {
        notice.setCreatedAt(LocalDateTime.now());
        return noticeRepository.save(notice);
    }

    // 👉 Anyone can view notices
    @GetMapping("/all")
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    //  Optional: Admin deletes notice
    @DeleteMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id) {
        noticeRepository.deleteById(id);
        return "Notice deleted!";
    }
}