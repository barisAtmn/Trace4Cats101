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