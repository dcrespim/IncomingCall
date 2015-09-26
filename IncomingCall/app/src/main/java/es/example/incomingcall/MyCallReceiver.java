package es.example.incomingcall;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dcrespi on 19/09/2015.
 */
public class MyCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ContentResolver resolver = context.getContentResolver();
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(incomingNumber));

            Cursor cursor = resolver.query(lookupUri, new String[]{PhoneLookup.STARRED}, null, null, null);

            String starred = "NO";
            if (cursor == null) {

            } else {
                if (cursor.moveToFirst()) {
                    // Si encuentra, cambia el valor de retorno.
                    int isStarred = cursor.getInt(cursor
                            .getColumnIndex(PhoneLookup.STARRED));

                    if (isStarred==0){
                        Log.i("MyCallReceiver", "VIBRATE mode");
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                    else {
                        Log.i("MyCallReceiver", "NORMAL mode");
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }

                    starred = isStarred==1?"FAVORITO":"NO";
                }

                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }

            Toast.makeText(context, "Call from: "+incomingNumber + " " + starred, Toast.LENGTH_LONG).show();
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE) || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Toast.makeText(context, "Detected call hangup event",Toast.LENGTH_LONG).show();
        }
    }
}
