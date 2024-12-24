SELECT
    u.id,
    u.email,
    u.login,
    u.name,
    u.birthday
FROM users u
where u.id = ?