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

public class LeaderboardScreen implements Screen {

    private static final String BG = "ui/leaderboard_screen.png";

    // ── Static API ────────────────────────────────────────────────────────

    /**
     * Add a score for the default (Hatchling) board.
     * Convenience wrapper — same as addScore(score, Constants.BOARD_HATCHLING).
     */
    public static void addScore(int score) {
        addScore(score, Constants.BOARD_HATCHLING);
    }

    /**
     * Add a score for the given board size (4, 6, or 8).
     * Maintains a top-10 list in SharedPreferences for each board.
     */
    public static void addScore(int score, int boardSize) {
        Preferences prefs   = Gdx.app.getPreferences(Constants.PREFS_NAME);
        String      key     = prefKey(boardSize);
        String      stored  = prefs.getString(key, "");
        int[]       entries = parseScores(stored);

        // Insert new score and keep top-10
        int[] updated = insertScore(entries, score, Constants.LEADERBOARD_MAX_ENTRIES);
        prefs.putString(key, encodeScores(updated));
        prefs.flush();
    }

    // ── Instance ──────────────────────────────────────────────────────────

    private final MainGame game;
    private final Stage    stage;
    private final StretchViewport viewport;

    // Currently displayed tab (4, 6, or 8)
    private int activeTab = Constants.BOARD_HATCHLING;

    // Score row labels (10 entries)
    private final Label[] rowLabels = new Label[Constants.LEADERBOARD_MAX_ENTRIES];

    public LeaderboardScreen(MainGame game) {
        this.game = game;

        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage    = new Stage(viewport, game.batch);

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        buildStage();
        registerInput();
        refreshRows();

        game.playMusic("sounds/music/music_menu.ogg");
    }

    private void buildStage() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);
        TextButton.TextButtonStyle tabStyle  = UiFactory.makeRoundStyle(game.manager, game.fontSmall);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, null);
        Label.LabelStyle bodyStyle  = new Label.LabelStyle(game.fontBody,  null);
        Label.LabelStyle rowStyle   = new Label.LabelStyle(game.fontSmall, null);

        // ── LEADERBOARD title — top-Y=40, h=60 → libgdxY=754 ─────────────
        Label titleLbl = new Label("LEADERBOARD", titleStyle);
        titleLbl.setAlignment(Align.center);
        titleLbl.setSize(360f, 60f);
        titleLbl.setPosition((Constants.WORLD_WIDTH - 360f) / 2f, 754f);
        stage.addActor(titleLbl);

        // ── Tabs — top-Y=110, h=44 → libgdxY=700 ─────────────────────────
        // TAB: HATCHLING — x=left@20
        TextButton tabH = UiFactory.makeButton("4x4", tabStyle, 140f, 44f);
        tabH.setPosition(20f, 700f);
        tabH.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                activeTab = Constants.BOARD_HATCHLING;
                refreshRows();
            }
        });
        stage.addActor(tabH);

        // TAB: JUVENILE — centered
        TextButton tabJ = UiFactory.makeButton("6x6", tabStyle, 140f, 44f);
        tabJ.setPosition((Constants.WORLD_WIDTH - 140f) / 2f, 700f);
        tabJ.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                activeTab = Constants.BOARD_JUVENILE;
                refreshRows();
            }
        });
        stage.addActor(tabJ);

        // TAB: ALPHA — x=right@20
        TextButton tabA = UiFactory.makeButton("8x8", tabStyle, 140f, 44f);
        tabA.setPosition(Constants.WORLD_WIDTH - 20f - 140f, 700f);
        tabA.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playClick();
                activeTab = Constants.BOARD_ALPHA;
                refreshRows();
            }
        });
        stage.addActor(tabA);

        // ── Score rows — top-Y=170 → libgdxY=684 top of first row ────────
        // 10 rows, each 44px tall, spacing 4px → total 480px
        // rows fill from libgdxY=684 down to ~204
        float rowH       = 44f;
        float rowSpacing = 4f;
        float startY     = 684f - rowH; // bottom of first row

        for (int i = 0; i < Constants.LEADERBOARD_MAX_ENTRIES; i++) {
            Label lbl = new Label("", rowStyle);
            lbl.setAlignment(Align.center);
            lbl.setSize(440f, rowH);
            lbl.setPosition((Constants.WORLD_WIDTH - 440f) / 2f,
                    startY - i * (rowH + rowSpacing));
            stage.addActor(lbl);
            rowLabels[i] = lbl;
        }

        // ── BACK — top-Y=790, h=48 → libgdxY=16 ──────────────────────────
        TextButton backBtn = UiFactory.makeButton("BACK", rectStyle, Constants.BTN_W_STD, 48f);
        backBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 16f);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                playBack();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backBtn);
    }

    /** Populate the 10 row labels from SharedPreferences for the active tab. */
    private void refreshRows() {
        Preferences prefs  = Gdx.app.getPreferences(Constants.PREFS_NAME);
        String      stored = prefs.getString(prefKey(activeTab), "");
        int[]       scores = parseScores(stored);

        for (int i = 0; i < rowLabels.length; i++) {
            if (i < scores.length && scores[i] > 0) {
                rowLabels[i].setText((i + 1) + ".   " + scores[i]);
            } else {
                rowLabels[i].setText((i + 1) + ".   ---");
            }
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

    private void playBack() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_back.ogg", Sound.class).play(1.0f);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static String prefKey(int boardSize) {
        switch (boardSize) {
            case Constants.BOARD_JUVENILE: return Constants.PREF_LB_JUVENILE;
            case Constants.BOARD_ALPHA:    return Constants.PREF_LB_ALPHA;
            default:                       return Constants.PREF_LB_HATCHLING;
        }
    }

    /** Parse a comma-separated score list. */
    private static int[] parseScores(String stored) {
        if (stored == null || stored.isEmpty()) return new int[0];
        String[] parts = stored.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try { result[i] = Integer.parseInt(parts[i].trim()); }
            catch (NumberFormatException e) { result[i] = 0; }
        }
        return result;
    }

    /** Insert score into sorted (descending) array and trim to maxEntries. */
    private static int[] insertScore(int[] existing, int newScore, int maxEntries) {
        int len = existing.length;
        int[] merged = new int[len + 1];
        System.arraycopy(existing, 0, merged, 0, len);
        merged[len] = newScore;

        // Bubble sort descending (simple — max 10 elements)
        for (int i = 0; i < merged.length - 1; i++) {
            for (int j = 0; j < merged.length - 1 - i; j++) {
                if (merged[j] < merged[j + 1]) {
                    int tmp = merged[j]; merged[j] = merged[j + 1]; merged[j + 1] = tmp;
                }
            }
        }

        int size = Math.min(merged.length, maxEntries);
        int[] result = new int[size];
        System.arraycopy(merged, 0, result, 0, size);
        return result;
    }

    /** Encode int array as comma-separated string. */
    private static String encodeScores(int[] scores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scores.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(scores[i]);
        }
        return sb.toString();
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────

    @Override public void show() {
        registerInput();
        refreshRows();
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
