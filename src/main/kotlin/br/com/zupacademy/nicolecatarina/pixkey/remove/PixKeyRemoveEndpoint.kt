package br.com.zupacademy.nicolecatarina.pixkey.remove

import br.com.zupacademy.nicolecatarina.KeymanagerRemoveServiceGrpc
import br.com.zupacademy.nicolecatarina.RemoveRequest
import br.com.zupacademy.nicolecatarina.RemoveResponse
import br.com.zupacademy.nicolecatarina.pixkey.PixKeyService
import br.com.zupacademy.nicolecatarina.validation.interceptor.ErrorAdvice
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorAdvice
@Singleton
class PixKeyRemoveEndpoint(@field:Inject val pixKeyService: PixKeyService) :
        KeymanagerRemoveServiceGrpc.KeymanagerRemoveServiceImplBase() {

    override fun remove(request: RemoveRequest?, responseObserver: StreamObserver<RemoveResponse>?) {
        pixKeyService.delete(request!!.idCustomer, request.idPix)

        val response = RemoveResponse.newBuilder()
                .setIdCustomer(request.idCustomer)
                .setIdPix(request.idPix)
                .build()

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()

    }
}