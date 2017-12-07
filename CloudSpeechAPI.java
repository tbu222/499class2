package com.example.vpvu2.androidpizza;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechGrpc;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

/*import com.example.vpvu2.androidpizza.Model.Pizza;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.internal.DnsNameResolverProvider;
import io.grpc.okhttp.OkHttpChannelProvider;
import io.grpc.stub.StreamObserver;


public class CloudSpeechAPI {
    public static final List<String> SCOPE = Collections.singletonList("https://www.googleapis.com/auth/cloud-platform");
    public static final String TAG = "CloudSpeechAPI";

    private static final String PREFS = "SpeechService";
    private static final String PREF_ACCESS_TOKEN_VALUE = "access_token_value";
    private static final String PREF_ACCESS_TOKEN_EXPIRATION_TIME = "access_token_expiration_time";

    /**
     *  reuse an access token if its expiration time is longer than this.
     */
    private static final int ACCESS_TOKEN_EXPIRATION_TOLERANCE = 30 * 60 * 1000; // thirty minutes

    /**
     * refresh the current access token before it expires.
     */
    private static final int ACCESS_TOKEN_FETCH_MARGIN = 60 * 1000; // one minute

    private static final String HOSTNAME = "speech.googleapis.com";
    private static final int PORT = 443;
    private static Handler mHandler;

    //private final SpeechBinder mBinder = new SpeechBinder();
    private final ArrayList<Listener> mListeners = new ArrayList<>();

    private final StreamObserver<StreamingRecognizeResponse> mResponseObserver = new StreamObserver<StreamingRecognizeResponse>() {
        @Override
        public void onNext(StreamingRecognizeResponse response) {
            // pizzaId ="";
           // FirebaseDatabase database;
           // DatabaseReference pizza;
           // Pizza currentPizza;

            String text = null;
            boolean isFinal = false;
            if (response.getResultsCount() > 0) {
                final StreamingRecognitionResult result = response.getResults(0);
                isFinal = result.getIsFinal();
                if (result.getAlternativesCount() > 0) {
                    final SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                    text = alternative.getTranscript();
                }
            }

            if(isFinal)
            {
                String tokens[] =text.split(" ");
                for (Integer t =0; t < tokens.length; t++) {
                    if (tokens[t].equals("want")) {
                        if (tokens[t + 2].equals("pepperoni")) {
                            text = "Order added to cart.";

                            // new Database(getBaseContext()).addToCart(new Order(pizzaId,currentPizza.getName(),numberButton.getNumber(),currentPizza.getPrice()));
                        } else if (tokens[t + 2].equals("sausage")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("cheese")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("chicken")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("pizza")) {
                            text = "What kind of pizza? Chicken veggi, pepperoni, sausage, or chicken?";
                        } else if (tokens[t + 2].equals("Cesar")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("Mediterranean")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("Coke")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("water")) {
                            text = "Order added to cart.";
                        } else if (tokens[t + 2].equals("Sprite")) {
                            text = "Order added to cart.";
                        }
                    } else if (tokens[t].equals("cancel")) {
                        text = "Last order was canceled.";
                    } else if (tokens[t].equals("clear")) {
                        text = "Cart Cleared.";
                    } else if (tokens[t].equals("status")) {
                        text = "Order will arrive within 30 mins.";
                    }
                }
                if (text != null)
                {
                    for (Listener listener : mListeners) {
                        listener.onSpeechRecognized(text, isFinal);
                    }
                }

            }

        }

        @Override
        public void onError(Throwable t) {
            Log.e(TAG, "Error calling the API.", t);
        }

        @Override
        public void onCompleted() {
            Log.i(TAG, "API completed.");
        }

    };
    private Context mContext;
    private volatile AccessTokenTask mAccessTokenTask;
    private final Runnable mFetchAccessTokenRunnable = new Runnable() {
        @Override
        public void run() {
            fetchAccessToken();
        }
    };
    private SpeechGrpc.SpeechStub mApi;
    private StreamObserver<StreamingRecognizeRequest> mRequestObserver;

    public CloudSpeechAPI(Context mContext) {
        this.mContext = mContext;
        mHandler = new Handler();
        fetchAccessToken();

    }

    public void destroy() {
        mHandler.removeCallbacks(mFetchAccessTokenRunnable);
        mHandler = null;
        // Release the gRPC channel.
        if (mApi != null) {
            final ManagedChannel channel = (ManagedChannel) mApi.getChannel();
            if (channel != null && !channel.isShutdown()) {
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Error shutting down the gRPC channel.", e);
                }
            }
            mApi = null;
        }
    }

    private void fetchAccessToken() {
        if (mAccessTokenTask != null) {
            return;
        }
        mAccessTokenTask = new AccessTokenTask();
        mAccessTokenTask.execute();
    }

    public void addListener(@NonNull Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(@NonNull Listener listener) {
        mListeners.remove(listener);
    }

    /**
     * Starts recognizing speech audio.
     *
     * @param sampleRate The sample rate of the audio.
     */
    public void startRecognizing(int sampleRate) {
        if (mApi == null) {
            Log.w(TAG, "API not ready. Ignoring the request.");
            return;
        }

        // Configure the API
        mRequestObserver = mApi.streamingRecognize(mResponseObserver);

        StreamingRecognitionConfig streamingConfig = StreamingRecognitionConfig.newBuilder()
                .setConfig(RecognitionConfig.newBuilder()
                        .setLanguageCode("en-US")
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(sampleRate)
                        .build()
                )
                .setInterimResults(true)
                .setSingleUtterance(true)
                .build();

        StreamingRecognizeRequest streamingRecognizeRequest = StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build();
        mRequestObserver.onNext(streamingRecognizeRequest);
    }

    /**
     * Recognizes the speech audio. This method should be called every time a chunk of byte buffer
     * is ready.
     *
     * @param data The audio data.
     * @param size The number of elements that are actually relevant in the {@code data}.
     */
    public void recognize(byte[] data, int size) {
        if (mRequestObserver == null) {
            return;
        }
        // Call the streaming recognition API
        mRequestObserver.onNext(StreamingRecognizeRequest.newBuilder()
                .setAudioContent(ByteString.copyFrom(data, 0, size))
                .build());
    }

    /**
     * Finishes recognizing speech audio.
     */
    public void finishRecognizing() {
        if (mRequestObserver == null) {
            return;
        }
        mRequestObserver.onCompleted();
        mRequestObserver = null;
    }

    public interface Listener {
        //Called when a new piece of text was recognized by the Speech API.
        void onSpeechRecognized(String text, boolean isFinal);
    }

    private class AccessTokenTask extends AsyncTask<Void, Void, AccessToken> {

        @Override
        protected AccessToken doInBackground(Void... voids) {

            final SharedPreferences prefs = mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            String tokenValue = prefs.getString(PREF_ACCESS_TOKEN_VALUE, null);
            long expirationTime = prefs.getLong(PREF_ACCESS_TOKEN_EXPIRATION_TIME, -1);

            // Check if the current token is still valid for a while
            if (tokenValue != null && expirationTime > 0) {
                if (expirationTime > System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TOLERANCE) {
                    return new AccessToken(tokenValue, new Date(expirationTime));
                }
            }

            final InputStream stream = mContext.getResources().openRawResource(R.raw.credential);
            try {
                final GoogleCredentials credentials = GoogleCredentials.fromStream(stream).createScoped(SCOPE);
                final AccessToken token = credentials.refreshAccessToken();
                prefs.edit()
                        .putString(PREF_ACCESS_TOKEN_VALUE, token.getTokenValue())
                        .putLong(PREF_ACCESS_TOKEN_EXPIRATION_TIME, token.getExpirationTime().getTime())
                        .apply();
                return token;
            } catch (IOException e) {
                Log.e(TAG, "Failed to obtain access token.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(AccessToken accessToken) {
            mAccessTokenTask = null;
            final ManagedChannel channel = new OkHttpChannelProvider()
                    .builderForAddress(HOSTNAME, PORT)
                    .nameResolverFactory(new DnsNameResolverProvider())
                    .intercept(new GoogleCredentialsI(new GoogleCredentials(accessToken)
                            .createScoped(SCOPE)))
                    .build();
            mApi = SpeechGrpc.newStub(channel);

            // Schedule access token refresh before it expires
            if (mHandler != null) {
                mHandler.postDelayed(mFetchAccessTokenRunnable,
                        Math.max(accessToken.getExpirationTime().getTime() - System.currentTimeMillis() - ACCESS_TOKEN_FETCH_MARGIN, ACCESS_TOKEN_EXPIRATION_TOLERANCE));
            }
        }
    }
}