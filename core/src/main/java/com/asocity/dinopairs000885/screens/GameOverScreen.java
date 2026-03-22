package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.dinopairs000885.Constants;
import com.asocity.dinopairs000885.MainGame;
import com.asocity.dinopairs000885.UiFactory;

/**
 * Game Over screen.
 *
 * @param score     final score for this run
 * @param boardSize board dimension (4, 6, or 8) — used for Retry and Change Size navigation
 */
public class GameOverScreen implements Screen {

    private static final String BG = "ui/game_over_screen.png";

    private final MainGame game;
    private final int      score;
    private final int      boardSize;
    private final Stage    stage;
    private final StretchViewport viewport;

    public GameOverScreen(MainGame game, int score, int extra) {
        this.game      = game;
        this.score     = score;
        this.boardSize = extra;

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        buildStage();
        registerInput();

        game.playMusicOnce("sounds/music/music_game_over.ogg");
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", Sound.class).play(1.0f);
    }

    private void buildStage() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore, null);

        // ── GAME OVER label — top-Y=80, h=70 → libgdxY=704 ───────────────
        Label titleLbl = new Label("GAME OVER", titleStyle);
        titleLbl.setAlignment(Align.center);
        titleLbl.setSize(360f, 70f);
        titleLbl.setPosition((Constants.WORLD_WIDTH - 360f) / 2f, 704f);
        stage.addActor(titleLbl);

        // ── Score display — top-Y=280, h=60 → libgdxY=514 ────────────────
        Label scoreLbl = new Label("SCORE: " + score, scoreStyle);
        scoreLbl.setAlignment(Align.center);
        scoreLbl.setSize(300f, 60f);
        scoreLbl.setPosition((Constants.WORLD_WIDTH - 300f) / 2f, 514f);
        stage.addActor(scoreLbl);

        // ── Personal best ─────────────────────────────────────────────────
        Preferences prefs    = Gdx.app.getPreferences(Constants.PREFS_NAME);
        String      prefKey  = bestKey(boardSize);
        int         prevBest = prefs.getInteger(prefKey, 0);
        if (score > prevBest) {
            prefs.putInteger(prefKey, score);
            prefs.flush();
        }
        int best = Math.max(score, prevBest);

        Label.LabelStyle bodyStyle = new Label.LabelStyle(game.fontBody, null);
        Label bestLbl = new Label("BEST: " + best, bodyStyle);
        bestLbl.setAlignment(Align.center);
        bestLbl.setSize(300f, 44f);
        bestLbl.setPosition((Constants.WORLD_WIDTH - 300f) / 2f, 462f);
        stage.addActor(bestLbl);

        // ── TRY AGAIN — top-Y=520, h=64 → libgdxY=270 ────────────────────
        TextButton retryBtn = UiFactory.makeButton("TRY AGAIN", rectStyle, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        retryBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 270f);
        retryBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(newGameScreen());
            }
        });
        stage.addActor(retryBtn);

        // ── CHANGE SIZE — top-Y=600, h=56 → libgdxY=198 ──────────────────
        TextButton changeBtn = UiFactory.makeButton("CHANGE SIZE", rectStyle, Constants.BTN_W_STD, Constants.BTN_H_STD);
        changeBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 198f);
        changeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new SizeSelectScreen(game));
            }
        });
        stage.addActor(changeBtn);

        // ── MENU — top-Y=672, h=56 → libgdxY=126 ─────────────────────────
        TextButton menuBtn = UiFactory.makeButton("MENU", rectStyle, Constants.BTN_W_STD, Constants.BTN_H_STD);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 126f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(menuBtn);
    }

    /** Create a fresh game screen for the same board size. */
    private com.badlogic.gdx.Screen newGameScreen() {
        switch (boardSize) {
            case Constants.BOARD_JUVENILE: return new JuvenileScreen(game);
            case Constants.BOARD_ALPHA:    return new AlphaScreen(game);
            default:                       return new HatchlingScreen(game);
        }
    }

    private String bestKey(int size) {
        switch (size) {
            case Constants.BOARD_JUVENILE: return "bestScore_juvenile";
            case Constants.BOARD_ALPHA:    return "bestScore_alpha";
            default:                       return "bestScore_hatchling";
        }
    }

    private void registerInput() {
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", Sound.class).play(1.0f);
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────

    @Override public void show() { registerInput(); }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(game.manager.get(BG, Texture.class),
                0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
