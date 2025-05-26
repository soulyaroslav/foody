package com.jerdoul.foody.data.error

import com.jerdoul.foody.domain.Result
import com.jerdoul.foody.domain.error.NetworkErrorHandler
import com.jerdoul.foody.domain.error.NetworkErrorParser
import com.jerdoul.foody.domain.error.type.Error
import com.jerdoul.foody.domain.error.type.NetworkError
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class NetworkErrorHandlerImpl @Inject constructor(private val networkErrorParser: NetworkErrorParser) : NetworkErrorHandler {

    override suspend fun <D> runWithErrorHandler(block: suspend () -> D): Result<D, NetworkError> = try {
        Result.Success(block())
    } catch (e: HttpException) {
        val error = networkErrorParser.parse(e)
        Result.Error(error)
    } catch (e: UnknownHostException) {
        Result.Error(NetworkError.Http.NoNetworkConnection)
    } catch (e: SSLHandshakeException) {
        Result.Error(NetworkError.Http.NoNetworkConnection)
    }
}