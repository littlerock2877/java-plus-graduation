package ru.practicum.aggregator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aggregator.user.action.weight")
public final class UserActionWeight {
    public static double view;
    public static double register;
    public static double like;
}