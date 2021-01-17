import cats.Monad
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import io.janstenpickle.trace4cats.Span
import io.janstenpickle.trace4cats.avro.AvroSpanCompleter
import io.janstenpickle.trace4cats.kernel.SpanSampler
import io.janstenpickle.trace4cats.model.{SpanKind, SpanStatus, TraceProcess}

import scala.concurrent.duration._
import cats.syntax.flatMap._

/** This example shows how to send traces to the Avro Agent.
 *
 * Note how spans are surfaced as instances of `cats.effect.Resource` so may be flatMapped, however in this
 * example the `use` method is called explicitly
 */
object SimpleExample extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Monad[IO].whileM_(IO(true)) {
      (for {
        blocker <- Blocker[IO]
        completer <- AvroSpanCompleter.udp[IO](blocker, TraceProcess("test"), batchTimeout = 50.millis)
      } yield completer)
        .use { completer =>

          // Spans are surfaced as `cats.effect.Resource`s which form a timed bracket around an executed effect
          Span.root[IO]("root", SpanKind.Client, SpanSampler.always, completer).use { root =>
            // do some stuff
            root
              .putAll("root-attribute" -> "I am Root", "app-ver" -> "0.0.1")
          }

        }
    } >> IO(ExitCode.Success)


}