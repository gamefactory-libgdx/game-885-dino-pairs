package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class PauseScreen implements Screen {

    // No dedicated pause UI image in manifest — reuse main menu background
    private static final String BG = "ui/main_menu_screen.png";

    private final MainGame game;
    private final Screen   previousScreen;
    private Stage          stage;

    public PauseScreen(MainGame game, Screen previousScreen) {
        this.game           = game;
        this.previousScreen = previousScreen;

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        OrthographicCamera camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera), game.batch);

        buildUi();
        registerInput();

        // Pause music
        if (game.currentMusic != null) game.currentMusic.pause();
    }

    private void buildUi() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label title = new Label("PAUSED", titleStyle);
        title.setPosition((Constants.WORLD_WIDTH - title.getPrefWidth()) / 2f, 620f);
        stage.addActor(title);

        // RESUME — center ~500
        TextButton resumeBtn = UiFactory.makeButton("RESUME", rectStyle,
                                                    Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        resumeBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 490f);
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                if (game.musicEnabled && game.currentMusic != null) game.currentMusic.play();
                game.setScreen(previousScreen);
            }
        });

        // RESTART — center ~390
        TextButton restartBtn = UiFactory.makeButton("RESTART", rectStyle,
                                                     Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        restartBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 390f);
        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(newGameScreen());
            }
        });

        // MAIN MENU — center ~290
        TextButton menuBtn = UiFactory.makeButton("MAIN MENU", rectStyle,
                                                  Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 290f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(resumeBtn);
        stage.addActor(restartBtn);
        stage.addActor(menuBtn);
    }

    private Screen newGameScreen() {
        if (previousScreen instanceof HatchlingScreen) return new HatchlingScreen(game);
        if (previousScreen instanceof JuvenileScreen)  return new JuvenileScreen(game);
        return new AlphaScreen(game);
    }

    private void registerInput() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.BACK) {
                    if (game.musicEnabled && game.currentMusic != null) game.currentMusic.play();
                    game.setScreen(previousScreen);
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
