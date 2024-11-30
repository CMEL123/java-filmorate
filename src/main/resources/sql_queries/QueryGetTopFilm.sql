 -- получить топ N наиболее популярных фильмов
 
Select
  f.name
from film f
left join likes l
on f.id = l.filmId
group by f.name
order by count(l.userId)
limit 10 -- N