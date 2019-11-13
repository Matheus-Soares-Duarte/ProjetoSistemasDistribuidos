package stubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.24.0)",
    comments = "Source: Comunicacao.proto")
public final class ComunicacaoGrpc {

  private ComunicacaoGrpc() {}

  public static final String SERVICE_NAME = "Comunicacao";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.criarMesaRequest,
      stubs.ComunicacaoOuterClass.criarMesaResponse> getCriarMesaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "criarMesa",
      requestType = stubs.ComunicacaoOuterClass.criarMesaRequest.class,
      responseType = stubs.ComunicacaoOuterClass.criarMesaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.criarMesaRequest,
      stubs.ComunicacaoOuterClass.criarMesaResponse> getCriarMesaMethod() {
    io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.criarMesaRequest, stubs.ComunicacaoOuterClass.criarMesaResponse> getCriarMesaMethod;
    if ((getCriarMesaMethod = ComunicacaoGrpc.getCriarMesaMethod) == null) {
      synchronized (ComunicacaoGrpc.class) {
        if ((getCriarMesaMethod = ComunicacaoGrpc.getCriarMesaMethod) == null) {
          ComunicacaoGrpc.getCriarMesaMethod = getCriarMesaMethod =
              io.grpc.MethodDescriptor.<stubs.ComunicacaoOuterClass.criarMesaRequest, stubs.ComunicacaoOuterClass.criarMesaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "criarMesa"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.criarMesaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.criarMesaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ComunicacaoMethodDescriptorSupplier("criarMesa"))
              .build();
        }
      }
    }
    return getCriarMesaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.entrarMesaRequest,
      stubs.ComunicacaoOuterClass.informacoesJogoResponse> getEntrarMesaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "entrarMesa",
      requestType = stubs.ComunicacaoOuterClass.entrarMesaRequest.class,
      responseType = stubs.ComunicacaoOuterClass.informacoesJogoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.entrarMesaRequest,
      stubs.ComunicacaoOuterClass.informacoesJogoResponse> getEntrarMesaMethod() {
    io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.entrarMesaRequest, stubs.ComunicacaoOuterClass.informacoesJogoResponse> getEntrarMesaMethod;
    if ((getEntrarMesaMethod = ComunicacaoGrpc.getEntrarMesaMethod) == null) {
      synchronized (ComunicacaoGrpc.class) {
        if ((getEntrarMesaMethod = ComunicacaoGrpc.getEntrarMesaMethod) == null) {
          ComunicacaoGrpc.getEntrarMesaMethod = getEntrarMesaMethod =
              io.grpc.MethodDescriptor.<stubs.ComunicacaoOuterClass.entrarMesaRequest, stubs.ComunicacaoOuterClass.informacoesJogoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "entrarMesa"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.entrarMesaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.informacoesJogoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ComunicacaoMethodDescriptorSupplier("entrarMesa"))
              .build();
        }
      }
    }
    return getEntrarMesaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
      stubs.ComunicacaoOuterClass.comprarCartaResponse> getComprarCartaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "comprarCarta",
      requestType = stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.class,
      responseType = stubs.ComunicacaoOuterClass.comprarCartaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
      stubs.ComunicacaoOuterClass.comprarCartaResponse> getComprarCartaMethod() {
    io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest, stubs.ComunicacaoOuterClass.comprarCartaResponse> getComprarCartaMethod;
    if ((getComprarCartaMethod = ComunicacaoGrpc.getComprarCartaMethod) == null) {
      synchronized (ComunicacaoGrpc.class) {
        if ((getComprarCartaMethod = ComunicacaoGrpc.getComprarCartaMethod) == null) {
          ComunicacaoGrpc.getComprarCartaMethod = getComprarCartaMethod =
              io.grpc.MethodDescriptor.<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest, stubs.ComunicacaoOuterClass.comprarCartaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "comprarCarta"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.comprarCartaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ComunicacaoMethodDescriptorSupplier("comprarCarta"))
              .build();
        }
      }
    }
    return getComprarCartaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
      stubs.ComunicacaoOuterClass.passarVezResponse> getPassarVezMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "passarVez",
      requestType = stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.class,
      responseType = stubs.ComunicacaoOuterClass.passarVezResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
      stubs.ComunicacaoOuterClass.passarVezResponse> getPassarVezMethod() {
    io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest, stubs.ComunicacaoOuterClass.passarVezResponse> getPassarVezMethod;
    if ((getPassarVezMethod = ComunicacaoGrpc.getPassarVezMethod) == null) {
      synchronized (ComunicacaoGrpc.class) {
        if ((getPassarVezMethod = ComunicacaoGrpc.getPassarVezMethod) == null) {
          ComunicacaoGrpc.getPassarVezMethod = getPassarVezMethod =
              io.grpc.MethodDescriptor.<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest, stubs.ComunicacaoOuterClass.passarVezResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "passarVez"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.passarVezResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ComunicacaoMethodDescriptorSupplier("passarVez"))
              .build();
        }
      }
    }
    return getPassarVezMethod;
  }

  private static volatile io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
      stubs.ComunicacaoOuterClass.sairMesaResponse> getSairMesaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sairMesa",
      requestType = stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.class,
      responseType = stubs.ComunicacaoOuterClass.sairMesaResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
      stubs.ComunicacaoOuterClass.sairMesaResponse> getSairMesaMethod() {
    io.grpc.MethodDescriptor<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest, stubs.ComunicacaoOuterClass.sairMesaResponse> getSairMesaMethod;
    if ((getSairMesaMethod = ComunicacaoGrpc.getSairMesaMethod) == null) {
      synchronized (ComunicacaoGrpc.class) {
        if ((getSairMesaMethod = ComunicacaoGrpc.getSairMesaMethod) == null) {
          ComunicacaoGrpc.getSairMesaMethod = getSairMesaMethod =
              io.grpc.MethodDescriptor.<stubs.ComunicacaoOuterClass.requisicaoNaVezRequest, stubs.ComunicacaoOuterClass.sairMesaResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sairMesa"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.requisicaoNaVezRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stubs.ComunicacaoOuterClass.sairMesaResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ComunicacaoMethodDescriptorSupplier("sairMesa"))
              .build();
        }
      }
    }
    return getSairMesaMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ComunicacaoStub newStub(io.grpc.Channel channel) {
    return new ComunicacaoStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ComunicacaoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ComunicacaoBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ComunicacaoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ComunicacaoFutureStub(channel);
  }

  /**
   */
  public static abstract class ComunicacaoImplBase implements io.grpc.BindableService {

    /**
     */
    public void criarMesa(stubs.ComunicacaoOuterClass.criarMesaRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.criarMesaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCriarMesaMethod(), responseObserver);
    }

    /**
     */
    public void entrarMesa(stubs.ComunicacaoOuterClass.entrarMesaRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.informacoesJogoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getEntrarMesaMethod(), responseObserver);
    }

    /**
     */
    public void comprarCarta(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.comprarCartaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getComprarCartaMethod(), responseObserver);
    }

    /**
     */
    public void passarVez(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.passarVezResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPassarVezMethod(), responseObserver);
    }

    /**
     */
    public void sairMesa(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.sairMesaResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSairMesaMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCriarMesaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                stubs.ComunicacaoOuterClass.criarMesaRequest,
                stubs.ComunicacaoOuterClass.criarMesaResponse>(
                  this, METHODID_CRIAR_MESA)))
          .addMethod(
            getEntrarMesaMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                stubs.ComunicacaoOuterClass.entrarMesaRequest,
                stubs.ComunicacaoOuterClass.informacoesJogoResponse>(
                  this, METHODID_ENTRAR_MESA)))
          .addMethod(
            getComprarCartaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
                stubs.ComunicacaoOuterClass.comprarCartaResponse>(
                  this, METHODID_COMPRAR_CARTA)))
          .addMethod(
            getPassarVezMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
                stubs.ComunicacaoOuterClass.passarVezResponse>(
                  this, METHODID_PASSAR_VEZ)))
          .addMethod(
            getSairMesaMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                stubs.ComunicacaoOuterClass.requisicaoNaVezRequest,
                stubs.ComunicacaoOuterClass.sairMesaResponse>(
                  this, METHODID_SAIR_MESA)))
          .build();
    }
  }

  /**
   */
  public static final class ComunicacaoStub extends io.grpc.stub.AbstractStub<ComunicacaoStub> {
    private ComunicacaoStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ComunicacaoStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ComunicacaoStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ComunicacaoStub(channel, callOptions);
    }

    /**
     */
    public void criarMesa(stubs.ComunicacaoOuterClass.criarMesaRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.criarMesaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCriarMesaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void entrarMesa(stubs.ComunicacaoOuterClass.entrarMesaRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.informacoesJogoResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getEntrarMesaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void comprarCarta(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.comprarCartaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getComprarCartaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void passarVez(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.passarVezResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPassarVezMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sairMesa(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request,
        io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.sairMesaResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSairMesaMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ComunicacaoBlockingStub extends io.grpc.stub.AbstractStub<ComunicacaoBlockingStub> {
    private ComunicacaoBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ComunicacaoBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ComunicacaoBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ComunicacaoBlockingStub(channel, callOptions);
    }

    /**
     */
    public stubs.ComunicacaoOuterClass.criarMesaResponse criarMesa(stubs.ComunicacaoOuterClass.criarMesaRequest request) {
      return blockingUnaryCall(
          getChannel(), getCriarMesaMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<stubs.ComunicacaoOuterClass.informacoesJogoResponse> entrarMesa(
        stubs.ComunicacaoOuterClass.entrarMesaRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getEntrarMesaMethod(), getCallOptions(), request);
    }

    /**
     */
    public stubs.ComunicacaoOuterClass.comprarCartaResponse comprarCarta(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request) {
      return blockingUnaryCall(
          getChannel(), getComprarCartaMethod(), getCallOptions(), request);
    }

    /**
     */
    public stubs.ComunicacaoOuterClass.passarVezResponse passarVez(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request) {
      return blockingUnaryCall(
          getChannel(), getPassarVezMethod(), getCallOptions(), request);
    }

    /**
     */
    public stubs.ComunicacaoOuterClass.sairMesaResponse sairMesa(stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request) {
      return blockingUnaryCall(
          getChannel(), getSairMesaMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ComunicacaoFutureStub extends io.grpc.stub.AbstractStub<ComunicacaoFutureStub> {
    private ComunicacaoFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ComunicacaoFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ComunicacaoFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ComunicacaoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<stubs.ComunicacaoOuterClass.criarMesaResponse> criarMesa(
        stubs.ComunicacaoOuterClass.criarMesaRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCriarMesaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<stubs.ComunicacaoOuterClass.comprarCartaResponse> comprarCarta(
        stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getComprarCartaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<stubs.ComunicacaoOuterClass.passarVezResponse> passarVez(
        stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPassarVezMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<stubs.ComunicacaoOuterClass.sairMesaResponse> sairMesa(
        stubs.ComunicacaoOuterClass.requisicaoNaVezRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSairMesaMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CRIAR_MESA = 0;
  private static final int METHODID_ENTRAR_MESA = 1;
  private static final int METHODID_COMPRAR_CARTA = 2;
  private static final int METHODID_PASSAR_VEZ = 3;
  private static final int METHODID_SAIR_MESA = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ComunicacaoImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ComunicacaoImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CRIAR_MESA:
          serviceImpl.criarMesa((stubs.ComunicacaoOuterClass.criarMesaRequest) request,
              (io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.criarMesaResponse>) responseObserver);
          break;
        case METHODID_ENTRAR_MESA:
          serviceImpl.entrarMesa((stubs.ComunicacaoOuterClass.entrarMesaRequest) request,
              (io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.informacoesJogoResponse>) responseObserver);
          break;
        case METHODID_COMPRAR_CARTA:
          serviceImpl.comprarCarta((stubs.ComunicacaoOuterClass.requisicaoNaVezRequest) request,
              (io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.comprarCartaResponse>) responseObserver);
          break;
        case METHODID_PASSAR_VEZ:
          serviceImpl.passarVez((stubs.ComunicacaoOuterClass.requisicaoNaVezRequest) request,
              (io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.passarVezResponse>) responseObserver);
          break;
        case METHODID_SAIR_MESA:
          serviceImpl.sairMesa((stubs.ComunicacaoOuterClass.requisicaoNaVezRequest) request,
              (io.grpc.stub.StreamObserver<stubs.ComunicacaoOuterClass.sairMesaResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ComunicacaoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ComunicacaoBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return stubs.ComunicacaoOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Comunicacao");
    }
  }

  private static final class ComunicacaoFileDescriptorSupplier
      extends ComunicacaoBaseDescriptorSupplier {
    ComunicacaoFileDescriptorSupplier() {}
  }

  private static final class ComunicacaoMethodDescriptorSupplier
      extends ComunicacaoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ComunicacaoMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ComunicacaoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ComunicacaoFileDescriptorSupplier())
              .addMethod(getCriarMesaMethod())
              .addMethod(getEntrarMesaMethod())
              .addMethod(getComprarCartaMethod())
              .addMethod(getPassarVezMethod())
              .addMethod(getSairMesaMethod())
              .build();
        }
      }
    }
    return result;
  }
}
