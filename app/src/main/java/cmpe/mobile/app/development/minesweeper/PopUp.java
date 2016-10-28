package cmpe.mobile.app.development.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by saipranesh on 28-Feb-16.
 */
public class PopUp extends Activity{

    public static final String GAME_STATE = "cmpe.mobile.app.development.minesweeper.PopUp.GAME_STATE";
    public static final String USER_OPTION = "cmpe.mobile.app.development.minesweeper.PopUp.USER_OPTION";
    private TextView mTextView;
    private Button mRestartButton;
    private Button mExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.7),(int)(height*0.5));

        final Intent minesweeper = getIntent();
        String gameState = minesweeper.getStringExtra(GAME_STATE);
        mTextView = (TextView)findViewById(R.id.textView);
        mTextView.setText(gameState);

        mRestartButton = (Button)findViewById(R.id.Restart);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserOption("restart");
                finish();
            }
        });

        mExitButton = (Button)findViewById(R.id.Exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserOption("exit");
                finish();
            }
        });
    }

    private void sendUserOption(String result){
        String selectedOption = "";
        if(result.equals("restart")){
            selectedOption = "restart";
        }else if(result.equals("exit")){
            selectedOption = "exit";
        }
        Intent sendMinesweeper = new Intent();
        sendMinesweeper.putExtra(USER_OPTION,selectedOption);
        setResult(0,sendMinesweeper);
    }
}
