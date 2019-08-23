import java.io._
import scala.io.Source._
import scala.io.StdIn._
import scala.collection.mutable.ArrayBuffer

var fileSet = ArrayBuffer[File]()
var ignoreSet = ArrayBuffer[String]()
val directorySet = new File(args(0))
if(directorySet.exists() == false) {
    println("This directory does not exist")
} else {
    fileSet = exploreDirect(directorySet)
    ignoreSet = readFile(searchFor(fileSet, ".gitignore"))
}


def exploreDirect(directory: File): ArrayBuffer[File] = {
    var subFileSet = ArrayBuffer[File]()
    directory.listFiles().foreach {
        x: File => 
        if(x.isFile()) subFileSet += x
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

def readFile(textFile: File): ArrayBuffer[String] = {
    val textSet = ArrayBuffer[String]()
    val readableFile = new BufferedReader(new FileReader(textFile))
    for(i <- 0 until readableFile.lines().count().toInt) {
        textSet += readableFile.readLine()
    }
    textSet
}

def filterFiles(fileSet: ArrayBuffer[File], ignoreSet: ArrayBuffer[String]): ArrayBuffer[File] = {
    val filteredSet = fileSet.filter {
        x =>
        var option = true
        for(i <- 0 until ignoreSet.length) {
            if (x.getAbsolutePath.indexOf(ignoreSet(i)) != -1) {
                 option = false
            }
        }
        option
    }
    filteredSet
}


