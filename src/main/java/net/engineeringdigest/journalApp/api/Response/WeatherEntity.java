package net.engineeringdigest.journalApp.api.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WeatherEntity
{
    private Current current;

    @Data
    public class Current
    {

        @JsonProperty("observation_time")
        private String observationTime ;

        private int temperature;

        @JsonProperty("weather_descriptions")
        private ArrayList<String> weatherDescriptions;

        private int feelslike;
    }

}


