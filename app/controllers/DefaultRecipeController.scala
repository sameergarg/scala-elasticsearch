package controllers

import javax.xml.ws.WebServiceClient

import com.sksamuel.elastic4s._
import client.{RecipeHttpClient, LocalElasticSearchClientFactory}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.source.Indexable
import fileutils.Unzip
import javax.inject.{ Inject, Singleton }
import models._
import play.Play
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import com.sksamuel.elastic4s._
import scala.io.Source
import com.sksamuel.elastic4s.Executable

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
/**
 *
 */






trait RecipeController extends Controller with Unzip {

  def httpClient: RecipeHttpClient

  implicit object RecipeIndexable extends Indexable[Recipe] {
    override def json(recipe: Recipe): String = Json.toJson(recipe).toString()
  }


  def loadRecipes = Action { implicit request =>
    /*client.execute {
      index into ("stores", "store") source store
    }*/
    val map1: Iterator[Recipe] = Source.
      fromFile(storeUnzippedFile())
      .getLines()
      .map(parseRow)
      .filter(_.isDefined)
      .map(_.get)

    map1.foreach(storeRecipe)

    Ok
  }

  def search(term: String) = Action.async { implicit request =>
    httpClient.search(term).map( response =>
      Ok(response.body)
    )
  }

  private def storeRecipe(recipe: Recipe) =
    httpClient.save(recipe).onComplete {
      case Success(res) => Logger.info(s"${recipe.name} saved successfully")
      case Failure(ex) => Logger.info(s"Unable to save ${recipe.name} due to an exception", ex)
    }



  private[controllers] def parseRow(row: String): Option[Recipe] = {
    Some(Json.parse(row).as[Recipe])
  }

  private def storeUnzippedFile(): String = {
    val result: Boolean = unZip(Play.application().getFile("conf/recipeitems-latest.json.zip").getPath, tempDir)
    assert(result == true)
    Logger.info(s"storing file at ${tempDir}recipeitems-latest.json")
    s"${tempDir}recipeitems-latest.json"
  }



}

class DefaultRecipeController @Inject() (ws: WSClient) extends RecipeController {

  def httpClient = new RecipeHttpClient(ws)

}
