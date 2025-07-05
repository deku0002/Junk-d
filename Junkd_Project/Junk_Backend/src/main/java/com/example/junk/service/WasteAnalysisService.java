package com.example.junk.service;

import com.example.junk.dto.WasteAnalysisRequest;
import com.example.junk.dto.WasteAnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class WasteAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(WasteAnalysisService.class);
    
    @Autowired
    private WebClient webClient;
    
    @Value("${ai.model.base-url:http://localhost:5000}")
    private String aiModelBaseUrl;
    
    @Value("${ai.model.timeout:30000}")
    private int aiModelTimeout;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // In-memory storage for demo (replace with database in production)
    private final Map<String, Integer> userCoins = new HashMap<>();
    private final Map<String, Integer> userScores = new HashMap<>();
    
    public WasteAnalysisResponse analyzeWaste(WasteAnalysisRequest request) {
        logger.info("🔍 Starting waste analysis for user: {}", request.getUserId());
        
        try {
            // Call AI model API
            WasteAnalysisResponse aiResponse = callAiModel(request);
            
            if (aiResponse.isSuccess()) {
                // Update user coins and scores
                updateUserStats(request.getUserId(), aiResponse);
                
                // Set additional response fields
                aiResponse.setTotalCoins(getUserTotalCoins(request.getUserId()));
                aiResponse.setRanking(getUserRanking(request.getUserId()));
                
                logger.info("✅ Analysis completed successfully for user: {}", request.getUserId());
                return aiResponse;
            } else {
                logger.warn("⚠️ AI model returned error response");
                return createMockResponse(); // Fallback for demo
            }
            
        } catch (Exception e) {
            logger.error("❌ Error during waste analysis", e);
            return createMockResponse(); // Fallback for demo
        }
    }
    
    private WasteAnalysisResponse callAiModel(WasteAnalysisRequest request) {
        try {
            logger.info("🤖 Calling AI model at: {}", aiModelBaseUrl);
            
            // Prepare request payload for AI model
            Map<String, Object> aiRequest = new HashMap<>();
            aiRequest.put("image_data", request.getImageData());
            aiRequest.put("user_id", request.getUserId());
            
            // Make HTTP call to Python AI model
            String response = webClient.post()
                    .uri(aiModelBaseUrl + "/analyze")
                    .bodyValue(aiRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(aiModelTimeout))
                    .block();
            
            // Parse AI response
            JsonNode responseNode = objectMapper.readTree(response);
            
            return WasteAnalysisResponse.success(
                    responseNode.get("wasteType").asText(),
                    responseNode.get("sorted").asBoolean(),
                    responseNode.get("recyclableDetected").asBoolean(),
                    responseNode.get("score").asInt()
            );
            
        } catch (Exception e) {
            logger.error("🔥 AI model call failed", e);
            throw new RuntimeException("AI model unavailable", e);
        }
    }
    
    private WasteAnalysisResponse createMockResponse() {
        logger.info("🎭 Creating mock response for demo");
        
        // Generate random but realistic mock data
        String[] wasteTypes = {"Plastic", "Paper", "Organic", "Mixed", "E-waste"};
        String wasteType = wasteTypes[(int) (Math.random() * wasteTypes.length)];
        
        boolean sorted = Math.random() > 0.3; // 70% chance of being sorted
        boolean recyclable = Math.random() > 0.4; // 60% chance of being recyclable
        int score = (int) (Math.random() * 40) + 60; // Score between 60-100
        
        return WasteAnalysisResponse.success(wasteType, sorted, recyclable, score);
    }
    
    private void updateUserStats(String userId, WasteAnalysisResponse response) {
        // Update user coins
        int currentCoins = userCoins.getOrDefault(userId, 0);
        userCoins.put(userId, currentCoins + response.getCoinsEarned());
        
        // Update user total score
        int currentScore = userScores.getOrDefault(userId, 0);
        userScores.put(userId, currentScore + response.getScore());
        
        logger.info("📊 Updated stats for user {}: coins={}, totalScore={}", 
                   userId, userCoins.get(userId), userScores.get(userId));
    }
    
    private int getUserTotalCoins(String userId) {
        return userCoins.getOrDefault(userId, 0);
    }
    
    private int getUserRanking(String userId) {
        int userScore = userScores.getOrDefault(userId, 0);
        int rank = 1;
        
        for (int score : userScores.values()) {
            if (score > userScore) {
                rank++;
            }
        }
        
        return rank;
    }
    
    // Additional utility methods
    public Map<String, Integer> getLeaderboard() {
        logger.info("🏆 Generating leaderboard");
        return new HashMap<>(userScores);
    }
    
    public Map<String, Object> getUserStats(String userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCoins", getUserTotalCoins(userId));
        stats.put("totalScore", userScores.getOrDefault(userId, 0));
        stats.put("ranking", getUserRanking(userId));
        stats.put("totalAnalyses", 1); // This would be tracked in a real database
        
        return stats;
    }
}