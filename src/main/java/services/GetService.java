package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GetService {
    private static final String appid = "3643c25db6f300f6d126dfcab529d5cd";
    private static final String urlLat = "http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}";
    private static final String urlName = "http://api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}";
    private RestTemplate restTemplate = new RestTemplate();


    public Resp getDataByNameOrId(String nameOfCity) throws JsonProcessingException {
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.getForEntity(urlName, String.class, nameOfCity, appid);
        } catch (Exception e) {
            System.out.println("Не нашлось информации для этого города");
        }

        return mapJsonToObject(response);
    }

    public Resp getByLatAndLon(int lat, int lon) throws JsonProcessingException {
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.getForEntity(urlLat, String.class, lat, lon, appid);
        } catch (Exception e) {
            System.out.println("Не нашлось информации для этого города");
        }

        return mapJsonToObject(response);
    }


    private Resp mapJsonToObject(ResponseEntity<String> json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        return json == null ? null : objectMapper.readValue(json.getBody(), Resp.class);
    }
}
