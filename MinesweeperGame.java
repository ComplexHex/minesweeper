package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;

    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";

    private int countFlags;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);

            }
        }
        countMineNeighbors();
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                System.out.print(gameField[i][j].isMine + " ");
            }
            System.out.println();
        }
        countFlags = countMinesOnField;
    }

    //получение списка соседей
    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    // для каждой ячейки "не мины" из матрицы gameField подсчитать количество соседних ячеек "мин"
    // и установить это значение в поле countMineNeighbors
    private void countMineNeighbors() {

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (!gameField[y][x].isMine) {
                    int count = 0;
                    List<GameObject> list = getNeighbors(gameField[y][x]);

                    for (GameObject gameObject : list) {
                        if (gameObject.isMine) {
                            count++;
                            System.out.println(count);
                            gameField[y][x].countMineNeighbors++;

                        }


                    }

                }


            }
        }
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                System.out.print(gameField[i][j].countMineNeighbors + " ");
            }
            System.out.println();
        }

    }

    private void openTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        gameObject.isOpen = true;
        setCellColor(x, y, Color.GREEN);
        if (gameObject.isMine) {
            setCellValue(gameObject.x, gameObject.y, MINE);
        } else if (gameObject.countMineNeighbors == 0) {

            setCellValue(x, y, "");

            List<GameObject> list = getNeighbors(gameObject);
            for (GameObject gameObject1 : list) {
                if (!gameObject1.isOpen) {
                    openTile(gameObject1.getX(), gameObject1.getY());
                }
            }
        } else {
            setCellNumber(x, y, gameObject.countMineNeighbors);
        }
    }


    @Override
    public void onMouseLeftClick(int x, int y) {
        openTile(x, y);
    }
}