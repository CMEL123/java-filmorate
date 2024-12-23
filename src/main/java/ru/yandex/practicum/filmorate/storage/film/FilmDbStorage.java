package ru.yandex.practicum.filmorate.storage.film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.reader.FileReader;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@Slf4j
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql_query/film/";
    private static final String SELECT_ALL_SQL_PATH = SQL_QUERY_DIR + "select_all.sql";
    private static final String SELECT_BY_ID_SQL_PATH = SQL_QUERY_DIR + "select_by_id.sql";
    private static final String INSERT_SQL_PATH = SQL_QUERY_DIR + "insert.sql";
    private static final String UPDATE_SQL_PATH = SQL_QUERY_DIR + "update.sql";
    private static final String TOP_SQL_PATH = SQL_QUERY_DIR + "top.sql";

    private static final String SQL_QUERY_LIKE_DIR = "src/main/resources/sql_query/like/";
    private static final String INSERT_LIKE_SQL_PATH = SQL_QUERY_LIKE_DIR + "insert.sql";
    private static final String DELETE_LIKE_SQL_PATH = SQL_QUERY_LIKE_DIR + "delete.sql";
    private static final String SELECT_LIKE_SQL_PATH = SQL_QUERY_LIKE_DIR + "select_by_film_id.sql";

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(FileReader.readString(SELECT_ALL_SQL_PATH), new FilmMapper());
    }

    @Override
    public Film getFilm(long filmId) {
        Film film = jdbcTemplate.query(FileReader.readString(SELECT_BY_ID_SQL_PATH), new FilmMapper(), filmId).stream()
                .findAny().orElse(null);
        if (film == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        checkNewFilm(film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(FileReader.readString(INSERT_SQL_PATH),
                            Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                preparedStatement.setInt(5, film.getMpa().getId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.NULL);
            }
            return preparedStatement;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue()); //для тестов
        return getFilm(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        checkNewFilm(film);
        getFilm(film.getId());
        jdbcTemplate.update(FileReader.readString(UPDATE_SQL_PATH),
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                (film.getMpa() != null ? film.getMpa().getId() : null),
                film.getId());
        return getFilm(film.getId());
    }

    @Override
    public void addLike(Film film, long userId) {
        if (!getLike(film).contains(userId)) {
            jdbcTemplate.update(FileReader.readString(INSERT_LIKE_SQL_PATH), film.getId(), userId);
            film.addLike(userId);
        }
    }

    @Override
    public long getLikes(Film film) {
        return getLike(film).size();
    }

    @Override
    public Set<Long> getLike(Film film) {
        return new HashSet<>(jdbcTemplate.queryForList(FileReader.readString(SELECT_LIKE_SQL_PATH), Long.class, film.getId()));
    }

    @Override
    public void removeLike(Film film, long userId) {
        jdbcTemplate.update(FileReader.readString(DELETE_LIKE_SQL_PATH), film.getId(), userId);
        film.addLike(userId);
    }

    @Override
    public List<Film> getTopFilms(long count) {
        return jdbcTemplate.query(FileReader.readString(TOP_SQL_PATH), new FilmMapper(), (count + 1));
    }

    private void checkNewFilm(Film film) {

        //название не может быть пустым;
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }

        //максимальная длина описания — 200 символов;
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        //дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        //продолжительность фильма должна быть положительным числом.
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

    }

}