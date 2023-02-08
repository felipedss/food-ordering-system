
# food-ordering-system

## Running apache kafka using docker

```bash
docker-compose -f common.yml -f zookeeper.yml up
docker-compose -f common.yml -f kafka_cluster.yml up
docker-compose -f common.yml -f init_kafka.yml up
``` 

### Testing healthy of zookeper

```bash
echo ruok | nc localhost 2181
```