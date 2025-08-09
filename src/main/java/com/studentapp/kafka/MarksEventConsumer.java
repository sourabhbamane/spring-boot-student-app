package com.studentapp.kafka;

import com.studentapp.service.GradeCalculationScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MarksEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MarksEventConsumer.class);

    private final GradeCalculationScheduler scheduler;

    public MarksEventConsumer(GradeCalculationScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @KafkaListener(topics = "marks-events", groupId = "studentapp-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(MarksEvent event) {
        logger.info("📥 Consumed Kafka event: {}", event);
        logger.info("📊 Triggering grade recalculation...");

        // ⏱ Trigger scheduler logic immediately
        scheduler.recalculateGrades();
    }
}