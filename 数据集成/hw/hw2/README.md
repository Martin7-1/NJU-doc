# README

## 基础环境搭建

### Intro

基础环境搭建主要涉及到以下几个设施：

1. Spark + Hadoop 搭建
2. Kafka 搭建
3. Flink 搭建
4. Clickhouse 搭建

下面将会对这几个方面的基础设施搭建进行简单的介绍

### Spark

由于 Spark 需要连接校内 Hive 数据仓库来获取数据，因此选择使用 docker compose 在本地环境上部署三实例的分布式 Spark + Hadoop  环境。使用 Spark 3.2.3、Hadoop 3.3.1、Scala 2.12 版本来进行环境搭建。由于 bitnami 所提供的 Spark 镜像原生并不包含 Hadoop，所以需要先构建属于自己的镜像。Dockerfile 如下所示：

```Dockerfile
FROM docker.io/bitnami/spark:3.2.3
USER root

# 省略设置一些 Hadoop 环境变量

WORKDIR /opt

# 省略一些步骤

RUN curl -OL https://mirrors.tuna.tsinghua.edu.cn/apache/hadoop/common/hadoop-3.3.1/hadoop-3.3.1.tar.gz
RUN tar -xzvf hadoop-3.3.1.tar.gz && \
    mv hadoop-3.3.1 hadoop && \
    rm -rf hadoop-3.3.1.tar.gz && \
    mkdir /var/log/hadoop

RUN mkdir -p /root/hdfs/namenode && \ 
    mkdir -p /root/hdfs/datanode 

COPY config/* /tmp/

# 省略设置一些 Hadoop 的配置

COPY start-hadoop.sh /opt/start-hadoop.sh

RUN chmod +x /opt/start-hadoop.sh && \
    chmod +x $HADOOP_HOME/sbin/start-dfs.sh && \
    chmod +x $HADOOP_HOME/sbin/start-yarn.sh 

RUN hdfs namenode -format
RUN sed -i "1 a /etc/init.d/ssh start > /dev/null &" /opt/bitnami/scripts/spark/entrypoint.sh

ENTRYPOINT [ "/opt/bitnami/scripts/spark/entrypoint.sh" ]
CMD [ "/opt/bitnami/scripts/spark/run.sh" ]
```

大概步骤就是通过 curl 下载到对应 Hadoop 的压缩包，然后解压安装并运行。构建集群的 docker compose 文件如下：

```yml
version: "2"

services:
  spark:
    image: data/spark-hadoop:3
    hostname: master
    environment:
      - SPARK_MODE=master
      - SPARK_RPC_AUTHENTICATION_ENABLED=no
      - SPARK_RPC_ENCRYPTION_ENABLED=no
      - SPARK_LOCAL_STORAGE_ENCRYPTION_ENABLED=no
      - SPARK_SSL_ENABLED=no
    volumes:
      - /c/Users/70903/Study/NJUSE/2023_Spring/DataIntegration/share:/opt/share
    ports:
      - "8080:8080"
      - "4040:4040"
      - "8088:8088"
      - "8042:8042"
      - "9870:9870"
      - "19888:19888"
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 4G
  spark-worker-1:
    image: data/spark-hadoop:3
    hostname: worker1
    environment:
      - SPARK_MODE=worker
      - SPARK_MASTER_URL=spark://master:7077
      - SPARK_WORKER_MEMORY=4G
      - SPARK_WORKER_CORES=2
      - SPARK_RPC_AUTHENTICATION_ENABLED=no
      - SPARK_RPC_ENCRYPTION_ENABLED=no
      - SPARK_LOCAL_STORAGE_ENCRYPTION_ENABLED=no
      - SPARK_SSL_ENABLED=no
    volumes:
      - /c/Users/70903/Study/NJUSE/2023_Spring/DataIntegration/share:/opt/share
    ports:
      - "8081:8081"
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 4G
  spark-worker-2:
    image: data/spark-hadoop:3
    hostname: worker2
    environment:
      - SPARK_MODE=worker
      - SPARK_MASTER_URL=spark://master:7077
      - SPARK_WORKER_MEMORY=4G
      - SPARK_WORKER_CORES=2
      - SPARK_RPC_AUTHENTICATION_ENABLED=no
      - SPARK_RPC_ENCRYPTION_ENABLED=no
      - SPARK_LOCAL_STORAGE_ENCRYPTION_ENABLED=no
      - SPARK_SSL_ENABLED=no
    volumes:
      - /c/Users/70903/Study/NJUSE/2023_Spring/DataIntegration/share:/opt/share
    ports:
      - "8082:8081"
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 4G
```

通过 `docker compose up -d` 命令即可构建本地的 Spark 集群，通过 master 节点的 `spark-submit` 即可在集群上提交 Spark 任务

### Kafka

Kafka 采用了分布式的集群部署模式，通过三台主机来部署一个三个 kafka 节点，三台主机的配置分别为 2c4g、两个 2c2g。部署流程如下：

1. 首先在三台主机上分别部署 zookeeper 节点
2. 然后部署三个 kafka 节点
3. 然后在 2c4g 的 master 节点上部署 [kafka-manager](http://124.221.88.241:10010/clusters/kafka-cluster) 作为面板管理控制

使用 [docker swarm](https://docs.docker.com/engine/swarm/) 作为集群管理、容器编排工具，通过编写 docker compose 文件如下：

```yaml
version: "3.7"

networks:
  kafka_network:
    external: true

services:
  kafka-zookeeper-1:
    image: zookeeper:latest
    hostname: kafka-zookeeper-1
    ports:
      - 2183:2181
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=0.0.0.0:2888:3888;2181 server.2=kafka-zookeeper-2:2888:3888;2181 server.3=kafka-zookeeper-3:2888:3888;2181
    volumes:
      - "/etc/timezone:/etc/timezone:ro"
      - "/etc/localtime:/etc/localtime:ro"
      - /data/kafka/zookeeper/data:/data
      - /data/kafka/zookeeper/datalog:/datalog
      - /data/kafka/zookeeper/logs:/logs
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints: # 添加条件约束
          - node.labels.hostname==master
  kafka-zookeeper-2:
    image: zookeeper:latest
    hostname: kafka-zookeeper-2
    ports:
      - 2184:2181
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=kafka-zookeeper-1:2888:3888;2181 server.2=0.0.0.0:2888:3888;2181 server.3=kafka-zookeeper-3:2888:3888;2181
    volumes:
      - "/etc/timezone:/etc/timezone:ro"
      - "/etc/localtime:/etc/localtime:ro"
      - /data/kafka/zookeeper/data:/data
      - /data/kafka/zookeeper/datalog:/datalog
      - /data/kafka/zookeeper/logs:/logs
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints: # 添加条件约束
          - node.labels.hostname==worker-1
  kafka-zookeeper-3:
    image: zookeeper:latest
    hostname: kafka-zookeeper-3
    ports:
      - 2185:2181
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=kafka-zookeeper-1:2888:3888;2181 server.2=kafka-zookeeper-2:2888:3888;2181 server.3=0.0.0.0:2888:3888;2181
    volumes:
      - "/etc/timezone:/etc/timezone:ro"
      - "/etc/localtime:/etc/localtime:ro"
      - /data/kafka/zookeeper/data:/data
      - /data/kafka/zookeeper/datalog:/datalog
      - /data/kafka/zookeeper/logs:/logs
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints: # 添加条件约束
          - node.labels.hostname==worker-2
  kafka-1:
    image: wurstmeister/kafka:latest
    hostname: kafka-1
    ports:
      - 9093:9093
    depends_on:
      - kafka-zookeeper-1
      - kafka-zookeeper-2
      - kafka-zookeeper-3
    environment:
      HOSTNAME_COMMAND: "docker info -f '{{`{{.Swarm.NodeAddr}}`}}'"
      KAFKA_BROKER_ID: 1
      KAFKA_MESSAGE_MAX_BYTES: 5000000
      KAFKA_ZOOKEEPER_CONNECT: kafka-zookeeper-1:2181,kafka-zookeeper-2:2181,kafka-zookeeper-3:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT,SASL_PLAINTEXT:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: INSIDE://:9092,OUTSIDE://124.221.88.241:9093
      KAFKA_LISTENERS: INSIDE://:9092,OUTSIDE://0.0.0.0:9093
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
      KAFKA_LOG_DIRS: /kafka/kafka_log
      KAFKA_LOG_CLEANUP_POLICY: "delete"
      KAFKA_LOG_RETENTION_HOURS: 1
      KAFKA_LOG_RETENTION_BYTES: 1048576
      KAFKA_LOG_SEGMENT_BYTES: 1048576
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 300000
      KAFKA_LOG_CLEANLE_ENABLE: "true"
    volumes:
      - "/etc/timezone:/etc/timezone:ro"
      - "/etc/localtime:/etc/localtime:ro"
      - /var/run/docker.sock:/var/run/docker.sock
      - "/data/kafka/kafka:/kafka"
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints:
          - node.labels.hostname==master
  kafka-2:
    image: wurstmeister/kafka:latest
    hostname: kafka-2
    ports:
      - "9094:9094"
    depends_on:
      - kafka-zookeeper-1
      - kafka-zookeeper-2
      - kafka-zookeeper-3
    environment:
      HOSTNAME_COMMAND: "docker info -f '{{`{{.Swarm.NodeAddr}}`}}'"
      KAFKA_BROKER_ID: 2
      KAFKA_MESSAGE_MAX_BYTES: 5000000
      KAFKA_ZOOKEEPER_CONNECT: kafka-zookeeper-1:2181,kafka-zookeeper-2:2181,kafka-zookeeper-3:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT,SASL_PLAINTEXT:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: INSIDE://:9092,OUTSIDE://124.221.99.108:9094
      KAFKA_LISTENERS: INSIDE://:9092,OUTSIDE://0.0.0.0:9094
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
      KAFKA_LOG_DIRS: /kafka/kafka_log
      KAFKA_LOG_CLEANUP_POLICY: "delete"
      KAFKA_LOG_RETENTION_HOURS: 1
      KAFKA_LOG_RETENTION_BYTES: 1048576
      KAFKA_LOG_SEGMENT_BYTES: 1048576
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 300000
      KAFKA_LOG_CLEANLE_ENABLE: "true"
    volumes:
      - "/etc/timezone:/etc/timezone:ro"
      - "/etc/localtime:/etc/localtime:ro"
      - /var/run/docker.sock:/var/run/docker.sock
      - "/data/kafka/kafka:/kafka"
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 2G
      placement:
        constraints:
          - node.labels.hostname==worker-1
  kafka-3:
    image: wurstmeister/kafka:latest
    hostname: kafka-3
    ports:
      - "9095:9095"
    depends_on:
      - kafka-zookeeper-1
      - kafka-zookeeper-2
      - kafka-zookeeper-3
    environment:
      HOSTNAME_COMMAND: "docker info -f '{{`{{.Swarm.NodeAddr}}`}}'"
      KAFKA_BROKER_ID: 3
      KAFKA_MESSAGE_MAX_BYTES: 5000000
      KAFKA_ZOOKEEPER_CONNECT: kafka-zookeeper-1:2181,kafka-zookeeper-2:2181,kafka-zookeeper-3:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT,SASL_PLAINTEXT:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: INSIDE://:9092,OUTSIDE://150.158.53.187:9095
      KAFKA_LISTENERS: INSIDE://:9092,OUTSIDE://0.0.0.0:9095
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
      KAFKA_LOG_DIRS: /kafka/kafka_log
      KAFKA_LOG_CLEANUP_POLICY: "delete"
      KAFKA_LOG_RETENTION_HOURS: 1
      KAFKA_LOG_RETENTION_BYTES: 1048576
      KAFKA_LOG_SEGMENT_BYTES: 1048576
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 300000
      KAFKA_LOG_CLEANLE_ENABLE: "true"
    volumes:
      - "/etc/timezone:/etc/timezone:ro"
      - "/etc/localtime:/etc/localtime:ro"
      - /var/run/docker.sock:/var/run/docker.sock
      - "/data/kafka/kafka:/kafka"
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 2G
      placement:
        constraints:
          - node.labels.hostname==worker-2

  kafka-manager:
    image: sheepkiller/kafka-manager
    hostname: kafka-manager
    ports:
      - "10010:9000"
    depends_on:
      - kafka-1
      - kafka-2
      - kafka-3
      - kafka-zookeeper-1
      - kafka-zookeeper-2
      - kafka-zookeeper-3
    environment:
      ZK_HOSTS: kafka-zookeeper-1:2181,kafka-zookeeper-2:2181,kafka-zookeeper-3:2181
      TZ: CST-8
    networks:
      - kafka_network
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints:
          - node.labels.hostname==master
```

该 docker compose 文件中主要做了如下的几件事：

1. 部署三个 zookeeper 节点
2. 部署三个 kafka 节点，同时进行相关配置（比如声明 zookeeper 节点的 ip 以便进行节点的注册、声明监听的端口以便可以从外部进行访问、声明要持久化的数据文件及其挂在的 volume 位置、声明 broker id 等）
3. 部署 kafka-manager 进行面板的管理和监控

### Flink

Flink 同样采用了集群部署等模式，部署了一个 taskmanager 和一个 jobmanager 来构建出 flink 集群，可以通过 [Flink Web UI](http://124.221.88.241:10081/#/overview) 查看 Flink 集群的信息。使用 docker compose 文件进行容器编排如下：

```yaml
version: "3.7"

networks:
  flink_network:
    external: true

services:
  jobmanager:
    image: apache/flink:java8
    hostname: jobmanager
    ports:
      - 10081:8081
    command: jobmanager
    networks:
      - flink_network
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints: # 添加条件约束
          - node.labels.hostname==master

  taskmanager:
    image: apache/flink:java8
    hostname: taskmanager
    depends_on:
      - jobmanager
    command: taskmanager
    networks:
      - flink_network
    environment:
      - |
        FLINK_PROPERTIES=
        jobmanager.rpc.address: jobmanager
        taskmanager.numberOfTaskSlots: 2
    deploy:
      mode: replicated
      replicas: 1
      placement:
        constraints: # 添加条件约束
          - node.labels.hostname==master
```

### Clickhouse

Clickhouse 部署了两个实例的集群，通过 [tabix](http://124.221.88.241:8080/#!/sql) 面板来可以进行简单的查看和 SQL 运行。对于 clickhouse 首先需要先准备好 `config.xml`、`metrika.xml` 和 `users.xml`。比较重要的是 `metrika.xml`，其中配置了实例分片，如下所示：

```xml
<yandex>
    <!-- 集群配置 -->
    <clickhouse_remote_servers>
        <my_cluster>
            <!-- 数据分片1  -->
            <shard>
                <internal_replication>false</internal_replication>
                <replica>
                    <host>ck-2</host>
                    <port>9000</port>
                    <user>default</user>
                    <password>test</password>
                </replica>
                <replica>
                    <host>ck-3</host>
                    <port>9000</port>
                    <user>default</user>
                    <password>test</password>
                </replica>
            </shard>
            <!-- 数据分片2  -->
            <shard>
                <internal_replication>false</internal_replication>
                <replica>
                    <host>ck-3</host>
                    <port>9000</port>
                    <user>default</user>
                    <password>test</password>
                </replica>
                <replica>
                    <host>ck-2</host>
                    <port>9000</port>
                    <user>default</user>
                    <password>test</password>
                </replica>
            </shard>
        </my_cluster>
    </clickhouse_remote_servers>
    <!-- ZK  -->
    <zookeeper-servers>
        <node index="1">
            <host>ck-1</host>
            <port>2181</port>
        </node>
    </zookeeper-servers>
    <networks>
        <ip>::/0</ip>
    </networks>
    <!-- 数据压缩算法  -->
    <clickhouse_compression>
        <case>
            <min_part_size>10000000000</min_part_size>
            <min_part_size_ratio>0.01</min_part_size_ratio>
            <method>lz4</method>
        </case>
    </clickhouse_compression>
</yandex>
```

通过 docker compose 来编排容器，docker compose 如下所示：

```yaml
version: "3"

services:
  zookeeper:
    image: zookeeper
    ports:
      - "2181:2181"
      - "2182:2182"
    volumes:
      - "/data/clickhouse/zookeeper/data:/data"
      - "/data/clickhouse/zookeeper/log:/datalog"
    deploy:
      placement:
        constraints:
          - node.labels.hostname==master
  tabix:
    image: spoonest/clickhouse-tabix-web-client
    ports:
      - "8080:80"
    deploy:
      placement:
        constraints:
          - node.labels.hostname==master
  clickhouse01:
    image: yandex/clickhouse-server
    user: root
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 2G
      placement:
        constraints:
          - node.labels.hostname==worker-1
    ports:
      - "10000:9000"
      - "8123:8123"
    volumes:
      - /data/clickhouse/clickhouse-server/etc:/etc/clickhouse-server
      - /data/clickhouse/clickhouse-server/data:/var/lib/clickhouse
      - /data/clickhouse/clickhouse-server/log:/var/log/clickhouse-server/
    ulimits:
      nofile:
        soft: 262144
        hard: 262144
    depends_on:
      - "zookeeper"
  clickhouse02:
    image: yandex/clickhouse-server
    user: root
    deploy:
      mode: replicated
      replicas: 1
      resources:
        limits:
          memory: 2G
      placement:
        constraints:
          - node.labels.hostname==worker-2
    ports:
      - "10001:9000"
      - "8124:8123"
    volumes:
      - /data/clickhouse/clickhouse-server/etc:/etc/clickhouse-server
      - /data/clickhouse/clickhouse-server/data:/var/lib/clickhouse
      - /data/clickhouse/clickhouse-server/log:/var/log/clickhouse-server/
    ulimits:
      nofile:
        soft: 262144
        hard: 262144
    depends_on:
      - "zookeeper"
```

主要就是部署 zookeeper 提供服务发现和集群管理功能，然后运行两个 clickhouse 容器，挂载对应目录、暴露相应端口。

至此，Spark-Hadoop、Kafka、Flink、Clickhouse 集群搭建完毕
