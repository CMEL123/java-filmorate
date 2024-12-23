package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.reader.FileReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_QUERY_DIR = "src/main/resources/sql_query/user/";
    private static final String SELECT_ALL_SQL_PATH = SQL_QUERY_DIR + "select_all.sql";
    private static final String SELECT_BY_ID_SQL_PATH = SQL_QUERY_DIR + "select_by_id.sql";
    private static final String INSERT_SQL_PATH = SQL_QUERY_DIR + "insert.sql";
    private static final String UPDATE_SQL_PATH = SQL_QUERY_DIR + "update.sql";

    private static final String SQL_QUERY_LIKE_DIR = "src/main/resources/sql_query/friendship/";
    private static final String INSERT_FRIEND_SQL_PATH = SQL_QUERY_LIKE_DIR + "insert.sql";
    private static final String DELETE_FRIEND_SQL_PATH = SQL_QUERY_LIKE_DIR + "delete.sql";
    private static final String SELECT_FRIEND_SQL_PATH = SQL_QUERY_LIKE_DIR + "select_by_id.sql";
    private static final String SELECT_MUTUAL_FRIEND_SQL_PATH = SQL_QUERY_LIKE_DIR + "select_mutual_by_id.sql";

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(FileReader.readString(SELECT_ALL_SQL_PATH), new UserMapper());
    }

    @Override
    public User getUser(long userId) {
        User user = jdbcTemplate.query(FileReader.readString(SELECT_BY_ID_SQL_PATH), new UserMapper(), userId).stream()
                .findAny().orElse(null);
        if (user == null) throw new NotFoundException("Нет пользователя с id: " + userId);
        return user;
    }

    @Override
    public User addUser(User user) {
        checkNewUser(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection
                        .prepareStatement(FileReader.readString(INSERT_SQL_PATH),
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
                return preparedStatement;
           }, keyHolder
        );
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue()); //для тестов
        return getUser(user.getId());
    }
    @Override
    public User updateUser(User newUser) {
        checkNewUser(newUser);
        getUser(newUser.getId());
        jdbcTemplate.update(FileReader.readString(UPDATE_SQL_PATH),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId()
        );
        return newUser;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        getUser(userId);
        getUser(friendId);
        jdbcTemplate.update(FileReader.readString(INSERT_FRIEND_SQL_PATH), userId, friendId);
    }

    @Override
    public void delFriend(long userId, long friendId) {
        getUser(userId);
        getUser(friendId);
        jdbcTemplate.update(FileReader.readString(DELETE_FRIEND_SQL_PATH), userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        getUser(userId);
        return jdbcTemplate.queryForList(FileReader.readString(SELECT_FRIEND_SQL_PATH), Long.class, userId).stream()
                .map(this::getUser).toList();
    }

    @Override
    public List<User> getMutualFriends(long userId1, long userId2) {
        getUser(userId1);
        getUser(userId2);
        return jdbcTemplate.queryForList(FileReader.readString(SELECT_MUTUAL_FRIEND_SQL_PATH),
                        Long.class,
                        userId1, userId2
                ).stream()
                .map(this::getUser).toList();
    }

    private void checkNewUser(User user) {
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        //электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        //логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        //дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем.");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

    }
}
