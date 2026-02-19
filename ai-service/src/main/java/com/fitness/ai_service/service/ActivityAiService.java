package com.fitness.ai_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.ai_service.model.Activity;
import com.fitness.ai_service.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt=createPromptForActivity( activity);
        String aiResponse=geminiService.getResponse(prompt);
        log.info("RESPONSE FROM AI {}", aiResponse);
        return  processAiResponse(activity, aiResponse);
    }
    private Recommendation processAiResponse(Activity activity, String aiResponse){
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(aiResponse);

            JsonNode textNode=rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent=textNode.asText()
                                       .replaceAll("```json", "")
                                       .replaceAll("\\n```", "")
                                       .trim();

            log.info("PARSE RESPONSE FROM AI {}", jsonContent);

            JsonNode analysisJson=mapper.readTree(jsonContent);

            if (analysisJson.isTextual()) {
                analysisJson = mapper.readTree(analysisJson.asText());
            }

            JsonNode analysisNode=analysisJson.path("analysis");
            StringBuilder fullAnalysis=new StringBuilder();
            addAnalysisSection(analysisNode, fullAnalysis, "overall", "Overall: ");
            addAnalysisSection(analysisNode, fullAnalysis, "pace", "Pace: ");
            addAnalysisSection(analysisNode, fullAnalysis, "heartRate", "Heart Rate: ");
            addAnalysisSection(analysisNode, fullAnalysis, "caloriesBurned", "Calories Burned: ");

            List<String> improvements=extractImprovement(analysisJson.path("improvements"));
            List<String> suggestions=extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety=extractSafety(analysisJson.path("safety"));
            
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvement(improvements)
                    .safety(safety)
                    .activityType(activity.getType())
                    .suggestion(suggestions)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        catch (Exception e){
            log.error("Error in Parsing AI Response",e);
        }
        return  createDefaiultRecommendation(activity);
    }

    private Recommendation createDefaiultRecommendation(Activity activity) {
        return Recommendation.builder()
                             .activityId(activity.getId())
                             .userId(activity.getUserId())
                             .recommendation("Unable to Generate recommendation")
                             .improvement(Collections.singletonList("Continue with your current training"))
                             .safety(Arrays.asList(
                                     "Always warm up",
                                     "Stay hydrated",
                                     "Listen to your body"
                             ))
                             .activityType(activity.getType())
                             .suggestion(Collections.singletonList("Consider consulting a fitness coach"))
                             .createdAt(LocalDateTime.now())
                             .build();
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safetyList=new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(safety-> safetyList.add(safety.asText()));
            return safetyList.isEmpty()?
                    Collections.singletonList("No Safety Response.."):
                    safetyList;
        }
        return  Collections.singletonList("No Safety Response..");

    }

    private List<String> extractSuggestions(JsonNode suggestions) {
        List<String> suggestionList=new ArrayList<>();
        if(suggestions.isArray()){
            suggestions.forEach(suggestion->{
                String workout=suggestion.path("workout").asText();
                String description=suggestion.path("description").asText();
                suggestionList.add(String.format("%s : %s", workout, description));
            });
            return suggestionList.isEmpty()?
                    Collections.singletonList("No Suggestions"):
                    suggestionList;
        }
		return Collections.singletonList("No Suggestions");
	}

    private List<String> extractImprovement(JsonNode improvementNode) {
        List<String> improvements=new ArrayList<>();
        if(improvementNode.isArray()){
            improvementNode.forEach(improvement->{
                String area=improvement.path("area").asText();
                String recommendation=improvement.path("recommendation").asText();
                improvements.add(String.format("%s : %s", area, recommendation));
            });
            return improvements.isEmpty()?
                    Collections.singletonList("No Specific Improvements"):
                    improvements;
        }
        return  Collections.singletonList("No Specific Improvements");
    }

    private void addAnalysisSection(JsonNode analysisNode, StringBuilder fullAnalysis, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                             activity.getType(),
                             activity.getDuration(),
                             activity.getCaloriesBurned(),
                             activity.getAdditionalMetrics()
        );
    }
}
