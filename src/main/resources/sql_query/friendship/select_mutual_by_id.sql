SELECT
    user_id2
from friendships
where user_id1 = ?
INTERSECT
SELECT
    user_id2
from friendships
where user_id1 = ?

