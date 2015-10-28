package es.tutorial.receiversms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainLibrary extends FrameLayout {

    Context context = null;
    TextView mensaje;

    public MainLibrary (Context context) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super(context);
        this.context = context;
        this.populate(context);
        context.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    public MainLibrary (Context context, AttributeSet attrs, int defStyle) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super(context);
        this.context = context;
        this.populate(context);
        context.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    public MainLibrary (Context context, AttributeSet attrs) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super(context);
        this.context = context;
        this.populate(context);
        context.registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    private void populate (Context context) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(layoutParams);

        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mensaje = new TextView(context);
        mensaje.setLayoutParams(layoutParams);
        mensaje.setId(new Random().nextInt());

        layout.addView(mensaje);
        this.addView(layout);
    }
    /**
     * Incerceptor de mensajes recibidos.
     */
    BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                // Standard decoding for SMS
                Object[] pdus = (Object[])intent.getExtras().get("pdus");
                StringBuilder message = new StringBuilder();
                SmsMessage messageSegment;

                for (int i=0;i < pdus.length;i++)
                {
                    messageSegment = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    message.append(messageSegment.getDisplayMessageBody());
                }
                if (isMessageValid(message.toString()))
                {
                    mensaje.setText(message.toString());
                }
            }
        }
        /**
         * Valida si el mensaje recibido se ajusta al patron definido como mensaje con PIN.
         * @param message mensaje recibido
         * @return true cuando el mensaje se ajusta al patron. false cuando no se ajusta.
         */
        private boolean isMessageValid(String message)
        {
            // Implementar si el mensaje es el que queremos
            return true;
        }
    };
}
