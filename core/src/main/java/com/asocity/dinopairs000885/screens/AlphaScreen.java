package com.asocity.dinopairs000885.screens;

import com.asocity.dinopairs000885.Constants;
import com.asocity.dinopairs000885.MainGame;

/** 8×8 memory-match board (Alpha difficulty). */
public class AlphaScreen extends BaseGameScreen {

    public AlphaScreen(MainGame game) {
        super(game,
              Constants.BOARD_ALPHA,
              Constants.ALPHA_BOARD_X,
              Constants.ALPHA_BOARD_Y,
              Constants.ALPHA_BOARD_W,
              Constants.ALPHA_BOARD_H,
              Constants.ALPHA_CARD_PADDING,
              Constants.TIMER_ALPHA);
    }

    @Override protected String getBgPath()       { return "ui/alpha_screen.png"; }
    @Override protected String getDifficultyKey() { return "A"; }
    @Override protected int    getStar3Moves()   { return Constants.STAR_3_MOVES_ALPHA; }
    @Override protected int    getStar2Moves()   { return Constants.STAR_2_MOVES_ALPHA; }
    @Override protected float  getBottomPausePad() { return 14f; }
}
