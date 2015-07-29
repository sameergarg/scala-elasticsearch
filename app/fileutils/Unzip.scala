package fileutils

import java.util.zip.ZipFile
import java.io.FileInputStream
import java.io.FileOutputStream
import scala.collection.JavaConversions._
import java.util.zip.ZipEntry
import java.io.InputStream
import java.io.OutputStream
import java.io.File

/**
 * Created by gargsame on 17/06/2015.
 */
trait Unzip {

  val BUFSIZE = 4096
  val buffer = new Array[Byte](BUFSIZE)

  val tempDir = System.getProperties.getProperty("java.io.tmpdir")

  def unZip(source: String, targetFolder: String) = {
    val zipFile = new ZipFile(source)

    unzipAllFile(zipFile.entries.toList, getZipEntryInputStream(zipFile)_, new File(targetFolder))
  }

  private def getZipEntryInputStream(zipFile: ZipFile)(entry: ZipEntry) = zipFile.getInputStream(entry)

  private def unzipAllFile(entryList: List[ZipEntry], inputGetter: (ZipEntry) => InputStream, targetFolder: File): Boolean = {

    entryList match {
      case entry :: entries =>

        if (entry.isDirectory)
          new File(targetFolder, entry.getName).mkdirs
        else
          saveFile(inputGetter(entry), new FileOutputStream(new File(targetFolder, entry.getName)))

        unzipAllFile(entries, inputGetter, targetFolder)
      case _ =>
        true
    }

  }

  private  def saveFile(fis: InputStream, fos: OutputStream) = {
    writeToFile(bufferReader(fis)_, fos)
    fis.close
    fos.close
  }

  private def bufferReader(fis: InputStream)(buffer: Array[Byte]) = (fis.read(buffer), buffer)

  private def writeToFile(reader: (Array[Byte]) => Tuple2[Int, Array[Byte]], fos: OutputStream): Boolean = {
    val (length, data) = reader(buffer)
    if (length >= 0) {
      fos.write(data, 0, length)
      writeToFile(reader, fos)
    } else
      true
  }
}

object Unzip extends Unzip
