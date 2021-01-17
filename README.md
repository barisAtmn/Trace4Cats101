# Trace4Cats101

- Simple applications by using Trace4Cats library.


For simple Example; it needs avro collector
```$xslt
echo "log-spans: true" > /tmp/collector.yaml
docker run -p7777:7777 -p7777:7777/udp -it \
  -v /tmp/collector.yaml:/tmp/collector.yaml \
  janstenpickle/trace4cats-collector-lite:0.7.0 \
  --config-file=/tmp/collector.yaml
```