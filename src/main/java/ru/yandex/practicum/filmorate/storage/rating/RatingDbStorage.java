package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.storage.reader.FileReader;

import java.util.List;

@Component
@Primary
public class RatingDbStorage implements RatingStorage{
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql_query/rating/";
    private static final String SELECT_ALL_SQL_PATH = SQL_QUERY_DIR + "select_all.sql";
    private static final String SELECT_BY_ID_SQL_PATH = SQL_QUERY_DIR + "select_by_id.sql";

    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Rating> getRatings() {
        return jdbcTemplate.query(FileReader.readString(SELECT_ALL_SQL_PATH), new RatingMapper());
    }

    @Override
    public Rating getRating(Integer id) {
        return jdbcTemplate.query(FileReader.readString(SELECT_BY_ID_SQL_PATH), new RatingMapper(), id)
                .stream().findAny().orElse(null);
    }

}
