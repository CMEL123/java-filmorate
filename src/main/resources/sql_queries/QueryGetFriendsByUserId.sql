--список общих друзей с другим пользователем. paramUserId

Select
  userId1
from Friends
where userId2 = paramUserId
UNION
Select
  userId2
from Friends
where userId1 = paramUserId