package ru.yandex.practicum.filmorate.storage;

import java.util.Map;

public abstract class GeneratorId {

    protected long getNextId(Map data) {
        long currentMaxId = data.keySet()
                .stream()
                .mapToLong(id -> (long) id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}