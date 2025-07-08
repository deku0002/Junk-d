import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/api/waste")
public class WasteAnalysisController {

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeWaste(@RequestBody Map<String, String> payload) {
        try {
            String imageData = payload.get("imageData");

            // Prepare request for Python API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("imageData", imageData);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            // Make request to Flask API
            ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:5000/analyze", request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("wasteType", response.getBody().get("wasteType"));
                result.put("sorted", response.getBody().get("sorted"));
                result.put("recyclableDetected", response.getBody().get("recyclableDetected"));
                result.put("score", response.getBody().get("score"));
                result.put("coinsEarned", response.getBody().get("coinsEarned"));
                result.put("confidence", response.getBody().get("confidence"));

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(Map.of("error", "AI model did not return a valid response"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
