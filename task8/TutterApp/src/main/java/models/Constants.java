package models;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String LOGGING_PROPERTIES = "logging.properties";
    public static final ObjectMapper OBJECT_MAPPER;
    public static final String DESCRIPTION = "description";
    public static final String TAG_ID = "tag_id";
    public static final String USER_ID = "user_id";
    public static final String POST_ID = "post_id";

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
