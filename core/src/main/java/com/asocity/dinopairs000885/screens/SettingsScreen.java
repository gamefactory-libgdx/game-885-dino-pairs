package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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

public class SettingsScreen implements Screen {

    private static final String BG = "ui/settings_screen.png";

    private final MainGame    game;
    private final Stage       stage;
    private final StretchViewport viewport;
    private final Preferences prefs;

    // Toggle state
    private boolean vibrationEnabled;
    private boolean hapticsEnabled;

    // Toggle buttons — kept as fields to update labels
    private TextButton musicToggle;
    private TextButton sfxToggle;
    private TextButton vibToggle;
    private TextButton hapToggle;

    public SettingsScreen(MainGame game) {
        this.game = game;

        prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        vibrationEnabled = prefs.getBoolean(Constants.PREF_VIBRATION, true);
        hapticsEnabled   = prefs.getBoolean(Constants.PREF_HAPTICS,   true);

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        buildStage();
        registerInput();
    }

    private void buildStage() {
        TextButton.TextButtonStyle rectStyle  = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontSmall);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);

        // ── SETTINGS title — top-Y=40, h=60 → libgdxY=754 ───────────────
        Label titleLbl = new Label("SETTINGS", titleStyle);
        titleLbl.setAlignment(Align.center);
        titleLbl.setSize(320f, 60f);
        titleLbl.setPosition((Constants.WORLD_WIDTH - 320f) / 2f, 754f);
        stage.addActor(titleLbl);

        // ── MUSIC row — top-Y=180, h=44 → libgdxY=630 ───────────────────
        Label musicLbl = new Label("MUSIC", bodyStyle);
        musicLbl.setSize(180f, 44f);
        musicLbl.setPosition(40f, 630f);
        stage.addActor(musicLbl);

        musicToggle = new TextButton(game.musicEnabled ? "ON" : "OFF", roundStyle);
        musicToggle.setSize(100f, 44f);
        musicToggle.setPosition(Constants.WORLD_WIDTH - 40f - 100f, 630f);
        musicToggle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.musicEnabled = !game.musicEnabled;
                prefs.putBoolean(Constants.PREF_MUSIC, game.musicEnabled);
                prefs.flush();
                musicToggle.setText(game.musicEnabled ? "ON" : "OFF");
                if (game.currentMusic != null) {
                    if (game.musicEnabled) game.currentMusic.play();
                    else game.currentMusic.pause();
                }
                playToggle();
            }
        });
        stage.addActor(musicToggle);

        // ── SFX row — top-Y=250, h=44 → libgdxY=560 ─────────────────────
        Label sfxLbl = new Label("SFX", bodyStyle);
        sfxLbl.setSize(180f, 44f);
        sfxLbl.setPosition(40f, 560f);
        stage.addActor(sfxLbl);

        sfxToggle = new TextButton(game.sfxEnabled ? "ON" : "OFF", roundStyle);
        sfxToggle.setSize(100f, 44f);
        sfxToggle.setPosition(Constants.WORLD_WIDTH - 40f - 100f, 560f);
        sfxToggle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.sfxEnabled = !game.sfxEnabled;
                prefs.putBoolean(Constants.PREF_SFX, game.sfxEnabled);
                prefs.flush();
                sfxToggle.setText(game.sfxEnabled ? "ON" : "OFF");
                playToggle();
            }
        });
        stage.addActor(sfxToggle);

        // ── VIBRATION row — top-Y=320, h=44 → libgdxY=490 ───────────────
        Label vibLbl = new Label("VIBRATION", bodyStyle);
        vibLbl.setSize(180f, 44f);
        vibLbl.setPosition(40f, 490f);
        stage.addActor(vibLbl);

        vibToggle = new TextButton(vibrationEnabled ? "ON" : "OFF", roundStyle);
        vibToggle.setSize(100f, 44f);
        vibToggle.setPosition(Constants.WORLD_WIDTH - 40f - 100f, 490f);
        vibToggle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                vibrationEnabled = !vibrationEnabled;
                prefs.putBoolean(Constants.PREF_VIBRATION, vibrationEnabled);
                prefs.flush();
                vibToggle.setText(vibrationEnabled ? "ON" : "OFF");
                playToggle();
            }
        });
        stage.addActor(vibToggle);

        // ── HAPTICS row — top-Y=390, h=44 → libgdxY=420 ─────────────────
        Label hapLbl = new Label("HAPTICS", bodyStyle);
        hapLbl.setSize(180f, 44f);
        hapLbl.setPosition(40f, 420f);
        stage.addActor(hapLbl);

        hapToggle = new TextButton(hapticsEnabled ? "ON" : "OFF", roundStyle);
        hapToggle.setSize(100f, 44f);
        hapToggle.setPosition(Constants.WORLD_WIDTH - 40f - 100f, 420f);
        hapToggle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                hapticsEnabled = !hapticsEnabled;
                prefs.putBoolean(Constants.PREF_HAPTICS, hapticsEnabled);
                prefs.flush();
                hapToggle.setText(hapticsEnabled ? "ON" : "OFF");
                playToggle();
            }
        });
        stage.addActor(hapToggle);

        // ── BACK — top-Y=760, h=52 → libgdxY=42 ─────────────────────────
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle, Constants.BTN_W_STD, 52f);
        backBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 42f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
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

    private void playToggle() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_toggle.ogg", Sound.class).play(0.5f);
    }

    private void playBack() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
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
