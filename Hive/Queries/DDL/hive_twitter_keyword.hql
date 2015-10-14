ADD JAR hdfs://master.hs.osnabrueck.de:8020/user/hive/lib/jsonserde.jar;

CREATE EXTERNAL TABLE keyword_tweets (
  id BIGINT,
  created_at STRING,
  source STRING,
  favorited BOOLEAN,
  retweeted_status STRUCT<
    text:STRING,
    user:STRUCT<screen_name:STRING,name:STRING>,
    retweet_count:INT>,
  entities STRUCT<
    urls:ARRAY<STRUCT<expanded_url:STRING>>,
    user_mentions:ARRAY<STRUCT<screen_name:STRING,name:STRING>>,
    hashtags:ARRAY<STRUCT<text:STRING>>>,
  text STRING,
  user STRUCT<
    screen_name:STRING,
    name:STRING,
    friends_count:INT,
    followers_count:INT,
    statuses_count:INT,
    verified:BOOLEAN,
    utc_offset:INT,
    time_zone:STRING>,
  in_reply_to_screen_name STRING
) 
PARTITIONED BY (year STRING, month STRING, day STRING)
ROW FORMAT SERDE 'com.amazon.elasticmapreduce.JsonSerde'
	WITH serdeproperties ( 'paths'='id, created_at, source, favorited, retweeted_status, entities, text, user, in_reply_to_screen_name' )
LOCATION '/user/flume/keyword/tweets';