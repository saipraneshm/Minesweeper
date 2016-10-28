package cmpe.mobile.app.development.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Minesweeper extends AppCompatActivity {

    private Tiles tile[][];
    private int totalRows = 12;
    private int totalColumns = 8;
    private int tileWH = 20;
    private int padding = 2;
    private int totalTiles = 65;
    private TableLayout minefield;
    private Intent popUp;
    private String userOption = "";
    private boolean gameOverFlag ;
    private ArrayList<String> storeCoordinatesMine = new ArrayList<>();
    private ArrayList<String> storeCoordinatesCovered = new ArrayList<>();
    private ArrayList<String> storeCoordinatesMineClicked = new ArrayList<>();
    private static final String COORDINATES_MINE = "cmpe.mobile.app.development.minesweeper.Minesweeper.COORDINATES_MINE";
    private static final String COORDINATES_MINE_CLICKED = "cmpe.mobile.app.development.minesweeper.Minesweeper.COORDINATES_MINE_CLICKED";
    private static final String COORDINATES_COVERED = "cmpe.mobile.app.development.minesweeper.Minesweeper.COORDINATES_COVERED";
    private static final String TOTAL_TILES = "cmpe.mobile.app.development.minesweeper.Minesweeper.TOTAL_TILES";
    private static final String GAME_OVER_FLAG = "cmpe.mobile.app.development.minesweeper.Minesweeper.GAME_OVER_FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        popUp = new Intent(this,PopUp.class);
        minefield = (TableLayout)findViewById(R.id.MineField);
        tile = new Tiles[totalRows][totalColumns];
        createGame();
        if(savedInstanceState == null){
            setUpMines();
        }else{
            try {
                setUpMinesAfterRestart(savedInstanceState);
                unCoverTilesAfterRestart(savedInstanceState);
                System.out.println(" the value of " + savedInstanceState.getBoolean(GAME_OVER_FLAG));
                if(savedInstanceState.getBoolean(GAME_OVER_FLAG)){
                    gameOverFlag = true;
                    endGame();
                    storeCoordinatesMineClicked = savedInstanceState.getStringArrayList(COORDINATES_MINE_CLICKED);
                    String temp[] = storeCoordinatesMineClicked.get(0).split(",");
                    tile[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])].setBackgroundResource(R.drawable.clickedbomb);

                }

                totalTiles = savedInstanceState.getInt(TOTAL_TILES);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void unCoverTilesAfterRestart(Bundle savedInstanceState) {
        ArrayList<String> getCoveredTilesCoords = savedInstanceState.getStringArrayList(COORDINATES_COVERED);
        for(String coord : getCoveredTilesCoords) {
            String temp[] = coord.split(",");
            int curRow = Integer.parseInt(temp[0]);
            int curCol = Integer.parseInt(temp[1]);
            unCoverTiles(curRow,curCol);
        }
    }

    private void createGame(){
        for(int rows = 0 ; rows< totalRows; rows++){
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams((tileWH*padding)*totalColumns,tileWH*padding));
            for(int columns=0; columns < totalColumns; columns++){
                tile[rows][columns] = new Tiles(this);
                tile[rows][columns].setBackgroundResource(R.drawable.tile);
                tile[rows][columns].setLayoutParams(new TableRow.LayoutParams(tileWH * padding, tileWH * padding));
                row.addView(tile[rows][columns]);
                final int tRows = rows;
                final int tColumns = columns;
                tile[rows][columns].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isGameWon()) {
                            gameOverFlag = true;
                            endGame();
                            popUp.putExtra(PopUp.GAME_STATE, "Game WON!!!");
                            startActivityForResult(popUp, 0);
                        }
                        else {
                            if (tile[tRows][tColumns].isMineSet()) {

                                endGame();
                                if (!(gameOverFlag)) {
                                    tile[tRows][tColumns].setBackgroundResource(R.drawable.clickedbomb);
                                    storeCoordinatesMineClicked.add(tRows + "," + tColumns);
                                }
                                gameOverFlag = true;
                                popUp.putExtra(PopUp.GAME_STATE, "Game Over");
                                startActivityForResult(popUp, 0);

                            } else if (!(tile[tRows][tColumns].isCovered())) {
                                unCoverTiles(tRows, tColumns);
                            }
                        }
                    }
                });

            }

            minefield.addView(row, new TableLayout.LayoutParams((tileWH * padding) * totalColumns, tileWH * padding));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        userOption = data.getStringExtra(PopUp.USER_OPTION);
        if(userOption.equals("restart")){
            finish();
            startActivity(new Intent(this,Minesweeper.class));
        }else if(userOption.equals("exit")){
            finish();
        }
    }

    private void setUpMines(){
        int minRow = 0;
        int maxRow = 2;
        int minColumn = 0;
        int maxColumn = 2;
        for(int i=0; i < 30;i++) {
            int randomRow = ThreadLocalRandom.current().nextInt(0, 11);
            int randomColumn = ThreadLocalRandom.current().nextInt(0, 7);


            if (!(tile[randomRow][randomColumn].isMineSet())) {
                storeCoordinatesMine.add(randomRow+","+randomColumn);
                tile[randomRow][randomColumn].setMine(true);
                tile[randomRow][randomColumn].noSurroundingMines = 0;
                if (randomRow == 0) {
                    minRow = 0;
                    maxRow = 1;
                }
                if (randomColumn == 0) {
                    minColumn = 0;
                    maxColumn = 1;
                }
                if (randomRow > 0 && randomRow < totalRows - 1) {
                    minRow = randomRow - 1;
                    maxRow = randomRow + 1;
                }
                if (randomColumn > 0 && randomColumn < totalColumns - 1) {
                    minColumn = randomColumn - 1;
                    maxColumn = randomColumn + 1;
                }
                if (randomRow == totalRows - 1) {
                    minRow = randomRow - 1;
                    maxRow = randomRow;
                }
                if (randomColumn == totalColumns - 1) {
                    minColumn = randomColumn - 1;
                    maxColumn = randomColumn;
                }
                for (int k = minRow; k <= maxRow; k++) {
                    for (int l = minColumn; l <= maxColumn; l++) {
                        if ((k != randomRow && l != randomColumn) || !(tile[k][l].isMineSet())) {
                            tile[k][l].updateMines();
                        }
                    }
                }
            } else {
                --i;
            }

        }

    }

    private void unCoverTiles(int i , int j){
        int curRow = i;
        int curCol = j;
        int minRow = 0;
        int minColumn = 0;
        int maxRow = 2;
        int maxColumn = 2;
        if(tile[curRow][curCol].isMineSet()){
            return;
        }
        tile[curRow][curCol].showTile();
        storeCoordinatesCovered.add(curRow+","+curCol);
            totalTiles--;

        if(tile[curRow][curCol].getNoSurroundingMines() > 0){
            return;
        }

        if (curRow == 0) {
            minRow = 0;
            maxRow = 1;
        }
        if (curCol == 0) {
            minColumn = 0;
            maxColumn = 1;
        }
        if (curRow > 0 && curRow < totalRows - 1) {
            minRow = curRow - 1;
            maxRow = curRow + 1;
        }
        if (curCol > 0 && curCol < totalColumns - 1) {
            minColumn = curCol - 1;
            maxColumn = curCol + 1;
        }
        if (curRow == totalRows - 1) {
            minRow = curRow - 1;
            maxRow = curRow;
        }
        if (curCol == totalColumns - 1) {
            minColumn = curCol - 1;
            maxColumn = curCol;

        }

        for (int k = minRow; k <= maxRow; k++) {
            for (int l = minColumn; l <= maxColumn; l++) {
                if(!(tile[k][l].isCovered())) {
                    unCoverTiles(k, l);
                }
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void endGame(){
        for(int minRows = 0 ; minRows < totalRows ; minRows++){
            for(int minColumns = 0; minColumns < totalColumns;minColumns++ ){
                if(!(tile[minRows][minColumns].isCovered()))
                    tile[minRows][minColumns].showTile();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if(!(gameOverFlag))
                    popUp.putExtra(PopUp.GAME_STATE, "Game Paused");
                else{
                    popUp.putExtra(PopUp.GAME_STATE, "Game Over");
                }
                startActivityForResult(popUp, 0);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList(COORDINATES_MINE, storeCoordinatesMine);
        savedInstanceState.putStringArrayList(COORDINATES_COVERED,storeCoordinatesCovered);
        savedInstanceState.putInt(TOTAL_TILES, totalTiles);
        savedInstanceState.putBoolean(GAME_OVER_FLAG,gameOverFlag);
        savedInstanceState.putStringArrayList(COORDINATES_MINE_CLICKED,storeCoordinatesMineClicked);
    }

    public boolean isGameWon(){
        return (totalTiles == 0);
    }

    private void setUpMinesAfterRestart(Bundle savedInstanceState) throws Exception{

            int minRow = 0;
            int maxRow = 2;
            int minColumn = 0;
            int maxColumn = 2;
            ArrayList<String> getMineCoords = savedInstanceState.getStringArrayList(COORDINATES_MINE);
            for(String coord : getMineCoords){
                String temp[] = coord.split(",");
                int randomRow = Integer.parseInt(temp[0]);
                int randomColumn = Integer.parseInt(temp[1]);
                if (!(tile[randomRow][randomColumn].isMineSet())) {

                    storeCoordinatesMine.add(randomRow + "," + randomColumn);
                    tile[randomRow][randomColumn].setMine(true);

                    tile[randomRow][randomColumn].noSurroundingMines = 0;
                    if (randomRow == 0) {
                        minRow = 0;
                        maxRow = 1;
                    }
                    if (randomColumn == 0) {
                        minColumn = 0;
                        maxColumn = 1;
                    }
                    if (randomRow > 0 && randomRow < totalRows - 1) {
                        minRow = randomRow - 1;
                        maxRow = randomRow + 1;
                    }
                    if (randomColumn > 0 && randomColumn < totalColumns - 1) {
                        minColumn = randomColumn - 1;
                        maxColumn = randomColumn + 1;
                    }
                    if (randomRow == totalRows - 1) {
                        minRow = randomRow - 1;
                        maxRow = randomRow;
                    }
                    if (randomColumn == totalColumns - 1) {
                        minColumn = randomColumn - 1;
                        maxColumn = randomColumn;

                    }

                    for (int k = minRow; k <= maxRow; k++) {
                        for (int l = minColumn; l <= maxColumn; l++) {
                            if ((k != randomRow && l != randomColumn) || !(tile[k][l].isMineSet())) {
                                tile[k][l].updateMines();
                            }
                        }
                    }
                }


            }

    }


}

