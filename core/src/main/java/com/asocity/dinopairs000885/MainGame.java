package com.asocity.dinopairs000885;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.asocity.dinopairs000885.screens.MainMenuScreen;

public class MainGame extends Game {

    public SpriteBatch  batch;
    public AssetManager manager;

    // Fonts
    public BitmapFont fontTitle;   // DPComic.ttf — large titles
    public BitmapFont fontBody;    // OrangeKid.otf — buttons / body text
    public BitmapFont fontSmall;   // OrangeKid.otf — small labels
    public BitmapFont fontScore;   // DPComic.ttf — score / large numbers
    public BitmapFont fontHud;     // OrangeKid.otf — HUD (score/timer/moves)

    // Audio state
    public boolean musicEnabled = true;
    public boolean sfxEnabled   = true;
    public Music   currentMusic = null;

    @Override
    public void create() {
        batch   = new SpriteBatch();
        manager = new AssetManager();

        // Load user preferences
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
        musicEnabled = prefs.getBoolean(Constants.PREF_MUSIC, true);
        sfxEnabled   = prefs.getBoolean(Constants.PREF_SFX,   true);

        generateFonts();
        loadAssets();
        manager.finishLoading();

        setScreen(new MainMenuScreen(this));
    }

    // ── Font generation ────────────────────────────────────────────────────

    private void generateFonts() {
        FreeTypeFontGenerator titleGen = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/DPComic.ttf"));
        FreeTypeFontGenerator bodyGen  = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/OrangeKid.otf"));

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.borderColor = new Color(0f, 0f, 0f, 0.85f);

        // fontTitle — large title text
        p.size        = Constants.FONT_SIZE_TITLE;
        p.borderWidth = 3;
        fontTitle = titleGen.generateFont(p);

        // fontScore — big score numbers
        p.size        = Constants.FONT_SIZE_SCORE;
        p.borderWidth = 3;
        fontScore = titleGen.generateFont(p);

        // fontBody — buttons and body
        p.size        = Constants.FONT_SIZE_BODY;
        p.borderWidth = 2;
        fontBody = bodyGen.generateFont(p);

        // fontHud — HUD labels
        p.size        = Constants.FONT_SIZE_HUD;
        p.borderWidth = 2;
        fontHud = bodyGen.generateFont(p);

        // fontSmall — small labels
        p.size        = Constants.FONT_SIZE_SMALL;
        p.borderWidth = 1;
        fontSmall = bodyGen.generateFont(p);

        titleGen.dispose();
        bodyGen.dispose();
    }

    // ── Asset loading ──────────────────────────────────────────────────────

    private void loadAssets() {
        // Button sprites
        manager.load("ui/buttons/button_rectangle_depth_gradient.png", com.badlogic.gdx.graphics.Texture.class);
        manager.load("ui/buttons/button_rectangle_depth_flat.png",     com.badlogic.gdx.graphics.Texture.class);
        manager.load("ui/buttons/button_round_depth_gradient.png",     com.badlogic.gdx.graphics.Texture.class);
        manager.load("ui/buttons/button_round_depth_flat.png",         com.badlogic.gdx.graphics.Texture.class);
        manager.load("ui/buttons/star.png",                            com.badlogic.gdx.graphics.Texture.class);
        manager.load("ui/buttons/star_outline.png",                    com.badlogic.gdx.graphics.Texture.class);

        // Music
        manager.load("sounds/music/music_menu.ogg",      Music.class);
        manager.load("sounds/music/music_gameplay.ogg",  Music.class);
        manager.load("sounds/music/music_game_over.ogg", Music.class);

        // SFX
        manager.load("sounds/sfx/sfx_button_click.ogg",   Sound.class);
        manager.load("sounds/sfx/sfx_button_back.ogg",    Sound.class);
        manager.load("sounds/sfx/sfx_toggle.ogg",         Sound.class);
        manager.load("sounds/sfx/sfx_coin.ogg",           Sound.class);
        manager.load("sounds/sfx/sfx_hit.ogg",            Sound.class);
        manager.load("sounds/sfx/sfx_game_over.ogg",      Sound.class);
        manager.load("sounds/sfx/sfx_level_complete.ogg", Sound.class);
        manager.load("sounds/sfx/sfx_power_up.ogg",       Sound.class);
    }

    // ── Music helpers ──────────────────────────────────────────────────────

    /** Play a looping music track — safe to call repeatedly (same-track guard). */
    public void playMusic(String path) {
        Music requested = manager.get(path, Music.class);
        if (requested == currentMusic && currentMusic.isPlaying()) return;
        if (currentMusic != null) currentMusic.stop();
        currentMusic = requested;
        currentMusic.setLooping(true);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    /** Play a one-shot music track (game over jingle — never loops). */
    public void playMusicOnce(String path) {
        if (currentMusic != null) currentMusic.stop();
        currentMusic = manager.get(path, Music.class);
        currentMusic.setLooping(false);
        currentMusic.setVolume(0.7f);
        if (musicEnabled) currentMusic.play();
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        manager.dispose();
        fontTitle.dispose();
        fontBody.dispose();
        fontSmall.dispose();
        fontScore.dispose();
        fontHud.dispose();
    }
}
