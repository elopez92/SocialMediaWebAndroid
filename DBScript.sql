DROP DATABASE twitter;
create DATABASE twitter;
USE twitter;

CREATE TABLE login (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(50),
    picture_path VARCHAR(350)
);

describe login;

CREATE TABLE tweets(
    user_id INT,
    tweet_id INT AUTO_INCREMENT PRIMARY KEY,
    tweet_text VARCHAR(100),
    tweet_picture VARCHAR(350),
    tweet_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES login(user_id)
);

describe tweets;

CREATE TABLE following(
    user_id INT,
    following_user_id INT,
    FOREIGN KEY (user_id) REFERENCES login(user_id),
    FOREIGN KEY (following_user_id) REFERENCES login(user_id)
);

describe following;

CREATE VIEW user_tweets AS

    SELECT  tweets.tweet_id, tweets.tweet_text, tweets.tweet_picture,
    tweets.tweet_date, login.first_name, login.picture_path
    FROM tweets
    INNER JOIN login
    on tweets.user_id = login.user_id ;

describe user_tweets;