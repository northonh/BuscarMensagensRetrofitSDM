package br.edu.ifspsaocarlos.sdm.buscarmensagensretrofitsdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    // Referências para os elementos de UI
    private EditText remetenteEt;
    private EditText destinatarioEt;
    private EditText ultimaMensagemEt;

    // Constante para Web Services
    private final String URL_BASE = "http://www.nobile.pro.br/sdm/mensageiro/";
    private final String CORPO_MENSAGEM_JSON = "corpo";

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
        retrofit = new Retrofit.Builder().baseUrl(URL_BASE).build();
    }

    public void buscarMensagens(View view) {
        if (view.getId() == R.id.bt_buscar_mensagens) {
            // Instanciando o objeto de acesso aos Web Services
            MensageiroApi mensageiroApi = retrofit.create(MensageiroApi.class);

            // Enfileirando a requisição em modo assíncrono
            mensageiroApi.getMensagemByQueryId(ultimaMensagemEt.
                    getText().toString()).enqueue(
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                // Recupera a mensagem da resposta da requisição
                                JSONObject mensagemJson = new JSONObject(response.body().string());

                                // Cria um ArrayList de Strings para guardar o CORPO das mensagens
                                ArrayList<String> mensagensAl = new ArrayList<>();

                                // Se a mensagem não é nula
                                if (mensagemJson != null) {
                                    mensagensAl.add(mensagemJson.getString(CORPO_MENSAGEM_JSON));
                                }
                                /* Agora com o ArrayList de Strings, cria uma Intent para enviar mostrar essas mensagens*/
                                Intent mostrarMensagensIntent = new Intent(getApplicationContext(), MostraMensagensActivity.class);

                                // Passa o ArrayList de String como EXTRA para a Intent
                                mostrarMensagensIntent.putStringArrayListExtra(
                                        MENSAGENS_STRING_ARRAY_EXTRA, mensagensAl);

                                // Inicia a Activity que vai mostrar as mensagens
                                startActivity(mostrarMensagensIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Erro na resposta do WS", Toast.LENGTH_LONG).show();
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
