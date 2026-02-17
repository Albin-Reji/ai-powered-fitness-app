package com.fitness.ai_service.service;

import com.fitness.ai_service.exception.InvalidActivityException;
import com.fitness.ai_service.model.Recommendation;
import com.fitness.ai_service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getByActivityId(String activityId) {
        return recommendationRepository.findByActivityId(activityId).orElseThrow(
                () -> new InvalidActivityException("No Recommendation Found for activityId: "+activityId)
        );
    }
}
