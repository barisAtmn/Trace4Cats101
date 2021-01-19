# Trace4Cats101

- Simple applications by using Trace4Cats library.

- type Trace[A] = Kleisli[IO, Span, A]
  We basically create an alias for anything that takes a Span and returns an IO[A].
  That alias is called a Trace[A].



For simple Example; it needs avro collector
```$xslt
echo "log-spans: true" > /tmp/collector.yaml
docker run -p7777:7777 -p7777:7777/udp -it \
  -v /tmp/collector.yaml:/tmp/collector.yaml \
  janstenpickle/trace4cats-collector-lite:0.7.0 \
  --config-file=/tmp/collector.yaml
```


--> Define implicits in for-comprehensions or matches
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
```$xslt
case class ImplicitTest(id: String)
ss
for {
  x <- Option(42)
  implicit0(it: ImplicitTest) <- Option(ImplicitTest("eggs"))
  _ <- Option("dummy")
  _ = "dummy"
  _ = assert(implicitly[ImplicitTest] eq it)
} yield "ok"
```


--> Jaeger 

```$xslt
docker run -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 14250:14250 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.21
``` 