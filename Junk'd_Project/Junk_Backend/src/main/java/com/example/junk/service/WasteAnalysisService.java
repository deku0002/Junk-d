import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WasteAnalysisService {

    private final String PYTHON_API_URL = "http://localhost:5000/analyze";

    public Map<String, Object> analyze(String imageData) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("imageData", imageData);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(PYTHON_API_URL, requestEntity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to get a response from the AI model.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("wasteType", response.getBody().get("wasteType"));
        result.put("sorted", response.getBody().get("sorted"));
        result.put("recyclableDetected", response.getBody().get("recyclableDetected"));
        result.put("score", response.getBody().get("score"));
        result.put("coinsEarned", response.getBody().get("coinsEarned"));
        result.put("confidence", response.getBody().get("confidence"));

        return result;
    }
}
