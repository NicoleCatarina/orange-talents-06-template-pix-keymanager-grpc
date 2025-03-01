package br.com.zupacademy.nicolecatarina.pixkey.registry

import br.com.zupacademy.nicolecatarina.KeymanagerRegistryServiceGrpc
import br.com.zupacademy.nicolecatarina.RegistryRequest
import br.com.zupacademy.nicolecatarina.client.bcb.ChavePixBCBClient
import br.com.zupacademy.nicolecatarina.client.bcb.CreatePixKeyResponse
import br.com.zupacademy.nicolecatarina.client.itau.AccountHolder
import br.com.zupacademy.nicolecatarina.client.itau.AccountResponse
import br.com.zupacademy.nicolecatarina.client.itau.Institution
import br.com.zupacademy.nicolecatarina.client.itau.ItauCustomerAccountClient
import br.com.zupacademy.nicolecatarina.pixkey.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class PixKeyRegistryEndpointTest {

    @field:Inject
    lateinit var grpcClient: KeymanagerRegistryServiceGrpc.KeymanagerRegistryServiceBlockingStub

    @field:Inject
    @field:Client
    lateinit var itauCustomerAccountClient: ItauCustomerAccountClient

    @field:Inject
    lateinit var bcbClient: ChavePixBCBClient

    //    @field:Spy
    @field:Inject
    lateinit var pixKeyRepository: PixKeyRepository

    @field:Inject
    lateinit var pixKeyService: PixKeyService

    @BeforeEach
    fun setup() {
        pixKeyRepository.deleteAll()
    }

    private val request: RegistryRequest = RegistryRequest.newBuilder()
            .setIdCustomer(UUID.randomUUID().toString())
            .setKeyType(KeyType.PHONE)
            .setKeyValue("+5515988778899")
            .setAccountType(AccountType.CONTA_CORRENTE)
            .build()

    @Test
    fun `should registry a new pix key`() {
        `when`(
                itauCustomerAccountClient.findCustomerAccount(
                        request.idCustomer,
                        br.com.zupacademy.nicolecatarina.pixkey.AccountType.valueOf(request.accountType.toString())
                )
        ).thenReturn(
                HttpResponse.ok(
                        AccountResponse(
                                br.com.zupacademy.nicolecatarina.pixkey.AccountType.CONTA_CORRENTE,
                                Institution("teste", "12345"),
                                "12",
                                "444",
                                AccountHolder("1", "teste", "213122121")
                        )
                )
        )

        `when`(bcbClient.registryKey(any()))
                .thenReturn(HttpResponse.created(CreatePixKeyResponse(request.keyValue)))

//        val spy = spy(pixKeyRepository)
        val response = grpcClient.registry(request)

//        verify(spy).save(any())
        assertTrue(response.idPix != null)
    }

    @Test
    fun `should throw exception by pix key already registered`() {
        pixKeyRepository.save(
                PixKey(
                        br.com.zupacademy.nicolecatarina.pixkey.KeyType.PHONE,
                        "+5515988778899",
                        UUID.randomUUID().toString(),
                        br.com.zupacademy.nicolecatarina.pixkey.AccountType.CONTA_CORRENTE,
                        "123",
                        "23",
                        "123"
                )
        )

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registry(request)
        }

        with(response) {
            assertEquals("ALREADY_EXISTS: Chave pix '${request.keyValue}' já está cadastrada", message)
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
        }
    }

    @Test
    fun `should throw exception by customer account not found`() {
        `when`(itauCustomerAccountClient.findCustomerAccount(any(), any()))
                .thenReturn(HttpResponse.unprocessableEntity())

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registry(request)
        }

        with(response) {
            assertEquals("NOT_FOUND: 'CONTA_CORRENTE' não encontrada para cliente '${request.idCustomer}'", message)
            assertEquals(Status.NOT_FOUND.code, status.code)
        }
    }

    @Test
    fun `should throw exception by pix key already registered in another institution`() {

        `when`(itauCustomerAccountClient.findCustomerAccount(any(), any()))
                .thenReturn(HttpResponse.ok(mock(AccountResponse::class.java, Mockito.RETURNS_MOCKS)))

        `when`(bcbClient.registryKey(any()))
                .thenThrow(HttpClientResponseException::class.java)

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registry(request)
        }

        with(response) {
            assertEquals(
                    "ALREADY_EXISTS: A chave '${request.keyValue}' já foi registrada em outra instituição",
                    message
            )
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
        }
    }

    @Test
    fun `should throw exception by invalid id customer`() {

        val request = RegistryRequest.newBuilder()
                .setIdCustomer("not a valid uuid")
                .setKeyType(KeyType.PHONE)
                .setKeyValue("+5515981476877")
                .setAccountType(AccountType.CONTA_CORRENTE)
                .build()

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registry(request)//mock(RegistryRequest::class.java))
        }

        assertEquals("INVALID_ARGUMENT: há parametros incorretos", response.message)
        assertEquals(Status.INVALID_ARGUMENT.code, response.status.code)
    }

    @MockBean(ItauCustomerAccountClient::class)
    fun itauCustomerAccountClientMock() = mock(ItauCustomerAccountClient::class.java)

    @MockBean(ChavePixBCBClient::class)
    fun chavePixBCBClient() = mock(ChavePixBCBClient::class.java)

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeymanagerRegistryServiceGrpc.KeymanagerRegistryServiceBlockingStub {
            return KeymanagerRegistryServiceGrpc.newBlockingStub(channel)
        }
    }
}