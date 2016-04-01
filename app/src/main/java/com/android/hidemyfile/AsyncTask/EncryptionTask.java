package com.android.hidemyfile.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.android.hidemyfile.Encryption.Encryption;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class EncryptionTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "EncryptionTask";
    private EncryptionCallbacks encryptionCallbacks;

    private String secretKey;
    private String filePath;

    public static int NO_SUCH_ALGORITHM_EXCEPTION = 0;
    public static int NO_SUCH_PADDING_EXCEPTION = 1;
    public static int INVALID_KEY_EXCEPTION = 2;
    public static int UNSUPPORTED_ENCODING_EXCEPTION = 3;
    public static int IO_EXCEPTION = 4;

    public EncryptionTask(String secretKey, String filePath) {
        this.secretKey = secretKey;
        this.filePath = filePath;
    }

    @Override
    protected void onPreExecute() {
        if (encryptionCallbacks != null) {
            encryptionCallbacks.onPrepare();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            Encryption.encrypt(secretKey, new File(filePath));

            if (encryptionCallbacks != null) {
                encryptionCallbacks.onSuccess();
            }
        } catch (NoSuchAlgorithmException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(NO_SUCH_ALGORITHM_EXCEPTION);
            }
        } catch (NoSuchPaddingException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(NO_SUCH_PADDING_EXCEPTION);
            }
        } catch (InvalidKeyException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(INVALID_KEY_EXCEPTION);
            }
        } catch (UnsupportedEncodingException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(UNSUPPORTED_ENCODING_EXCEPTION);
            }
        } catch (IOException ex) {
            Log.e(TAG, "doInBackground() -> ", ex);
            if (encryptionCallbacks != null) {
                encryptionCallbacks.onException(IO_EXCEPTION);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (encryptionCallbacks != null) {
            encryptionCallbacks.onComplete();
        }
    }

    public void setEncryptionCallbacks(EncryptionCallbacks encryptionCallbacks) {
        this.encryptionCallbacks = encryptionCallbacks;
    }

    public interface EncryptionCallbacks {
        void onPrepare();

        void onComplete();

        void onSuccess();

        void onException(int exceptionCode);
    }
}