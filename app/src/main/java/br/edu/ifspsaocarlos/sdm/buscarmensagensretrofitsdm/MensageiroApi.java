package br.edu.ifspsaocarlos.sdm.buscarmensagensretrofitsdm;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MensageiroApi {
    /* Linka requisição HTTP GET para endpoint "mensagem" com o método da Interface getMensagemByPathId
    * O método recebe uma String mensagemId como parâmetro que será usada para substituir {mensagemId}
    * no Path da URL*/
    @GET("mensagem/{mensagemId}")
    Call<ResponseBody> getMensagemByPathId(@Path("mensagemId") String mensagemId);

    /* Linka requisição HTTP GET para endpoint "mensagem" com o método da Interface getMensagemByQueryId
    * O método recebe uma String mensagemId como parâmetro que será usada para enviar um parâmetro id
     * para a requisição no formato mensagem?id= */
    @GET("mensagem")
    Call<ResponseBody> getMensagemByQueryId(@Query("id") String id);

    /* Linka requisição HTTP GET para endpoint "mensagem" com o método da Interface getMensagemByPathId
     * O método recebe uma String ultimaMensagemId, uma String origemId e uma String destinoId como
     * parâmetros que serão usados para substituir {mensagemId}, {origemId} e {destinoId}, respectivamente
     * no Path da URL*/
    @GET("mensagens/{ultimaMensagemId}/{origemId}/{destinoId}")
    Call<ListaMensagem> getMensagensByPath(@Path("ultimaMensagemId") String id, @Path("origemId") String origemId, @Path("destinoId") String destinoId);

    /* Linka requisição HTTP GET para endpoint "mensagem" com o método da Interface getMensagemByQueryId
     * O método recebe uma String ultimaMensagemId, uma String origemId e uma String destinoId como
     * parâmetros que serão usados para enviar os parâmetros para a requisição no formato
     * mensagem?ultimaMensagemId=100&origemId=1&destinoId=2 */
    @GET("mensagens")
    Call<ListaMensagem> getMensagensByQuery(@Query("ultimaMensagemId") String id, @Query("origemId") String origemId, @Query("destinoId") String destinoId);
}
