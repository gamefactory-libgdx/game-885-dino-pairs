package com.asocity.dinopairs000885.screens;

import com.asocity.dinopairs000885.Constants;
import com.asocity.dinopairs000885.MainGame;

/** 6×6 memory-match board (Juvenile difficulty). */
public class JuvenileScreen extends BaseGameScreen {

    public JuvenileScreen(MainGame game) {
        super(game,
              Constants.BOARD_JUVENILE,
              Constants.JUVENILE_BOARD_X,
              Constants.JUVENILE_BOARD_Y,
              Constants.JUVENILE_BOARD_W,
              Constants.JUVENILE_BOARD_H,
              Constants.JUVENILE_CARD_PADDING,
              Constants.TIMER_JUVENILE);
    }

    @Override protected String getBgPath()       { return "ui/juvenile_screen.png"; }
    @Override protected String getDifficultyKey() { return "J"; }
    @Override protected int    getStar3Moves()   { return Constants.STAR_3_MOVES_JUVENILE; }
    @Override protected int    getStar2Moves()   { return Constants.STAR_2_MOVES_JUVENILE; }
    @Override protected float  getBottomPausePad() { return 22f; }
}
