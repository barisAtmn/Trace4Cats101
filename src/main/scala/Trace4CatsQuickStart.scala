import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.janstenpickle.trace4cats.Span
import io.janstenpickle.trace4cats.avro.AvroSpanCompleter
import io.janstenpickle.trace4cats.inject.{EntryPoint, Trace}
import io.janstenpickle.trace4cats.kernel.SpanSampler
import io.janstenpickle.trace4cats.model.{SpanKind, SpanStatus, TraceProcess}
import scala.concurrent.duration._

object Trace4CatsQuickStart extends IOApp {
  def entryPoint[F[_]: Concurrent: ContextShift: Timer: Logger](
                                                                 blocker: Blocker,
                                                                 process: TraceProcess
                                                               ): Resource[F, EntryPoint[F]] =
    AvroSpanCompleter.udp[F](blocker, process, batchTimeout = 50.millis).map { completer =>
      EntryPoint[F](SpanSampler.always[F], completer)
    }

  def runF[F[_]: Sync: Trace]: F[Unit] =
    for {
      _ <- Trace[F].span("span1")(Sync[F].delay(println("trace this operation")))
      _ <- Trace[F].span("span2", SpanKind.Client)(
        Trace[F].putAll("attribute1" -> "test", "attribute2" -> 200).flatMap { _ =>
          Trace[F].setStatus(SpanStatus.Ok)
        }
      )
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
  {
    (for {
      blocker <- Blocker[IO]
      implicit0(logger: Logger[IO]) <- Resource.liftF(Slf4jLogger.create[IO])
      ep <- entryPoint[IO](blocker, TraceProcess("trace4cats"))
    } yield ep)
      .use { ep =>
        ep.root("this is the root span").use { span =>
          runF[Kleisli[IO, Span[IO], *]].run(span)
        }
      }
      .as(ExitCode.Success)
  }

}