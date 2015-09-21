ADD JAR <path-to-hive-serdes-jar>;

CREATE EXTERNAL TABLE account_tweets (
  sourceFeed STRING,
  feedTitle STRING,
  feedLink STRING,
  feedDescription STRING,
  feedComments STRING,
  publishedDate STRING,
  feedCategories:ARRAY<STRUCT<categorie:STRING>>>,
  contents:ARRAY<STRUCT<content:STRING>>>
) 
PARTITIONED BY (datehour INT)
ROW FORMAT SERDE 'com.amazon.elasticmapreduce.JsonSerde'
	WITH serdeproperties ( 'paths'='sourceFeed, feedTitle, feedLink, feedDescription, feedComments, publishedDate,feedCategories, contents' )
LOCATION '/user/flume/rss_feed';