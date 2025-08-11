package com.studentapp.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics = "marks-updates-topic", groupId = "student-app-group")
    public void handleMarksEvent(Object message) {
        log.info("📥 Received Marks Event: {}", message);
        // Optional: trigger grade re-calculation
    }

    @KafkaListener(topics = "grade-updates-topic", groupId = "student-app-group")
    public void handleGradeEvent(Object message) {
        log.info("📥 Received Grade Update Event: {}", message);
    }
}