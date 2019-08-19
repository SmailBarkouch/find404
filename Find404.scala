import java.io._
import scala.io.Source._
import scala.io.StdIn._

object Find404 {
    
    def main(args: Array[String]): Unit = {
        println("Provide a directory")
        val directory = new File(readLine())

        val directorySet = directory.listFiles()
        val fileSet = directorySet.filter(_.getClass().getName().indexOfSlice(".") == -1)

        fileSet.foreach(println(_))
    }
}