# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# If this file is placed at FLUME_CONF_DIR/flume-env.sh, it will be sourced
# during Flume startup.

# Enviroment variables can be set here.

export JAVA_HOME={{java_home}}

# Give Flume more memory and pre-allocate, enable remote monitoring via JMX
# export JAVA_OPTS="-Xms100m -Xmx2000m -Dcom.sun.management.jmxremote"
export FLUME_CLASSPATH=$FLUME_CLASSPATH:/usr/lib/flume/lib/custom/*

# Note that the Flume conf directory is always included in the classpath.
# Add flume sink to classpath
if [ -e "/usr/lib/flume/lib/ambari-metrics-flume-sink.jar" ]; then
  export FLUME_CLASSPATH=$FLUME_CLASSPATH:/usr/lib/flume/lib/ambari-metrics-flume-sink.jar
fi

export HIVE_HOME={{flume_hive_home}}
export HCAT_HOME={{flume_hcat_home}}