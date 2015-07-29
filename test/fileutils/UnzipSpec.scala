package fileutils

import org.scalatest.{Matchers, FlatSpec}
import play.api.Play
import play.api.Play.current

import scala.io.Source

/**
 *
 */
class UnzipSpec extends FlatSpec with Matchers {

  "Unzip" should "store unzipped file at temporary location" in new Unzip {
    private val path: String = getClass.getClassLoader.getResource("recipeitems-latest.json.zip").getPath //"conf/recipeitems-latest.json.gz" //).getPath

    println(s"Unzipping file at $path to $tempDir")

    unZip(path, tempDir)
  }

}
