/*
 * Copyright 2016 Christopher Hunt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package huntfamily.id.au.no.idea

import akka.actor.ActorSystem
import akka.util.ByteString
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import scala.concurrent.Future
import akka.http.scaladsl.model.headers.Host
import akka.stream.scaladsl._

object WebServer {
  def main(args: Array[String]) {

    implicit val system           = ActorSystem("my-system")
    implicit val materializer     = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete(
            HttpEntity(ContentTypes.`text/html(UTF-8)`,
                       "<h1>Say hello to akka-http</h1>")
          )
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "127.0.0.1", 8080)

    // Now send a request
    val uri  = new java.net.URI("http://127.0.0.1:8080/hello")
    val host = uri.getHost
    val port = uri.getPort
    val request =
      HttpRequest(uri = Uri(uri.getPath))
    val connection = Http(system).outgoingConnection(host, port)
    val responseFuture: Future[HttpResponse] =
      Source.single(request).via(connection).runWith(Sink.head)

    responseFuture.foreach { response =>
      println(
        "Got response, body: " + response.entity.dataBytes
          .runFold(ByteString(""))(_ ++ _)
      )
    }

    println(
      s"Server online at http://localhost:8080/\nPress RETURN to stop..."
    )
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
