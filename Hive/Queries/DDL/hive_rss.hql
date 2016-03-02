ADD JAR hdfs://master.hs.osnabrueck.de:8020/user/hive/lib/jsonserde.jar;

CREATE EXTERNAL TABLE rss_feed (
  sourceFeed STRING,
  feedTitle STRING,
  feedLink STRING,
  feedDescription STRING,
  feedComments STRING,
  publishedDate STRING,
  feedCategories:ARRAY<STRUCT<categorie:STRING>>>,
  contents:ARRAY<STRUCT<content:STRING>>>
) 
PARTITIONED BY (year STRING, month STRING, day STRING)
ROW FORMAT SERDE 'com.amazon.elasticmapreduce.JsonSerde'
	WITH serdeproperties ( 'paths'='sourceFeed, feedTitle, feedLink, feedDescription, feedComments, publishedDate,feedCategories, contents' )
LOCATION '/user/flume/rss_feed';