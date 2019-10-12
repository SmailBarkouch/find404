package find404

import java.io._
import java.net.{HttpURLConnection, URI}
import scala.util.Try
import scala.io._
import scala.sys.process._
import scala.util.control.NonFatal
import scala.collection.mutable.ArrayBuffer

object App {
  def main(args: Array[String]): Unit = {
    val fullDataSet =
      getFileNames
      .map(new File(_))
      .map(x => DataSet(x, (getURLS(readFile(x)), getURLS(readFile(x)).map(getStatusCode)))) // search for more efficent manner

    Try {
      args(0) match {
        case "verbose" => printVerbose(fullDataSet)
      }
    }

  }

  def getFileNames: Array[String] = "git ls-files --full-name".!!.split("\n")

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
    http.asInstanceOf[HttpURLConnection].getResponseCode()
  }

  def printVerbose(fileChain: Array[DataSet]): Unit = {
    fileChain.foreach {
      x =>
        println("File Found: " + x.file)
        if(x.urlSet._1.size != 0) {
          for (z <- 0 until x.urlSet._2.size) {
            if (x.urlSet._2(z) == 401) println("URL Failure: " + x.urlSet)
            else println("URL Success: " + x.urlSet)
          }
        }
    }
  }

}

case class DataSet(file: File, urlSet: (List[URI], List[Integer]))