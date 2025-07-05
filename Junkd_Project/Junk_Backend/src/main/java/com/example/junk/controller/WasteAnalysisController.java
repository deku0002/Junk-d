package com.example.junk.controller;

import com.example.junk.dto.WasteAnalysisRequest;
import com.example.junk.dto.WasteAnalysisResponse;
import com.example.junk.service.WasteAnalysisService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/waste")
@CrossOrigin(origins = "*")
public class WasteAnalysisController {
    
    private static final Logger logger = LoggerFactory.getLogger(WasteAnalysisController.class);
    
    @Autowired
    private WasteAnalysisService wasteAnalysisService;
    
    @PostMapping("/analyze")
    public ResponseEntity<WasteAnalysisResponse> analyzeWaste(
            @Valid @RequestBody WasteAnalysisRequest request) {
        
        logger.info("📱 Received waste analysis request from user: {}", request.getUserId());
        
        try {
            WasteAnalysisResponse response = wasteAnalysisService.analyzeWaste(request);
            
            if (response.isSuccess()) {
                logger.info("✅ Analysis successful: {}", response);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("⚠️ Analysis failed: {}", response.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            logger.error("❌ Error processing waste analysis", e);
            WasteAnalysisResponse errorResponse = WasteAnalysisResponse.error(
                "Internal server error: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/analyze/upload")
    public ResponseEntity<WasteAnalysisResponse> analyzeWasteWithFileUpload(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("userId") String userId) {
        
        logger.info("📷 Received image upload from user: {}", userId);
        
        try {
            // Validate file
            if (imageFile.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    WasteAnalysisResponse.error("No image file provided")
                );
            }
            
            // Convert to base64
            byte[] imageBytes = imageFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            
            // Create request
            WasteAnalysisRequest request = new WasteAnalysisRequest(base64Image, userId);
            request.setTimestamp(String.valueOf(System.currentTimeMillis()));
            
            // Process analysis
            WasteAnalysisResponse response = wasteAnalysisService.analyzeWaste(request);
            
            logger.info("✅ File upload analysis completed for user: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error processing file upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                WasteAnalysisResponse.error("Error processing image: " + e.getMessage())
            );
        }
    }
    
    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Object>> getLeaderboard() {
        logger.info("🏆 Fetching leaderboard");
        
        try {
            Map<String, Integer> leaderboard = wasteAnalysisService.getLeaderboard();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("leaderboard", leaderboard);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error fetching leaderboard", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String userId) {
        logger.info("📊 Fetching stats for user: {}", userId);
        
        try {
            Map<String, Object> stats = wasteAnalysisService.getUserStats(userId);
            stats.put("success", true);
            stats.put("userId", userId);
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("❌ Error fetching user stats", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Junk'd Waste Analysis API");
        health.put("timestamp", System.currentTimeMillis());
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
    
    @PostMapping("/demo")
    public ResponseEntity<WasteAnalysisResponse> demoAnalysis() {
        logger.info("🎭 Demo analysis requested");
        
        // Create demo request
        WasteAnalysisRequest demoRequest = new WasteAnalysisRequest("demo_image_data", "demo_user");
        WasteAnalysisResponse response = wasteAnalysisService.analyzeWaste(demoRequest);
        
        return ResponseEntity.ok(response);
    }
}