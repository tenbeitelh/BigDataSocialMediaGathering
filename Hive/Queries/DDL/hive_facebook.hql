ADD JAR hdfs://master.hs.osnabrueck.de:8020/user/hive/lib/jsonserde.jar;

CREATE EXTERNAL TABLE facebook (
  sourcePage STRING,
  id STRING,
  fromCategory STRING,
  createdTime STRING,
  desription STRING,
  message STRING,
  messageTags:ARRAY<STRUCT<tags:STRING>>>,
  likes INT,
   comments:ARRAY<STRUCT<
				id STRING,
				from STRING,
				createdTime STRING,
				message STRING,
				likeCount INT
				>>
  
) 
PARTITIONED BY (year STRING, month STRING, day STRING)
ROW FORMAT SERDE 'com.amazon.elasticmapreduce.JsonSerde'
	WITH serdeproperties ( 'paths'='sourcePage, id, fromCategory, createdTime, description, message, messageTags, likes, comments' )
LOCATION '/user/facebook';