
DROP TABLE user;
DROP TABLE tweets;
DROP TABLE following;
DROP VIEW user_tweets;
DROP DATABASE twitter;
create DATABASE twitter;
USE twitter;

CREATE TABLE user(
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(50),
    picture_path VARCHAR(350)
);

describe user;

CREATE TABLE tweets(
    user_id INT,
    tweet_id INT AUTO_INCREMENT PRIMARY KEY,
    tweet_text VARCHAR(100),
    tweet_picture VARCHAR(350),
    tweet_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

describe tweets;

CREATE TABLE following(
    user_id INT,
    following_user_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (following_user_id) REFERENCES user(id)
);

describe following;

CREATE VIEW user_tweets AS

    SELECT  tweets.tweet_id, tweets.tweet_text, tweets.tweet_picture,
    tweets.tweet_date, user.first_name, user.last_name, user.picture_path
    FROM tweets
    INNER JOIN user
    on tweets.user_id = user.id ;

describe user_tweets;