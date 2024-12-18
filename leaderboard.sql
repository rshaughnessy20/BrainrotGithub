SELECT u.name, l.gametype 
FROM user u 
JOIN leaderboard l ON u.userid = l.userid 
WHERE l.gametype = 100 
ORDER BY l.gametype DESC 
LIMIT 10;

SELECT * FROM brainrottranslator.leaderboard;