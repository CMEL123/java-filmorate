package ru.yandex.practicum.filmorate.storage.genre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.reader.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql_query/genre/";
    private static final String SELECT_ALL_SQL_PATH = SQL_QUERY_DIR + "select_all.sql";
    private static final String SELECT_FILM_GENRES_SQL_PATH = SQL_QUERY_DIR + "select_film_genres.sql";
    private static final String SELECT_GENRES_ALL_FILMS_SQL_PATH = SQL_QUERY_DIR + "select_genres_all_films.sql";
    private static final String SELECT_BY_ID_SQL_PATH = SQL_QUERY_DIR + "select_by_id.sql";
    private static final String INSERT_SQL_PATH = SQL_QUERY_DIR + "insert_film_genres.sql";
    private static final String DELETE_SQL_PATH = SQL_QUERY_DIR + "delete_film_genres.sql";

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        return jdbcTemplate.query(FileReader.readString(SELECT_FILM_GENRES_SQL_PATH), new GenreMapper(), filmId);
    }

    @Override
    public void addGenres(Long filmId, Integer genreId) {
        jdbcTemplate.update(FileReader.readString(INSERT_SQL_PATH), filmId, genreId);
    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(jdbcTemplate.query(FileReader.readString(SELECT_ALL_SQL_PATH), new GenreMapper()));
    }

    @Override
    public List<Map<Long, Genre>> getAllFilmsGenres() {
        return jdbcTemplate.query(FileReader.readString(SELECT_GENRES_ALL_FILMS_SQL_PATH),
                (rs, rowNum) -> {
                    Map<Long, Genre> result = new HashMap<>();
                    result.put(rs.getLong("film_id"),
                            new Genre(rs.getInt("genre_id"),
                                    rs.getString("genre_name")));
                    return result;
                });
    }

    @Override
    public Genre getGenre(Integer genreId) {
        return jdbcTemplate.query(FileReader.readString(SELECT_BY_ID_SQL_PATH), new GenreMapper(), genreId)
                .stream().findAny().orElse(null);
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        jdbcTemplate.update(FileReader.readString(DELETE_SQL_PATH), filmId);
    }

}