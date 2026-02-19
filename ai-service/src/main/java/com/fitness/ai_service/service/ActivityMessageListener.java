package com.fitness.ai_service.service;

import com.fitness.ai_service.model.Activity;
import com.fitness.ai_service.model.Recommendation;
import com.fitness.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAiService aiService;
    private  final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void processActivity(Activity activity){
        log.info("Received Activity for Processing in AI-SERVICE Activity-> {}", activity.toString());
        Recommendation recommendation=aiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);

    }

}
