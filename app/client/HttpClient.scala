package client

import com.typesafe.config.ConfigFactory
import models._
import org.elasticsearch.common.inject.Inject
import play.api.{Logger, Configuration}
import play.api.libs.json.{Json, JsValue}
import play.api.libs.ws.{WSResponse, WSClient}

import scala.concurrent.Future

/**
 *
 */
trait WebServiceClient {

  type T

  def wsClient: WSClient

  def host: String

  def port: Int

  def path: String

  def parseToJson(resource: T): JsValue

  def save(resource: T): Future[WSResponse] = {
    val url: String = s"http://$host:$port/$path"
    Logger.debug(s"connecting to http client at $url")
    wsClient.url(url)
      .withHeaders("Content-Type" -> "application/json")
      .post(parseToJson(resource))
  }

  def search(term: String):Future[WSResponse] = {
    wsClient.url(s"http://$host:$port/$path/_search?pretty=true")
      .withHeaders("Content-Type" -> "application/json")
      .post(
        s"""
          |{
          | "query": {
          |   "query_string": {
          |     "query": "$term"
          |   }
          | }
          |}
        """.stripMargin)
  }
}

trait ElasticSearchWebServiceClient extends WebServiceClient {

  val config = new Configuration(ConfigFactory.load())

  def host = config.getString("elasticSearch.host").getOrElse("192.168.0.11")

  def port = config.getInt("elasticSearch.port").getOrElse(9200)

}

class RecipeHttpClient @Inject()(ws: WSClient) extends ElasticSearchWebServiceClient {

  type T = Recipe

  def wsClient = ws

  def path = "recipes/recipe"

  def parseToJson(resource: T): JsValue = {
    val json: JsValue = Json.toJson(resource)
    Logger.debug(s"storing json $json")
    json
  }


}