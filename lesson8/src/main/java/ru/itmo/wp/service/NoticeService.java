package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Notice;
import ru.itmo.wp.form.NoticeData;
import ru.itmo.wp.repository.NoticeRepository;

import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public Notice saveNotice(NoticeData noticeData) {
        Notice notice = new Notice();
        notice.setContent(noticeData.getContent());
        noticeRepository.save(notice);
        return notice;
    }

    public Notice findById(Long id) {
        return id == null ? null : noticeRepository.findById(id).orElse(null);
    }

    public List<Notice> findAll() {
        return noticeRepository.findAllByOrderByIdDesc();
    }
}
