ADD JAR <path-to-hive-serdes-jar>;

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
PARTITIONED BY (datehour INT)
ROW FORMAT SERDE 'com.amazon.elasticmapreduce.JsonSerde'
	WITH serdeproperties ( 'paths'='sourcePage, id, fromCategory, createdTime, description, message, messageTags, likes, comments' )
LOCATION '/user/facebook';