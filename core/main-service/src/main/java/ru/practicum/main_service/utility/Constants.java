package ru.practicum.main_service.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
   public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
}
