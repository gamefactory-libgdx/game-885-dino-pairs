package com.asocity.dinopairs000885.screens;

import com.asocity.dinopairs000885.Constants;
import com.asocity.dinopairs000885.MainGame;

/** 4×4 memory-match board (Hatchling difficulty). */
public class HatchlingScreen extends BaseGameScreen {

    public HatchlingScreen(MainGame game) {
        super(game,
              Constants.BOARD_HATCHLING,
              Constants.HATCHLING_BOARD_X,
              Constants.HATCHLING_BOARD_Y,
              Constants.HATCHLING_BOARD_W,
              Constants.HATCHLING_BOARD_H,
              Constants.HATCHLING_CARD_PADDING,
              Constants.TIMER_HATCHLING);
    }

    @Override protected String getBgPath()       { return "ui/hatchling_screen.png"; }
    @Override protected String getDifficultyKey() { return "H"; }
    @Override protected int    getStar3Moves()   { return Constants.STAR_3_MOVES_HATCHLING; }
    @Override protected int    getStar2Moves()   { return Constants.STAR_2_MOVES_HATCHLING; }
    @Override protected float  getBottomPausePad() { return 32f; }
}
