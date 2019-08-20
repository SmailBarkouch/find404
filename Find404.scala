import java.io._
import scala.io.Source._
import scala.io.StdIn._
import scala.collection.mutable.ArrayBuffer

val fileSet = ArrayBuffer[File]()

try {
    val directorySet = new File(args(0))
    exploreDirect(directorySet, fileSet)
} catch {
    case _: NullPointerException => println("Not a valid directory")
}

def exploreDirect(directory: File, fileSet: ArrayBuffer[File]): Unit = {
    directory.listFiles().foreach {
        System.out.println(directory.getAbsolutePath())
        x: File => if(x.isFile) fileSet += x else exploreDirect(x, fileSet)
    }
}
