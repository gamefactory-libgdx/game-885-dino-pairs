package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

public class MainMenuScreen implements Screen {

    private static final String BG = "ui/main_menu_screen.png";

    private final MainGame game;
    private final Stage    stage;
    private final StretchViewport viewport;

    public MainMenuScreen(MainGame game) {
        this.game = game;

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        buildStage();
        registerInput();

        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void buildStage() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // ── Title label ────────────────────────────────────────────────────
        // top-Y=80, height=90 → libgdxY = 854 - 80 - 90 = 684
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label title = new Label("DINO PAIRS", titleStyle);
        title.setAlignment(Align.center);
        title.setSize(360f, 90f);
        title.setPosition((Constants.WORLD_WIDTH - 360f) / 2f, 684f);
        stage.addActor(title);

        // ── PLAY — top-Y=420, h=64 → libgdxY=370 ─────────────────────────
        TextButton playBtn = UiFactory.makeButton("PLAY", rectStyle, Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        playBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 370f);
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new SizeSelectScreen(game));
            }
        });
        stage.addActor(playBtn);

        // ── LEADERBOARD — top-Y=502, h=56 → libgdxY=296 ──────────────────
        TextButton lbBtn = UiFactory.makeButton("LEADERBOARD", rectStyle, Constants.BTN_W_STD, Constants.BTN_H_STD);
        lbBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 296f);
        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new LeaderboardScreen(game));
            }
        });
        stage.addActor(lbBtn);

        // ── STATS — top-Y=572, h=56 → libgdxY=226 ────────────────────────
        TextButton statsBtn = UiFactory.makeButton("STATS", rectStyle, Constants.BTN_W_STD, Constants.BTN_H_STD);
        statsBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 226f);
        statsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new StatsScreen(game));
            }
        });
        stage.addActor(statsBtn);

        // ── SETTINGS — top-Y=642, h=56 → libgdxY=156 ────────────────────
        TextButton settingsBtn = UiFactory.makeButton("SETTINGS", rectStyle, Constants.BTN_W_STD, Constants.BTN_H_STD);
        settingsBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 156f);
        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                game.setScreen(new SettingsScreen(game));
            }
        });
        stage.addActor(settingsBtn);
    }

    private void registerInput() {
        stage.addListener(new InputListener() {
            @Override public boolean keyDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    private void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg", com.badlogic.gdx.audio.Sound.class).play(1.0f);
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────

    @Override public void show() {
        registerInput();
        game.playMusic("sounds/music/music_menu.ogg");
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

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
