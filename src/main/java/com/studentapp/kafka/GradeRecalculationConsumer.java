package com.studentapp.kafka;

import com.studentapp.service.GradeCalculationScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeRecalculationConsumer {

    private final GradeCalculationScheduler scheduler;
    private static final Logger logger = LoggerFactory.getLogger(GradeRecalculationConsumer.class);


    //Listener for single-student recalculation
    @KafkaListener(
            topics = "grade-single-recalc-topic",
            groupId = "student-app-group",
            containerFactory = "kafkaStringListenerContainerFactory"
    )
    public void handleSingleStudentRecalculation(String studentIdStr) {
        try {
            Long studentId = Long.parseLong(studentIdStr);
            logger.info("📥 Received single student recalculation trigger for studentId: {}", studentId);
            scheduler.recalculateGradeForStudent(studentId);
        } catch (NumberFormatException ex) {
            logger.error("❌ Invalid studentId received: {}", studentIdStr);
        }
    }

    @KafkaListener(
            topics = "grade-recalc-topic",
            groupId = "student-app-group",
            containerFactory = "kafkaStringListenerContainerFactory"
    )
    public void handleFullRecalculation(String message) {
        logger.info("📥 Received full grade recalculation trigger: {}", message);
        scheduler.recalculateGrades();
    }
}