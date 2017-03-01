package cs646.edu.sdsu.cs.slate;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Runnable {

    public final static String MODE_DRAW = "DRAW";
    public final static String MODE_DELETE = "DELETE";
    public final static String MODE_MOVE = "MOVE_M";
    private long delay = 40;
    Canvas canvasHandler;
    public String modeSelected;
    public int brushColor;
    private Thread calculationThread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setModeSelected(MODE_DRAW);
        getSupportActionBar().setTitle("Slate-"+getString(R.string.draw_mode));
        setBrushColor(Color.BLACK);
        setContentView(R.layout.activity_main);
        canvasHandler = (Canvas)findViewById(R.id.viewCanvas);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String appName = getString(R.string.app_name);
        switch (item.getItemId()){
            case R.id.menuDraw:

                getSupportActionBar().setTitle(appName+"-"+getString(R.string.draw_mode));
                setModeSelected(MODE_DRAW);
                canvasHandler.resetCircle(0,0,0,null);

              break;

            case R.id.menuMove:

                getSupportActionBar().setTitle(appName+"-"+getString(R.string.move_mode));
                setModeSelected(MODE_MOVE);
                startAnim(delay);

                break;
            case  R.id.menuDelete:

                getSupportActionBar().setTitle(appName+"-"+getString(R.string.delete_mode));
                setModeSelected(MODE_DELETE);

                break;

            case R.id.colorRed:
                setBrushColor(Color.RED);
                break;

            case R.id.colorBlack:
                setBrushColor(Color.BLACK);
                break;

            case R.id.colorBlue:
                setBrushColor(Color.BLUE);
                break;

            case R.id.colorGreen:
                setBrushColor(Color.GREEN);
                break;
            default:


        }
        return super.onOptionsItemSelected(item);
    }

    public String getModeSelected() {
        return modeSelected;
    }

    public void setModeSelected(String modeSelected) {
        this.modeSelected = modeSelected;
    }

    public int getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(int brushColor) {
        this.brushColor = brushColor;
    }


    @Override
    public void run() {

        try{

            while(true){

                for(Circle c : canvasHandler.circleList){
                    c.move();

                }

                canvasHandler.postInvalidate();

                Thread.sleep(delay);
            }
        }catch (Exception e){

        }

    }
    //Method to start the thread.
    public void startAnim(long delay){
        this.delay = delay;
        calculationThread = new Thread(this);
        calculationThread.start();
    }



}


