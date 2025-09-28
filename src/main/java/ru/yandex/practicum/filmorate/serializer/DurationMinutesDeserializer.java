package ru.yandex.practicum.filmorate.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationMinutesDeserializer extends StdDeserializer<Duration> {

    public DurationMinutesDeserializer() {
        super(Duration.class);
    }

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        int minutes = p.getIntValue();
        return Duration.ofMinutes(minutes);
    }
}