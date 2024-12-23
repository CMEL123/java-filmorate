package ru.yandex.practicum.filmorate.storage.reader;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public final class FileReader {

    public static String readString(String filePath) throws NoSuchElementException {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException ioException) {
            throw new ValidationException("Ошибка чтения запроса");
        }
    }

}