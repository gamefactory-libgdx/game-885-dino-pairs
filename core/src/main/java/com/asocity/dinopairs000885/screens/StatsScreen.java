package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.dinopairs000885.Constants;
import com.asocity.dinopairs000885.MainGame;
import com.asocity.dinopairs000885.UiFactory;

public class StatsScreen implements Screen {

    private static final String BG = "ui/stats_screen.png";

    private final MainGame game;
    private Stage          stage;

    // We store labels so we can rebuild after reset
    private Label gamesPlayedLbl, bestHLbl, bestJLbl, bestALbl;
    private Label totalMatchesLbl, goldenFossilsLbl, perfectGamesLbl;

    public StatsScreen(MainGame game) {
        this.game = game;

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        OrthographicCamera camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera), game.batch);

        buildUi();
        registerInput();

        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void buildUi() {
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  Color.WHITE);
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // Title — top-Y=40, h=60 → libgdxY = 854-40-60 = 754
        Label title = new Label("STATS", titleStyle);
        title.setPosition((Constants.WORLD_WIDTH - title.getPrefWidth()) / 2f, 754f);
        stage.addActor(title);

        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);

        // Stat rows — using FIGMA top-Y values converted:
        // Each row: libgdxY = 854 - topY - 44
        gamesPlayedLbl   = makeStatLabel(bodyStyle, "GAMES PLAYED",
                                         p.getInteger(Constants.PREF_GAMES_PLAYED, 0));
        bestHLbl         = makeTimeLabelRow(bodyStyle, "BEST TIME: HATCHLING",
                                            p.getFloat(Constants.PREF_BEST_TIME_H, -1f));
        bestJLbl         = makeTimeLabelRow(bodyStyle, "BEST TIME: JUVENILE",
                                            p.getFloat(Constants.PREF_BEST_TIME_J, -1f));
        bestALbl         = makeTimeLabelRow(bodyStyle, "BEST TIME: ALPHA",
                                            p.getFloat(Constants.PREF_BEST_TIME_A, -1f));
        totalMatchesLbl  = makeStatLabel(bodyStyle, "TOTAL MATCHES",
                                         p.getInteger(Constants.PREF_TOTAL_MATCHES, 0));
        goldenFossilsLbl = makeStatLabel(bodyStyle, "GOLDEN FOSSILS",
                                         p.getInteger(Constants.PREF_GOLDEN_FOSSILS, 0));
        perfectGamesLbl  = makeStatLabel(bodyStyle, "PERFECT GAMES",
                                         p.getInteger(Constants.PREF_PERFECT_GAMES, 0));

        // Position stat rows — top-Y: 160,220,280,340,400,460,520 → subtract 44
        positionStatRow(gamesPlayedLbl,   854f - 160f - 44f);
        positionStatRow(bestHLbl,         854f - 220f - 44f);
        positionStatRow(bestJLbl,         854f - 280f - 44f);
        positionStatRow(bestALbl,         854f - 340f - 44f);
        positionStatRow(totalMatchesLbl,  854f - 400f - 44f);
        positionStatRow(goldenFossilsLbl, 854f - 460f - 44f);
        positionStatRow(perfectGamesLbl,  854f - 520f - 44f);

        stage.addActor(gamesPlayedLbl);
        stage.addActor(bestHLbl);
        stage.addActor(bestJLbl);
        stage.addActor(bestALbl);
        stage.addActor(totalMatchesLbl);
        stage.addActor(goldenFossilsLbl);
        stage.addActor(perfectGamesLbl);

        // RESET STATS — top-Y=680, h=52 → libgdxY=122
        TextButton resetBtn = UiFactory.makeButton("RESET STATS", rectStyle, 240f, 52f);
        resetBtn.setPosition((Constants.WORLD_WIDTH - 240f) / 2f, 122f);
        resetBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                resetStats();
            }
        });

        // BACK — top-Y=748, h=52 → libgdxY=54
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle,
                                                  Constants.BTN_W_STD, 52f);
        backBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 54f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(resetBtn);
        stage.addActor(backBtn);
    }

    private Label makeStatLabel(Label.LabelStyle style, String name, int value) {
        return new Label(name + ": " + value, style);
    }

    private Label makeTimeLabelRow(Label.LabelStyle style, String name, float seconds) {
        String val = (seconds < 0f) ? "--" : formatTime(seconds);
        return new Label(name + ": " + val, style);
    }

    private void positionStatRow(Label label, float y) {
        label.setPosition(30f, y);
    }

    private void resetStats() {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        p.putInteger(Constants.PREF_GAMES_PLAYED,  0);
        p.putFloat(Constants.PREF_BEST_TIME_H,     -1f);
        p.putFloat(Constants.PREF_BEST_TIME_J,     -1f);
        p.putFloat(Constants.PREF_BEST_TIME_A,     -1f);
        p.putInteger(Constants.PREF_TOTAL_MATCHES, 0);
        p.putInteger(Constants.PREF_GOLDEN_FOSSILS,0);
        p.putInteger(Constants.PREF_PERFECT_GAMES, 0);
        p.flush();

        // Refresh labels
        gamesPlayedLbl.setText("GAMES PLAYED: 0");
        bestHLbl.setText("BEST TIME: HATCHLING: --");
        bestJLbl.setText("BEST TIME: JUVENILE: --");
        bestALbl.setText("BEST TIME: ALPHA: --");
        totalMatchesLbl.setText("TOTAL MATCHES: 0");
        goldenFossilsLbl.setText("GOLDEN FOSSILS: 0");
        perfectGamesLbl.setText("PERFECT GAMES: 0");
    }

    private static String formatTime(float s) {
        int m = (int)(s / 60);
        int sec = (int)(s % 60);
        return String.format("%d:%02d", m, sec);
    }

    private void registerInput() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
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
            game.manager.get("sounds/sfx/sfx_button_click.ogg", com.badlogic.gdx.audio.Sound.class).play(1f);
    }

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

    @Override public void show()   { Gdx.input.setInputProcessor(stage); }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { stage.dispose(); }
}
