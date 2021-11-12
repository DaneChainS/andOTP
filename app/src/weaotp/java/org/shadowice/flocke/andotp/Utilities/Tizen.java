package org.shadowice.flocke.andotp.Utilities;


import org.shadowice.flocke.andotp.Database.Entry;

import android.util.Log;


import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


import javax.crypto.SecretKey;

public class Tizen {

    private static final String TAG = "WearOS (weaOTP/link)";

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String transferstring;

    public static void setTizenData(byte[] databytes) {
        String hexdata = bytesToHex(databytes);
        transferstring = hexdata;
    //debug    Log.e(TAG, "Tizen set called bytes: "+transferstring);
    }

    public static String getTizenString() {
    //debug    Log.e(TAG, "Tizen get called");
        if (transferstring==null) {
            transferstring = "No Data";
        }
        return transferstring;
    }

    public static boolean backupToTizen(String password, ArrayList<Entry> entries)
    {
        String plain = DatabaseHelper.entriesToString(entries);

        try {
            int iter = EncryptionHelper.generateRandomIterations();
            byte[] salt = EncryptionHelper.generateRandom(Constants.ENCRYPTION_IV_LENGTH);

            SecretKey key = EncryptionHelper.generateSymmetricKeyPBKDF2(password, iter, salt);
            byte[] encrypted = EncryptionHelper.encrypt(key, plain.getBytes(StandardCharsets.UTF_8));

            byte[] iterBytes = ByteBuffer.allocate(Constants.INT_LENGTH).putInt(iter).array();
            byte[] data = new byte[Constants.INT_LENGTH + Constants.ENCRYPTION_IV_LENGTH + encrypted.length];

            System.arraycopy(iterBytes, 0, data, 0, Constants.INT_LENGTH);
            System.arraycopy(salt, 0, data, Constants.INT_LENGTH, Constants.ENCRYPTION_IV_LENGTH);
            System.arraycopy(encrypted, 0, data, Constants.INT_LENGTH + Constants.ENCRYPTION_IV_LENGTH, encrypted.length);

    //debug        System.out.println(data.toString());
            Tizen.setTizenData(data);
    //debug        System.out.println("Set Tizen Data");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}


