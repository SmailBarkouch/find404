import java.io._
import java.nio.file.Files
import scala.collection.mutable.ArrayBuffer
import sys.process._
import java.nio.charset.StandardCharsets
import scala.collection.JavaConverters._

object Find404 {
    def main(args: Array[String]): Unit = {
        getFileNames
            .foreach {
                x =>
                println(new String(readFile(new File(x.getAbsolutePath()))))
            }
    }
    
    def getFileNames: ArrayBuffer[File] = {
        var fileSet = ArrayBuffer[File]()
        ("git ls-files --full-name" !!).split("\\r?\\n").foreach {
            x =>
            fileSet += new File(x)
        }
        println(fileSet.length)
        fileSet
    }

    def readFile(fileName: File): Array[Byte] = {
        val fullText = Files.readAllBytes(fileName.toPath())
        fullText
        }
}


