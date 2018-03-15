package br.edu.ifspsaocarlos.sdm.buscarmensagensretrofitsdm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    // Referências para os elementos de UI
    private EditText remetenteEt;
    private EditText destinatarioEt;
    private EditText ultimaMensagemEt;

    // Constante para Web Services
    private final String URL_BASE = "http://www.nobile.pro.br/sdm/mensageiro/";

    // Constante para passar como parâmetro para a tela que vai mostrar as mensagens
    public static final String MENSAGENS_STRING_ARRAY_EXTRA = "MENSAGENS_STRING_ARRAY_EXTRA";

    // Referência para o Retrofit
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buscando referências para objetos de UI
        remetenteEt = findViewById(R.id.et_remetente);
        destinatarioEt = findViewById(R.id.et_destinatario);
        ultimaMensagemEt = findViewById(R.id.et_ultima_mensagem);

        // Instanciando o Retrofit
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(URL_BASE);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        retrofit = builder.build();
    }

    public void buscarMensagens(View view) {
        if (view.getId() == R.id.bt_buscar_mensagens) {
            // Instanciando o objeto de acesso aos Web Services
            MensageiroApi mensageiroApi = retrofit.create(MensageiroApi.class);

            // Enfileirando a requisição em modo assíncrono
            mensageiroApi.getRawMensagensByPath(
                    ultimaMensagemEt.getText().toString(), remetenteEt.getText().toString(),
                    destinatarioEt.getText().toString())
                    .enqueue(
                            new Callback<List<Mensagem>>() {
                                @Override
                                public void onResponse(Call<List<Mensagem>> call, Response<List<Mensagem>> response) {

                                    /* Converte automaticamente o Corpo da Resposta para uma Lista de
                                    Mensagens */
                                    List<Mensagem> listaMensagem = response.body();

                                    // Cria um ArrayList de Strings para guardar o CORPO das mensagens
                                    ArrayList<String> mensagensAl = new ArrayList<>();

                                    // Percorre o JSONArray para recuperar o CORPO das mensagens
                                    for (Mensagem mensagem : listaMensagem) {
                                        // Recupera o CORPO da mensagem de dentro do JSONObject
                                        mensagensAl.add(mensagem.getCorpo());
                                    }

                                    /* Agora com o ArrayList de Strings, cria uma Intent para enviar mostrar essas mensagens*/
                                    Intent mostrarMensagensIntent = new Intent(getApplicationContext(), MostraMensagensActivity.class);
                                    // Passa o ArrayList de String como EXTRA para a Intent
                                    mostrarMensagensIntent.putStringArrayListExtra(MENSAGENS_STRING_ARRAY_EXTRA, mensagensAl);

                                    // Inicia a Activity que vai mostrar as mensagens
                                    startActivity(mostrarMensagensIntent);
                                }
                                @Override
                                public void onFailure(Call<List<Mensagem>> call, Throwable t) {
                                    Log.d(getString(R.string.app_name), "falhou");
                                }
                            }
                    );
            // Limpa os campos
            limparCampos();
        }
    }
    private void limparCampos() {
        remetenteEt.setText("");
        destinatarioEt.setText("");
        ultimaMensagemEt.setText("");
    }
}
