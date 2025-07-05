package com.example.junk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WasteAnalysisRequest {
    
    @NotBlank(message = "Image data is required")
    private String imageData; // Base64 encoded image
    
    @NotNull(message = "User ID is required")
    private String userId;
    
    private String location;
    private String timestamp;
    
    // Constructors
    public WasteAnalysisRequest() {}
    
    public WasteAnalysisRequest(String imageData, String userId) {
        this.imageData = imageData;
        this.userId = userId;
    }
    
    // Getters and Setters
    public String getImageData() {
        return imageData;
    }
    
    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "WasteAnalysisRequest{" +
                "userId='" + userId + '\'' +
                ", location='" + location + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", imageDataLength=" + (imageData != null ? imageData.length() : 0) +
                '}';
    }
}