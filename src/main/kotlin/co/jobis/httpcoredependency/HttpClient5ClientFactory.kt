package co.jobis.httpcoredependency

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.Timeout
import java.time.Duration

class HttpClient5ClientFactory {

  companion object {
    fun createHttpClient5Client(
      defaultMaxPerRoute: Int = 100,
      connectionMaxTotal: Int = 100,
      connectionTimeout: Duration = Duration.ofSeconds(1),
      responseTimeout: Duration = Duration.ofSeconds(5)
    ): HttpClient {
      val connectionManager = connectionManager(defaultMaxPerRoute, connectionMaxTotal, connectionTimeout)
      val requestConfig = RequestConfig.custom()
        .setResponseTimeout(Timeout.of(responseTimeout))
        .build()

      val httpClientBuilder = HttpClientBuilder.create()
        .useSystemProperties()
        .setConnectionManager(connectionManager)
        .setDefaultRequestConfig(requestConfig)

      return httpClientBuilder.build()
    }

    private fun connectionManager(defaultMaxPerRoute: Int, connectionMaxTotal: Int, connectionTimeout: Duration): PoolingHttpClientConnectionManager {
      val connectionManager = PoolingHttpClientConnectionManager()
        .apply {
          this.defaultMaxPerRoute = defaultMaxPerRoute
          this.maxTotal = connectionMaxTotal
          setDefaultConnectionConfig(ConnectionConfig.custom().setConnectTimeout(Timeout.of(connectionTimeout)).build())
        }
      return connectionManager
    }
  }
}