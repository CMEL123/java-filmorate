package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

import java.util.HashMap;

public class Controller<T>{
    @Getter
    public HashMap<Long, T> storageMap = new HashMap<>();

    // вспомогательный метод для генерации идентификатора нового поста
    protected long getNextId() {
        long currentMaxId = storageMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
