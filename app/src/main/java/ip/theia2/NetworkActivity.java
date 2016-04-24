package ip.theia2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Activity to handle client-server interactions.
 */
public class NetworkActivity extends AppCompatActivity implements NetworkMessageHandler {

    public NetworkConnection conn;
    public TextView networkOutput;
    public EditText inputMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        networkOutput = (TextView) findViewById(R.id.network_output);
        inputMsg = (EditText) findViewById(R.id.message_input);
    }

    /**
     * Implements the handle message interface.
     * @param msg message to handle.
     * @return success or fail
     */
    public Boolean handleMessage(final String msg){
        //Modifying the UI, so has to be run on the UI thread...
        //From:
        //http://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkOutput.append(msg + "\n");
            }
        });
        return true;
    }

    public void btnSendMsg(View v){
        final String msg = inputMsg.getText().toString();

        (new Thread(){
            public void run(){
                conn.sendMessage(msg);
            }
        }).start();
    }

    public void btnConnect(View v){

        EditText hostField = (EditText) findViewById(R.id.host_input);
        final String host = hostField.getText().toString();

        final InputStream trustStore = getResources().openRawResource(R.raw.truststore);

        //This seriously needs error handling.
        conn = new NetworkConnection(host, 5571, trustStore, this); //We are always using 5571.

        handleMessage("\nConnected");
    }

}
