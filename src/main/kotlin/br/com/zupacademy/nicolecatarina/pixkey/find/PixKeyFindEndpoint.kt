package br.com.zupacademy.nicolecatarina.pixkey.find

import br.com.zupacademy.nicolecatarina.FindRequest
import br.com.zupacademy.nicolecatarina.FindResponse
import br.com.zupacademy.nicolecatarina.KeymanagerFindServiceGrpc
import br.com.zupacademy.nicolecatarina.pixkey.PixKeyService
import br.com.zupacademy.nicolecatarina.validation.interceptor.ErrorAdvice
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorAdvice
@Singleton
class PixKeyFindEndpoint(@field:Inject val pixKeyService: PixKeyService) :
        KeymanagerFindServiceGrpc.KeymanagerFindServiceImplBase() {

    override fun find(request: FindRequest?, responseObserver: StreamObserver<FindResponse>?) {
        val response = pixKeyService.find(request!!.idPix, request.idCustomer)

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}