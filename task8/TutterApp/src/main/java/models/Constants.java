package models;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String PATH = "D:\\GitHub\\MyTwitter\\task8\\TutterApp\\src\\main\\webapp\\resources\\Posts.txt";
    public static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
