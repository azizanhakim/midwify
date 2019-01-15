package com.azizanhakim.midwife.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.azizanhakim.midwife.Config.LanguageConfig;
import com.azizanhakim.midwife.Model.User;
import com.azizanhakim.midwife.R;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

public class MidwifySuara extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = Midwify.class.getName();
    private Gson gson = GsonFactory.getGson();
    private AIDataService aiDataService;
    private ChatView chatView;
    private User myAccount;
    private User midwifyBot;
    private String hasil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.midwify_suara);

        initChatView();

        //Language, Dialogflow Client access token
        final LanguageConfig config = new LanguageConfig("id", "ac4a63b1816242c5be34598f9f951781");
        initService(config);

        //panggil menu midwify chat
        ImageView midwify_chat = findViewById(R.id.chat);
        midwify_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent chatIntent = new Intent(MidwifySuara.this, Midwify.class);
                startActivity(chatIntent);
            }
        });
    }
    private boolean punyaKoneksi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfos = connectivityManager.getActiveNetworkInfo();

        if (networkInfos != null && networkInfos.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mobile != null && mobile.isConnectedOrConnecting() || wifi != null && wifi.isConnectedOrConnecting())
                return true;
            else return false;
        } else
            return false;
    }
    @Override
    public void onClick(View v) {
        if (hasil != null) {
            //new message
            final Message message = new Message.Builder()
                    .setUser(myAccount)
                    .setRightMessage(true)
                    .setMessageText(hasil)
                    .hideIcon(true)
                    .build();
            //Set to chat view
            chatView.send(message);
            if (punyaKoneksi(MidwifySuara.this))
            {
                sendRequest(hasil);
            }else if (!punyaKoneksi(MidwifySuara.this))
            {
                Toast.makeText(MidwifySuara.this,"Koneksi Internet Tidak Tersedia",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MidwifySuara.this, "Silahkan tekan tombol mikrofon untuk memulai", Toast.LENGTH_SHORT).show();
        }

        //Reset edit text
        chatView.setInputText("");
    }

    /*
     * AIRequest harus memiliki query atau event
     */
    private void sendRequest(String text) {
        final String queryString = String.valueOf(text);
        final String eventString = null;
        final String contextString = null;

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        new AiTask().execute(queryString, eventString, contextString);
    }

    private class AiTask extends AsyncTask<String, Void, AIResponse> {
        private AIError aiError;

        @Override
        protected AIResponse doInBackground(final String... params) {
            final AIRequest request = new AIRequest();
            String query = params[0];
            String event = params[1];
            String context = params[2];

            if (!TextUtils.isEmpty(query)){
                request.setQuery(query);
            }
            if (!TextUtils.isEmpty(event)){
                request.setEvent(new AIEvent(event));
            }

            RequestExtras requestExtras = null;
            if (!TextUtils.isEmpty(context)) {
                final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
                requestExtras = new RequestExtras(contexts, null);
            }

            try {
                return aiDataService.request(request, requestExtras);
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }

    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Variables
                gson.toJson(response);
                final Status status = response.getStatus();
                final Result result = response.getResult();
                final String speech = result.getFulfillment().getSpeech();
                final Metadata metadata = result.getMetadata();
                final HashMap<String, JsonElement> params = result.getParameters();

                // Logging
                Log.d(TAG, "onResult");
                Log.i(TAG, "Received success response");
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
                Log.i(TAG, "Action: " + result.getAction());
                Log.i(TAG, "Speech: " + speech);

                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s",
                                entry.getKey(), entry.getValue().toString()));
                    }
                }

                //Update view to bot says
                final Message receivedMessage = new Message.Builder()
                        .setUser(midwifyBot)
                        .setRightMessage(false)
                        .setMessageText(speech)
                        .build();
                chatView.receive(receivedMessage);
            }
        });
    }

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,error.toString());
            }
        });
    }

    private void initChatView() {
        int myId = 0;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_user);
        String myName = "Anda";
        myAccount = new User(myId, myName, icon);

        int botId = 1;
        String botName = "MidWify Bot";
        midwifyBot = new User(botId, botName, icon);

        chatView = findViewById(R.id.chat_view);
        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        chatView.setLeftBubbleColor(Color.WHITE);
        chatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        chatView.setSendButtonColor(ContextCompat.getColor(this, R.color.lightBlue500));
        chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);
        chatView.setInputTextHint("ketik di sini...");
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);
        chatView.setOnClickSendButtonListener(this);
    }

    private void initService(final LanguageConfig languageConfig) {
        final AIConfiguration.SupportedLanguages lang =
                AIConfiguration.SupportedLanguages.fromLanguageTag(languageConfig.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(languageConfig.getAccessToken(),
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(this, config);
    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Perangkat anda tidak mendukung masukan berupa suara!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                hasil = result.get(0);
            }
        }
    }


}