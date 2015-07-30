package in.sadrudd.contactanalyser;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ContactAnalyserMainActivity extends AppCompatActivity implements View.OnClickListener{

    private Fragment deCluttrMainFragment;

    private Button btnAnalyseCallLog;
    private TextView tvCallLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decluttr_main);
        initialiseMainFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decluttr_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialiseMainFragment(){
        deCluttrMainFragment = getFragmentManager().findFragmentById(R.id.fragment_decluttr_main);
        btnAnalyseCallLog = (Button) findViewById(R.id.btn_analyse_call_log);
        btnAnalyseCallLog.setOnClickListener(this);
        tvCallLog = (TextView) findViewById(R.id.tv_call_log);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_analyse_call_log:
                tvCallLog.setText("");
                break;

        }
    }

    public Button getBtnAnalyseCallLog(){
        return btnAnalyseCallLog;
    }
}
