package com.fitness.activity_service.controller;

import com.fitness.activity_service.dto.ActivityRequest;
import com.fitness.activity_service.dto.ActivityResponse;
import com.fitness.activity_service.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/")
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request , @RequestHeader("X-User-ID") String userId){
        if(userId!=null){
           request.setUserId(userId);
        }
        request.setUserId(userId);
        return ResponseEntity.ok(activityService.trackActivity(request));
    }
    @GetMapping("/")
    public ResponseEntity<List<ActivityResponse>> getUserActivity(@RequestHeader("X-User-ID") String userId){
        log.info("X-USER-ID ActivityController: {}", userId);
        return ResponseEntity.ok(activityService.getUserActivity(userId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String activityId){
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }
}
