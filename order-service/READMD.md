# order-service(MSA)
## maria-db
### h2-console
- Driver Class : `org.mariadb.jdbc.Driver`
- JDBC URL : `jdbc:mysql://localhost:3306/mydb`
- User Name : `root`

## Kafka
### Kafka 실행
> mac에서는 windows 생략(mac=.bat -> .sh)
1. zookeeper 실행
````
cd C:\development\kafka\kafka_2.13-3.2.0 \
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
````

2. kafka server 실행
````
cd C:\development\kafka\kafka_2.13-3.2.0 \
.\bin\windows\kafka-server-start.bat .\config\server.properties
````