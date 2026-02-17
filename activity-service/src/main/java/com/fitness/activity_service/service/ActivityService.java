package com.fitness.activity_service.service;

import com.fitness.activity_service.dto.ActivityRequest;
import com.fitness.activity_service.dto.ActivityResponse;
import com.fitness.activity_service.exception.InvalidActivityId;
import com.fitness.activity_service.model.Activity;
import com.fitness.activity_service.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest request) {
        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .caloriesBurned(request.getCaloriesBurned())
                .duration(request.getDuration())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity savedActivity=activityRepository.save(activity);

        return mapToActivityResponse(savedActivity);
    }

    public List<ActivityResponse> getUserActivity(String userId) {
        List<Activity> activityList=activityRepository.findByUserId(userId);
        return  activityList.stream()
                .map(this::mapToActivityResponse)
                .toList();
    }

    public ActivityResponse getActivityById(String activityId) {
        return mapToActivityResponse(
                activityRepository.findById(activityId)
                        .orElseThrow(()-> new InvalidActivityId("Invalid Activity ID"))
        );
    }

    /* Map Activtiy to ActivityResponse*/
    public ActivityResponse mapToActivityResponse(Activity activity){
        return ActivityResponse.builder()
                .id(activity.getId())
                .userId(activity.getUserId())
                .additionalMetrics(activity.getAdditionalMetrics())
                .updatedAt(activity.getUpdatedAt())
                .startTime(activity.getStartTime())
                .type(activity.getType())
                .createdAt(activity.getCreatedAt())
                .caloriesBurned(activity.getCaloriesBurned())
                .duration(activity.getDuration())
                .build();
    }
}
