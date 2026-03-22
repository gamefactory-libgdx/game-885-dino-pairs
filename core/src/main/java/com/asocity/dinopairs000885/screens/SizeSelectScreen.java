package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

public class SizeSelectScreen implements Screen {

    private static final String BG = "ui/size_select_screen.png";

    private final MainGame game;
    private Stage             stage;

    public SizeSelectScreen(MainGame game) {
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
        TextButton.TextButtonStyle wideStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // Title label
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, com.badlogic.gdx.graphics.Color.WHITE);
        Label title = new Label("CHOOSE YOUR BOARD", titleStyle);
        title.setPosition((Constants.WORLD_WIDTH - title.getPrefWidth()) / 2f,
                          Constants.WORLD_HEIGHT - 60f - 60f); // top-Y=60, h=60
        stage.addActor(title);

        // HATCHLING 4×4  — top-Y=200, h=90 → libgdxY = 854-200-90 = 564
        TextButton hatchBtn = UiFactory.makeButton("HATCHLING  4\u00d74", wideStyle,
                                                   Constants.BTN_W_WIDE, Constants.BTN_H_WIDE);
        hatchBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_WIDE) / 2f, 564f);
        hatchBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new HatchlingScreen(game));
            }
        });

        // JUVENILE 6×6 — top-Y=360, h=90 → libgdxY = 404
        TextButton juvenileBtn = UiFactory.makeButton("JUVENILE  6\u00d76", wideStyle,
                                                      Constants.BTN_W_WIDE, Constants.BTN_H_WIDE);
        juvenileBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_WIDE) / 2f, 404f);
        juvenileBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new JuvenileScreen(game));
            }
        });

        // ALPHA 8×8 — top-Y=520, h=90 → libgdxY = 244
        TextButton alphaBtn = UiFactory.makeButton("ALPHA  8\u00d78", wideStyle,
                                                   Constants.BTN_W_WIDE, Constants.BTN_H_WIDE);
        alphaBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_WIDE) / 2f, 244f);
        alphaBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new AlphaScreen(game));
            }
        });

        // BACK — top-Y=760, h=48, x=left@20 → libgdxY = 854-760-48 = 46
        TextButton backBtn = UiFactory.makeButton("BACK", wideStyle, 120f, 48f);
        backBtn.setPosition(20f, 46f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(hatchBtn);
        stage.addActor(juvenileBtn);
        stage.addActor(alphaBtn);
        stage.addActor(backBtn);
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
