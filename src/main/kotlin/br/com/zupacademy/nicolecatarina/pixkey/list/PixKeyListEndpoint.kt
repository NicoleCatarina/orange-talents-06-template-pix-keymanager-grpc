package br.com.zupacademy.nicolecatarina.pixkey.list

import br.com.zupacademy.nicolecatarina.KeymanagerListServiceGrpc
import br.com.zupacademy.nicolecatarina.ListRequest
import br.com.zupacademy.nicolecatarina.ListResponse
import br.com.zupacademy.nicolecatarina.pixkey.PixKeyService
import br.com.zupacademy.nicolecatarina.validation.interceptor.ErrorAdvice
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorAdvice
@Singleton
class PixKeyListEndpoint(@field:Inject val pixKeyService: PixKeyService) :
        KeymanagerListServiceGrpc.KeymanagerListServiceImplBase()  {

    override fun list(request: ListRequest?, responseObserver: StreamObserver<ListResponse>?) {
        val response = pixKeyService.list(request!!.idCustomer)

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}