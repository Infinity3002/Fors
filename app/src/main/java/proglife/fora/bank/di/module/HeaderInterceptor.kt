package proglife.fora.bank.di.module

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import proglife.fora.bank.data.Token

class HeaderInterceptor(val token: Token): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(token.getToken()!=null && token.getHeaderName()!=null) {

            val newRequest = request.newBuilder()
                    .addHeader(token.getHeaderName()!!, token.getToken()!!)
                    .build()
            return chain.proceed(newRequest)

        }
        return chain.proceed(request)
    }
}