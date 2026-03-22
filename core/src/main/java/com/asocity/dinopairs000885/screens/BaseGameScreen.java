package com.asocity.dinopairs000885.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.asocity.dinopairs000885.Constants;
import com.asocity.dinopairs000885.MainGame;
import com.asocity.dinopairs000885.UiFactory;

/**
 * Abstract base for all three game-board screens (Hatchling / Juvenile / Alpha).
 * Subclasses supply: grid size, timer, board layout, and background image.
 */
public abstract class BaseGameScreen implements Screen {

    // ── Shared card face images (32 unique icons from sprites/ui/) ────────────
    protected static final String[] CARD_FACE_IMAGES = {
        "sprites/ui/heart.png",    "sprites/ui/clock.png",
        "sprites/ui/star_1.png",   "sprites/ui/star_2.png",
        "sprites/ui/star_3.png",   "sprites/ui/star_4.png",
        "sprites/ui/face.png",     "sprites/ui/ok.png",
        "sprites/ui/prize.png",    "sprites/ui/luck.png",
        "sprites/ui/level.png",    "sprites/ui/leader.png",
        "sprites/ui/down.png",     "sprites/ui/up.png",
        "sprites/ui/next.png",     "sprites/ui/prew.png",
        "sprites/ui/play.png",     "sprites/ui/pause.png",
        "sprites/ui/shop.png",     "sprites/ui/sound.png",
        "sprites/ui/setting.png",  "sprites/ui/moves.png",
        "sprites/ui/ico1.png",     "sprites/ui/ico2.png",
        "sprites/ui/lock.png",     "sprites/ui/logo.png",
        "sprites/ui/about.png",    "sprites/ui/scroll.png",
        "sprites/ui/upgrade.png",  "sprites/ui/dot.png",
        "sprites/ui/close.png",    "sprites/ui/btn.png"
    };
    protected static final String CARD_BACK = "sprites/ui/table.png";

    // ── Card data ─────────────────────────────────────────────────────────────
    protected static class Card {
        int     imageIndex;
        boolean matched;
        boolean faceUp;
        boolean isGolden;
        float   x, y, w, h;
        // flip animation
        float   flipTimer;
        boolean flipping;
        boolean flipToFace; // true = currently flipping to show face
    }

    // ── Fields ────────────────────────────────────────────────────────────────
    protected final MainGame game;
    protected final int      gridSize;
    protected final int      numPairs;

    protected final float boardX, boardY, boardW, boardH, cardPadding;
    protected final float timerLimit;

    protected Stage              stage;
    protected OrthographicCamera camera;

    protected Card[]  cards;
    protected Card    firstCard, secondCard;
    protected float   mismatchTimer;
    protected boolean waitingForFlipBack;

    protected int     score;
    protected int     moves;
    protected float   timeLeft;
    protected boolean gameWon, gameLost;

    // ── Abstract helpers ──────────────────────────────────────────────────────
    protected abstract String getBgPath();
    /** "H", "J", or "A" — used by WinScreen to know which board to restart. */
    protected abstract String getDifficultyKey();
    protected abstract int    getStar3Moves();
    protected abstract int    getStar2Moves();

    // ── Constructor ───────────────────────────────────────────────────────────
    protected BaseGameScreen(MainGame game, int gridSize,
                              float boardX, float boardY, float boardW, float boardH,
                              float cardPadding, float timerLimit) {
        this.game        = game;
        this.gridSize    = gridSize;
        this.numPairs    = (gridSize * gridSize) / 2;
        this.boardX      = boardX;
        this.boardY      = boardY;
        this.boardW      = boardW;
        this.boardH      = boardH;
        this.cardPadding = cardPadding;
        this.timerLimit  = timerLimit;

        timeLeft = timerLimit;

        camera = new OrthographicCamera();
        stage  = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera), game.batch);

        loadCardAssets();
        buildCards();
        buildPauseButton();
        registerInput();

        game.playMusic("sounds/music/music_gameplay.ogg");
        trackGamesPlayed();
    }

    // ── Asset loading ─────────────────────────────────────────────────────────
    private void loadCardAssets() {
        for (int i = 0; i < numPairs; i++) {
            String path = CARD_FACE_IMAGES[i];
            if (!game.manager.isLoaded(path)) game.manager.load(path, Texture.class);
        }
        if (!game.manager.isLoaded(CARD_BACK))    game.manager.load(CARD_BACK, Texture.class);
        if (!game.manager.isLoaded(getBgPath()))   game.manager.load(getBgPath(), Texture.class);
        game.manager.finishLoading();
    }

    // ── Card setup ────────────────────────────────────────────────────────────
    private void buildCards() {
        int total = gridSize * gridSize;
        cards = new Card[total];

        int[] indices = new int[total];
        for (int i = 0; i < numPairs; i++) {
            indices[i * 2]     = i;
            indices[i * 2 + 1] = i;
        }
        // Fisher-Yates shuffle
        for (int i = total - 1; i > 0; i--) {
            int j = MathUtils.random(i);
            int tmp = indices[i]; indices[i] = indices[j]; indices[j] = tmp;
        }

        float totalPadW = cardPadding * (gridSize + 1);
        float totalPadH = cardPadding * (gridSize + 1);
        float cardW = (boardW - totalPadW) / gridSize;
        float cardH = (boardH - totalPadH) / gridSize;

        for (int i = 0; i < total; i++) {
            int col = i % gridSize;
            int row = i / gridSize;
            Card c = new Card();
            c.imageIndex = indices[i];
            c.faceUp     = false;
            c.matched    = false;
            c.isGolden   = (MathUtils.random() < Constants.GOLDEN_FOSSIL_CHANCE);
            c.x = boardX + cardPadding + col * (cardW + cardPadding);
            c.y = boardY + boardH - cardPadding - (row + 1) * cardH - row * cardPadding;
            c.w = cardW;
            c.h = cardH;
            cards[i] = c;
        }
    }

    // ── HUD ───────────────────────────────────────────────────────────────────
    private void buildPauseButton() {
        TextButton.TextButtonStyle roundStyle = UiFactory.makeRoundStyle(game.manager, game.fontSmall);
        TextButton pauseBtn = UiFactory.makeRoundButton("||", roundStyle,
                                                        Constants.BTN_W_PAUSE, Constants.BTN_H_PAUSE);
        float pauseY = getBottomPausePad();
        pauseBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_PAUSE) / 2f, pauseY);
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new PauseScreen(game, BaseGameScreen.this));
            }
        });
        stage.addActor(pauseBtn);
    }

    /** Bottom Y of the pause button in world coordinates. Subclasses override for different boards. */
    protected float getBottomPausePad() { return 30f; }

    private void registerInput() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleTouch(x, y);
                return false; // allow stage to also process (for pause button)
            }
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.BACK) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    // ── Game logic ────────────────────────────────────────────────────────────
    private void handleTouch(float stageX, float stageY) {
        if (gameWon || gameLost || waitingForFlipBack) return;

        for (Card c : cards) {
            if (c.matched || c.faceUp || c.flipping) continue;
            if (stageX >= c.x && stageX <= c.x + c.w &&
                stageY >= c.y && stageY <= c.y + c.h) {
                flipCardToFace(c);
                onCardTapped(c);
                return;
            }
        }
    }

    private void onCardTapped(Card c) {
        if (firstCard == null) {
            firstCard = c;
        } else if (secondCard == null && c != firstCard) {
            secondCard = c;
            moves++;
            checkMatch();
        }
    }

    private void checkMatch() {
        if (firstCard.imageIndex == secondCard.imageIndex) {
            firstCard.matched  = true;
            secondCard.matched = true;
            int bonus = (firstCard.isGolden || secondCard.isGolden)
                        ? Constants.GOLDEN_FOSSIL_BONUS
                        : Constants.SCORE_PER_MATCH;
            score += bonus;
            if (firstCard.isGolden || secondCard.isGolden) trackGoldenFossil();
            trackTotalMatch();
            firstCard  = null;
            secondCard = null;
            checkWin();
        } else {
            waitingForFlipBack = true;
            mismatchTimer      = Constants.CARD_MISMATCH_DELAY;
        }
    }

    private void checkWin() {
        for (Card c : cards) {
            if (!c.matched) return;
        }
        gameWon = true;
        int timeBonus = (int)(timeLeft * Constants.SCORE_TIME_BONUS_PER_SEC);
        score += timeBonus;
        int stars = calcStars();
        if (stars == 3) trackPerfectGame();
        LeaderboardScreen.addScore(score, gridSize);
        saveBestTime();
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_level_complete.ogg", com.badlogic.gdx.audio.Sound.class).play(1f);
        game.setScreen(new WinScreen(game, score, timeBonus, stars, getDifficultyKey()));
    }

    private int calcStars() {
        if (moves <= getStar3Moves()) return 3;
        if (moves <= getStar2Moves()) return 2;
        return 1;
    }

    private void triggerGameOver() {
        if (gameLost || gameWon) return;
        gameLost = true;
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_game_over.ogg", com.badlogic.gdx.audio.Sound.class).play(1f);
        // GameOverScreen(MainGame, int score, int boardSize)
        game.setScreen(new GameOverScreen(game, score, gridSize));
    }

    // ── Flip animation ────────────────────────────────────────────────────────
    private void flipCardToFace(Card c) {
        c.faceUp     = false;
        c.flipping   = true;
        c.flipToFace = true;
        c.flipTimer  = 0f;
    }

    private void flipCardToBack(Card c) {
        c.faceUp     = true;
        c.flipping   = true;
        c.flipToFace = false;
        c.flipTimer  = 0f;
    }

    private void updateCardFlips(float delta) {
        for (Card c : cards) {
            if (!c.flipping) continue;
            c.flipTimer += delta;
            float half = Constants.CARD_FLIP_DURATION / 2f;
            if (c.flipTimer >= half && c.flipTimer - delta < half) {
                c.faceUp = c.flipToFace;
            }
            if (c.flipTimer >= Constants.CARD_FLIP_DURATION) {
                c.flipping = false;
                c.faceUp   = c.flipToFace;
            }
        }
    }

    // ── Update ────────────────────────────────────────────────────────────────
    protected void update(float delta) {
        timeLeft -= delta;
        if (timeLeft <= 0f) {
            timeLeft = 0f;
            triggerGameOver();
            return;
        }

        updateCardFlips(delta);

        if (waitingForFlipBack) {
            mismatchTimer -= delta;
            if (mismatchTimer <= 0f) {
                waitingForFlipBack = false;
                if (firstCard  != null) flipCardToBack(firstCard);
                if (secondCard != null) flipCardToBack(secondCard);
                firstCard  = null;
                secondCard = null;
            }
        }
    }

    // ── Render ────────────────────────────────────────────────────────────────
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // Background
        game.batch.draw(game.manager.get(getBgPath(), Texture.class),
                        0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Draw cards
        for (Card c : cards) drawCard(c);

        // HUD: SCORE / TIMER / MOVES drawn directly at top of screen
        float topY = Constants.WORLD_HEIGHT - 8f;
        game.fontHud.draw(game.batch, "SCORE: " + score, 16f, topY);
        game.fontHud.draw(game.batch, formatTime(timeLeft),
                          (Constants.WORLD_WIDTH - 60f) / 2f, topY);
        game.fontHud.draw(game.batch, "MOVES: " + moves,
                          Constants.WORLD_WIDTH - 120f, topY);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void drawCard(Card c) {
        float half   = Constants.CARD_FLIP_DURATION / 2f;
        float scaleX = 1f;

        if (c.flipping) {
            float progress = c.flipTimer / half;
            if (c.flipTimer < half) {
                scaleX = 1f - Math.min(1f, progress);
            } else {
                scaleX = Math.min(1f, (c.flipTimer - half) / half);
            }
        }

        Texture tex = (c.faceUp || c.matched)
                      ? game.manager.get(CARD_FACE_IMAGES[c.imageIndex], Texture.class)
                      : game.manager.get(CARD_BACK, Texture.class);

        float drawW = c.w * scaleX;
        float drawX = c.x + (c.w - drawW) / 2f;
        game.batch.draw(tex, drawX, c.y, drawW, c.h);

        // Golden tint overlay
        if (c.isGolden && (c.faceUp || c.matched)) {
            game.batch.setColor(1f, 0.85f, 0.1f, 0.4f);
            game.batch.draw(tex, drawX, c.y, drawW, c.h);
            game.batch.setColor(1f, 1f, 1f, 1f);
        }
    }

    // ── Persistence helpers ───────────────────────────────────────────────────
    private void saveBestTime() {
        String key = bestTimeKey();
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        float existing = p.getFloat(key, Float.MAX_VALUE);
        float elapsed  = timerLimit - timeLeft;
        if (elapsed < existing) { p.putFloat(key, elapsed); p.flush(); }
    }

    private void trackGamesPlayed() {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        p.putInteger(Constants.PREF_GAMES_PLAYED,
                     p.getInteger(Constants.PREF_GAMES_PLAYED, 0) + 1);
        p.flush();
    }

    private void trackTotalMatch() {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        p.putInteger(Constants.PREF_TOTAL_MATCHES,
                     p.getInteger(Constants.PREF_TOTAL_MATCHES, 0) + 1);
        p.flush();
    }

    private void trackGoldenFossil() {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        p.putInteger(Constants.PREF_GOLDEN_FOSSILS,
                     p.getInteger(Constants.PREF_GOLDEN_FOSSILS, 0) + 1);
        p.flush();
    }

    private void trackPerfectGame() {
        Preferences p = Gdx.app.getPreferences(Constants.PREFS_NAME);
        p.putInteger(Constants.PREF_PERFECT_GAMES,
                     p.getInteger(Constants.PREF_PERFECT_GAMES, 0) + 1);
        p.flush();
    }

    private String bestTimeKey() {
        switch (getDifficultyKey()) {
            case "H": return Constants.PREF_BEST_TIME_H;
            case "J": return Constants.PREF_BEST_TIME_J;
            default:  return Constants.PREF_BEST_TIME_A;
        }
    }

    // ── Utility ───────────────────────────────────────────────────────────────
    protected static String formatTime(float seconds) {
        int m = (int)(seconds / 60);
        int s = (int)(seconds % 60);
        return String.format("%d:%02d", m, s);
    }

    protected void playClick() {
        if (game.sfxEnabled)
            game.manager.get("sounds/sfx/sfx_button_click.ogg",
                             com.badlogic.gdx.audio.Sound.class).play(1f);
    }

    // ── Screen lifecycle ──────────────────────────────────────────────────────
    @Override public void show()   { Gdx.input.setInputProcessor(stage); }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
