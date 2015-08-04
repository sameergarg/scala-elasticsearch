package client

import java.nio.file.Files

import com.sksamuel.elastic4s.ElasticClient
import com.typesafe.config.ConfigFactory
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.NodeBuilder._
import play.api.Configuration

trait ElasticSearchClientFactory {

  val client: ElasticClient

  val settings: ImmutableSettings
}


trait LocalElasticSearchClientFactory extends ElasticSearchClientFactory {

  private val config = new Configuration(ConfigFactory.load())

  val host = config.getString("elasticSearch.host").getOrElse("http://192.168.0.11/")

  val port = config.getInt("elasticSearch.port").getOrElse(9300)

  val clusterName: String =  config.getString("elasticSearch.clusterName").getOrElse("My amazing ES cluster")

  val settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build()

  val client = ElasticClient.remote(settings, host, port)

}

trait InMemoryElasticSearchClientFactory extends ElasticSearchClientFactory {

  private val clusterName = "elasticsearch"
  private val dataDir = Files.createTempDirectory("elasticsearch_data_").toFile
  private val settings = ImmutableSettings.settingsBuilder
    .put("path.data", dataDir.toString)
    .put("cluster.name", clusterName)
    .build

  private lazy val node = nodeBuilder().local(true).settings(settings).build

  val client = ElasticClient.fromNode(node)

}

