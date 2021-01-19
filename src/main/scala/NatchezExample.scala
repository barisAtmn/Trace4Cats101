import cats.data.Kleisli
import cats.effect.{ ExitCode, IO, IOApp, Resource, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._

import natchez.{EntryPoint, Trace, Span => NatchezSpan}

object NatchezExample extends IOApp {


  case class User(name:String)

  case class UserProfile(name:String)

  case class UserNetwork(name:String)

  case class UserData(userProfile: UserProfile, userNetwork: UserNetwork)

  case class UserSearchResult(user: User, userData: UserData)


  def getUser[F[_]: Sync: Trace] (userId: Int): F[User] = Trace[F].span("get user from database"){
    Sync[F].delay(User(""))
  }


  def getUserNetwork[F[_]: Sync: Trace](user: User): F[UserNetwork] = Trace[F].span("get user network"){
    Sync[F].delay(UserNetwork(""))
  }

  def getUserProfile[F[_]: Sync: Trace](user: User): F[UserProfile] = Trace[F].span("get user profile"){
    Sync[F].delay(UserProfile(""))
  }


  def getUserData[F[_]: Sync: Trace](user: User): F[UserData] = Trace[F].span("get user data"){
    for {
      userNetwork <- getUserNetwork(user)
      userProfile <- getUserProfile(user)
    } yield UserData(userProfile, userNetwork)
  }


  def program[F[_]: Sync: Trace](userId: Int): F[UserSearchResult] = Trace[F].span("program span"){
    for {
      user <- getUser(userId)
      userData <- getUserData(user)
    } yield UserSearchResult(user, userData)
  }

  def entryPoint[F[_]: Sync]: Resource[F, EntryPoint[F]] = {
    import natchez.jaeger.Jaeger
    import io.jaegertracing.Configuration.SamplerConfiguration
    import io.jaegertracing.Configuration.ReporterConfiguration
    Jaeger.entryPoint[F]("natchez-example") { c =>
      Sync[F].delay {
        c.withSampler(SamplerConfiguration.fromEnv)
          .withReporter(ReporterConfiguration.fromEnv)
          .getTracer
      }
    }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    entryPoint[IO].use{
      ep =>
        ep.root("this is the root span").use { span =>
          program[Kleisli[IO, NatchezSpan[IO], *]](1).run(span)
        }.as(ExitCode.Success)
    }
  }
}