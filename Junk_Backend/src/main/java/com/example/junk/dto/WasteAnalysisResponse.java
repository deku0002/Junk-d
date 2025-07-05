package com.example.junk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WasteAnalysisResponse {
    
    @JsonProperty("wasteType")
    private String wasteType;
    
    @JsonProperty("sorted")
    private boolean sorted;
    
    @JsonProperty("recyclableDetected")
    private boolean recyclableDetected;
    
    @JsonProperty("score")
    private int score;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("coinsEarned")
    private int coinsEarned;
    
    @JsonProperty("totalCoins")
    private int totalCoins;
    
    @JsonProperty("ranking")
    private int ranking;
    
    @JsonProperty("success")
    private boolean success;
    
    // Constructors
    public WasteAnalysisResponse() {}
    
    public WasteAnalysisResponse(String wasteType, boolean sorted, boolean recyclableDetected, int score) {
        this.wasteType = wasteType;
        this.sorted = sorted;
        this.recyclableDetected = recyclableDetected;
        this.score = score;
        this.success = true;
    }
    
    // Static factory methods
    public static WasteAnalysisResponse success(String wasteType, boolean sorted, boolean recyclableDetected, int score) {
        WasteAnalysisResponse response = new WasteAnalysisResponse(wasteType, sorted, recyclableDetected, score);
        response.calculateCoins();
        return response;
    }
    
    public static WasteAnalysisResponse error(String message) {
        WasteAnalysisResponse response = new WasteAnalysisResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
    
    // Calculate coins based on score
    private void calculateCoins() {
        if (score >= 90) {
            coinsEarned = 10;
            message = "Excellent sorting! 🌟";
        } else if (score >= 75) {
            coinsEarned = 7;
            message = "Great job! 👍";
        } else if (score >= 50) {
            coinsEarned = 5;
            message = "Good effort! 💪";
        } else {
            coinsEarned = 2;
            message = "Keep improving! 📈";
        }
    }
    
    // Getters and Setters
    public String getWasteType() {
        return wasteType;
    }
    
    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }
    
    public boolean isSorted() {
        return sorted;
    }
    
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }
    
    public boolean isRecyclableDetected() {
        return recyclableDetected;
    }
    
    public void setRecyclableDetected(boolean recyclableDetected) {
        this.recyclableDetected = recyclableDetected;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getCoinsEarned() {
        return coinsEarned;
    }
    
    public void setCoinsEarned(int coinsEarned) {
        this.coinsEarned = coinsEarned;
    }
    
    public int getTotalCoins() {
        return totalCoins;
    }
    
    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }
    
    public int getRanking() {
        return ranking;
    }
    
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    @Override
    public String toString() {
        return "WasteAnalysisResponse{" +
                "wasteType='" + wasteType + '\'' +
                ", sorted=" + sorted +
                ", recyclableDetected=" + recyclableDetected +
                ", score=" + score +
                ", message='" + message + '\'' +
                ", coinsEarned=" + coinsEarned +
                ", success=" + success +
                '}';
    }
}