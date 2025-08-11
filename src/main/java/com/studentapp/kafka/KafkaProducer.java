package com.studentapp.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaJsonTemplate;
    private final KafkaTemplate<String, String> kafkaStringTemplate;

    // Topic constants
    private static final String MARKS_TOPIC = "marks-events";
    private static final String FULL_GRADE_RECALC_TOPIC = "grade-recalc-topic";
    private static final String SINGLE_GRADE_RECALC_TOPIC = "grade-single-recalc-topic";

    // Send a MarksEvent to Kafka (JSON)

    public void sendMarksEvent(MarksEvent event) {
        kafkaJsonTemplate.send(MARKS_TOPIC, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("✅ Sent MarksEvent: {}", event);
                    } else {
                        logger.error("❌ Failed to send MarksEvent", ex);
                    }
                });
    }

    //Trigger full grade recalculation
    public void sendFullGradeRecalcTrigger(String trigger) {
        kafkaStringTemplate.send(FULL_GRADE_RECALC_TOPIC, trigger)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("📤 Sent full grade recalculation trigger");
                    } else {
                        logger.error("❌ Failed to send full recalculation trigger", ex);
                    }
                });
    }

    //Trigger single student recalculation

    public void sendStudentGradeRecalcTrigger(Long studentId) {
        kafkaStringTemplate.send(SINGLE_GRADE_RECALC_TOPIC, String.valueOf(studentId))
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("📤 Sent recalculation trigger for studentId: {}", studentId);
                    } else {
                        logger.error("❌ Failed to send single recalculation trigger for {}", studentId, ex);
                    }
                });
    }
}