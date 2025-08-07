package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.api.Response.WeatherEntity;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Autowired
    private AppCache appCache;

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherEntity getWeather(String city)
    {
        WeatherEntity  weatherResponse = redisService.getDataFromRedisDB("weather_of_" + city, WeatherEntity.class);
        if(weatherResponse!=null)
        {
            return weatherResponse;
        }
        else{
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY,apiKey).replace(Placeholders.CITY,city);
            ResponseEntity<WeatherEntity> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherEntity.class);
            WeatherEntity  body = response.getBody();
            if(body!=null)
            {
                redisService.setDataInRedisDB("weather_of_"+city,body,5000l);
            }
            return body;
        }
    }

    //Sample Post Method
    /*
    public WeatherEntity createNewWeatherEntry(String city)
    {
        String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);

        //Sending http headers with key value pairs
        //Represents the headers you can send in your HTTP request.
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.set("key","value");

        //We passed through postman
        //Send raw JSON data in the body, commonly used in POST APIs.
        String requestBody= "{\n"+
                "    \"userName\": \"vipul\",\n " +
                "     \"password\": \"vipul\", \n"+
                "}    ";
        //Represents the entire HTTP request (body + headers).
        HttpEntity<String> httpEntity=new HttpEntity<>(requestBody,httpHeaders);
        // Might be used instead of raw JSON to let RestTemplate convert the object to JSON automatically.
        User user = User.builder().username("vipul").password("vipul").build();
        //Represents the entire HTTP request (body + headers).
        HttpEntity<User> httpEntity1=new HttpEntity<>(user,httpHeaders);

        /*restTemplate.exchange(...): Makes an HTTP request.
        finalAPI: URL with city + API key. HttpMethod.POST: Request method. httpEntity: Contains body (JSON string) and headers
        WeatherEntity.class: Expected response type to convert the JSON into Java object
        Returns: ResponseEntity<WeatherEntity> – holds HTTP status, headers, and body.
        Purpose: Actually sends the request and receives the weather data.

        ResponseEntity<WeatherEntity> weatherBody = restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, WeatherEntity.class);
        WeatherEntity  weatherResponse = weatherBody.getBody();
        return weatherResponse;
    }
    */
}
