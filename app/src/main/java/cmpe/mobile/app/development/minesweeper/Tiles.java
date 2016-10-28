package cmpe.mobile.app.development.minesweeper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by saipranesh on 25-Feb-16.
 */
public class Tiles extends Button {

    private boolean isMine = false;
    private boolean isCovered = false;
    public int noSurroundingMines = 0;

    public Tiles(Context context){
        super(context);
    }

    public Tiles(Context context, AttributeSet atr){
        super(context,atr);
    }

    public Tiles(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public int getNoSurroundingMines(){
        return noSurroundingMines;
    }

    public void setMine(boolean isMine){
        this.isMine = isMine;
    }

    public boolean isMineSet(){
        return isMine;
    }

    public void updateMines(){
        ++noSurroundingMines;
        if(!(this.isMineSet()))
        switch (noSurroundingMines){
            case 1: this.setTag(R.drawable.mine1);break;
            case 2: this.setTag(R.drawable.mine2);break;
            case 3: this.setTag(R.drawable.mine3);break;
            case 4: this.setTag(R.drawable.mine4);break;
            case 5: this.setTag(R.drawable.mine5);break;
            case 6: this.setTag(R.drawable.mine6);break;
            case 7: this.setTag(R.drawable.mine7);break;
            case 8: this.setTag(R.drawable.mine8);break;
            default:break;
        }
    }

    public void setCovered(boolean isCovered){
        this.isCovered = isCovered;
    }

    public boolean isCovered(){
        return isCovered;
    }

    public void showTile(){
        if(isCovered){
            return;
        }
        setCovered(true);
        if(isMine){
            this.setBackgroundResource(R.drawable.bomb);
        }else {
            switch (noSurroundingMines) {
                case 1:
                    this.setBackgroundResource(R.drawable.mine1);
                    break;
                case 2:
                    this.setBackgroundResource(R.drawable.mine2);
                    break;
                case 3:
                    this.setBackgroundResource(R.drawable.mine3);
                    break;
                case 4:
                    this.setBackgroundResource(R.drawable.mine4);
                    break;
                case 5:
                    this.setBackgroundResource(R.drawable.mine5);
                    break;
                case 6:
                    this.setBackgroundResource(R.drawable.mine6);
                    break;
                case 7:
                    this.setBackgroundResource(R.drawable.mine7);
                    break;
                case 8:
                    this.setBackgroundResource(R.drawable.mine8);
                    break;
                default:
                    this.setBackgroundResource(R.drawable.uncoveredtile);
                    break;
            }
        }
    }

}
