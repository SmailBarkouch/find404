package find404

import java.io._
import java.net.{HttpURLConnection, URI}

import scala.io._
import scala.sys.process._
import scala.util.control.NonFatal

case class DataSet(file: File, urlSet: (List[URI], List[Integer]))

object App {
  def main(args: Array[String]): Unit = {

    if (checkInternet) {
      val invalidURLS = get404
      evaluateArgs(args.lift(0), invalidURLS)
    }

  }

  def evaluateArgs(arg0: Option[String], dataSet: Array[DataSet]): Unit = {

    arg0 match {
      case Some("verbose") => printVerbose(dataSet)
      case None =>
        println(
          "Use the argument \"verbose\" to have commandline output. This is the only current command."
        )
    }
  }

  def checkInternet: Boolean = {
    val internet = try { // Move to function
      val reliableServer = new URI("http://google.com").toURL.openConnection()
      val internetCheck =
        reliableServer.asInstanceOf[HttpURLConnection].getResponseCode()
      true
    } catch {
      case noConnection: java.net.UnknownHostException => {
        println("Your internet connection is off, please connect to a network")
        false
      }
    }
    internet
  }

  def getFileNames: Array[String] = "git ls-files --full-name".!!.split("\n")

  def get404: Array[DataSet] = {
    val fullDataSet =
      getFileNames
        .map(new File(_))
        .filter(!_.isDirectory)
        .map(
          x =>
            DataSet(
              x,
              (
                getURLS(readFile(x)),
                getURLS(readFile(x)).map(getStatusCode)
              )
            )
        )
    fullDataSet
  }

  def readFile(fileName: File): String = {
    val source = Source.fromFile(fileName)
    val lines = {
      try {
        source.mkString
      } catch {
        case NonFatal(_) => ""
      } finally {
        source.close()
      }
    }

    //if file is too large, could run out of memory
    lines
  }

  def getURLS(text: String): List[URI] = {
    """(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]""".r
      .findAllIn(text)
      .map(new URI(_))
      .toList
  }

  def getStatusCode(url: URI): Integer = {
    val http = url.toURL.openConnection()
    try {
      http.asInstanceOf[HttpURLConnection].getResponseCode()
    } catch {
      case NonFatal(_) => 401
    }
  }

  def printVerbose(fileChain: Array[DataSet]): Unit = { // Needs to be simplfied, use hashmap
    fileChain.foreach { x =>
      println("File Found: " + x.file)
      if (x.urlSet._1.size != 0) {
        for (z <- 0 until x.urlSet._2.size) {
          if (x.urlSet._2(z) == 401) println("URL Failure: " + x.urlSet)
          else println("URL Success: " + x.urlSet)
        }
      }
    }
  }
}
