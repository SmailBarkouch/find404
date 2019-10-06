package find404

import java.io._
import scala.io._
import java.nio.file.Files
import scala.collection.mutable.ArrayBuffer
import sys.process._
import java.nio.charset.StandardCharsets
import scala.collection.JavaConverters._
import java.net.URI
import scala.util.matching.Regex
import java.net.HttpURLConnection

object App {
  def main(args: Array[String]): Unit = {
    val a = getFileNames
      .map(new File(_))
      .map(readFile(_))
      .map(getURLS(_))
      .flatten
      .map(x =>(x, getStatusCode(x))) // mention
      .foreach(println(_))
  }

  def getFileNames: Array[String] = ("git ls-files --full-name".!!).split("\n")

  def readFile(fileName: File): String = {
    val source = Source.fromFile(fileName)
    val lines = try source.mkString
    finally source.close() //if file is too large, could run out of memory
    lines
  }

  def getURLS(text: String): List[URI] = {
    "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]".r.findAllIn(text).map(new URI(_)).toList
  }

  def getStatusCode(url: URI): Integer = {
    val http = url.toURL().openConnection()
    http.asInstanceOf[HttpURLConnection].getResponseCode()
  }
}
