import java.io._
import scala.io.Source._
import scala.io.StdIn._
import scala.collection.mutable.ArrayBuffer
object Find404 {
    def main(args: Array[String]): Unit = {
        val ignoreSet = ArrayBuffer[String]()
        if(args.length < 1) {
            println("To use this commandline application, enter a directory following")
            println("the command to start the application.")
            println("This application takes in a directory and checks for deadlinks")
            println("within files, excluding the files found in .gitignore.")
        } else if(new File(args(0)).exists() == false) {
            println(s"The entered directory of ${args(0)} does not exist!")
        } else {
            val directorySet = new File(args(0))
            val fileSet = exploreDirect(directorySet)
        }
    }
    
    def exploreDirect(directory: File): ArrayBuffer[File] = {
        var subFileSet = ArrayBuffer[File]()
        directory.listFiles().foreach {
            x: File => 
            if(x.isFile()) {
                println(x.getAbsolutePath())
                subFileSet += x
            }
            else subFileSet ++= exploreDirect(x)
        }
        subFileSet
    }
    
    def searchFor(fileSet: ArrayBuffer[File], name: String): File = {
        var foundFile = new File("/")
        fileSet.foreach {
            x =>
            if (x.getAbsolutePath.indexOf(name) != -1) {
                foundFile = x
            }
        }
        foundFile
    }
}


