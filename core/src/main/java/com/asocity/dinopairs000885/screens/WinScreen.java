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

public class WinScreen implements Screen {

    private static final String BG = "ui/win_screen.png";

    private final MainGame game;
    private final int      finalScore;
    private final int      timeBonus;
    private final int      stars;        // 1, 2, or 3
    private final String   difficultyKey;

    private Stage stage;

    // Star textures
    private Texture starFull;
    private Texture starEmpty;

    public WinScreen(MainGame game, int finalScore, int timeBonus, int stars, String difficultyKey) {
        this.game          = game;
        this.finalScore    = finalScore;
        this.timeBonus     = timeBonus;
        this.stars         = stars;
        this.difficultyKey = difficultyKey;

        if (!game.manager.isLoaded(BG)) {
            game.manager.load(BG, Texture.class);
            game.manager.finishLoading();
        }

        starFull  = game.manager.get("ui/buttons/star.png", Texture.class);
        starEmpty = game.manager.get("ui/buttons/star_outline.png", Texture.class);

        OrthographicCamera camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera), game.batch);

        buildUi();
        registerInput();

        game.playMusicOnce("sounds/music/music_game_over.ogg");
    }

    private void buildUi() {
        TextButton.TextButtonStyle rectStyle = UiFactory.makeRectStyle(game.manager, game.fontBody);

        // "YOU WIN!" — top-Y=60, h=70 → libgdxY = 854-60-70 = 724
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.fontTitle, Color.WHITE);
        Label winLabel = new Label("YOU WIN!", titleStyle);
        winLabel.setPosition((Constants.WORLD_WIDTH - winLabel.getPrefWidth()) / 2f, 724f);
        stage.addActor(winLabel);

        // Score — top-Y=200, h=80 → libgdxY = 574
        Label.LabelStyle scoreStyle = new Label.LabelStyle(game.fontScore, Color.WHITE);
        Label scoreLabel = new Label(String.valueOf(finalScore), scoreStyle);
        scoreLabel.setPosition((Constants.WORLD_WIDTH - scoreLabel.getPrefWidth()) / 2f, 574f);
        stage.addActor(scoreLabel);

        // Time bonus — top-Y=520, h=48 → libgdxY = 286
        Label.LabelStyle bodyStyle = new Label.LabelStyle(game.fontBody, Color.WHITE);
        Label bonusLabel = new Label("TIME BONUS  +" + timeBonus, bodyStyle);
        bonusLabel.setPosition((Constants.WORLD_WIDTH - bonusLabel.getPrefWidth()) / 2f, 286f);
        stage.addActor(bonusLabel);

        // PLAY AGAIN — top-Y=620, h=64 → libgdxY = 170
        TextButton playAgainBtn = UiFactory.makeButton("PLAY AGAIN", rectStyle,
                                                       Constants.BTN_W_MAIN, Constants.BTN_H_MAIN);
        playAgainBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_MAIN) / 2f, 170f);
        playAgainBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(newGameScreen());
            }
        });

        // MENU — top-Y=702, h=56 → libgdxY = 96
        TextButton menuBtn = UiFactory.makeButton("MENU", rectStyle,
                                                  Constants.BTN_W_STD, Constants.BTN_H_STD);
        menuBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 96f);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // LEADERBOARD — top-Y=770, h=52 → libgdxY = 32
        TextButton lbBtn = UiFactory.makeButton("LEADERBOARD", rectStyle,
                                                Constants.BTN_W_STD, 52f);
        lbBtn.setPosition((Constants.WORLD_WIDTH - Constants.BTN_W_STD) / 2f, 32f);
        lbBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                playClick();
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        stage.addActor(playAgainBtn);
        stage.addActor(menuBtn);
        stage.addActor(lbBtn);
    }

    private Screen newGameScreen() {
        switch (difficultyKey) {
            case "H": return new HatchlingScreen(game);
            case "J": return new JuvenileScreen(game);
            default:  return new AlphaScreen(game);
        }
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

        // Draw star rating — top-Y=310, h=60 → libgdxY=484; 3 stars centered
        // Stars area: top-Y=310, h=60 → libgdxY = 854-310-60 = 484
        float starSize = 56f;
        float starGap  = 10f;
        float totalW   = 3 * starSize + 2 * starGap;
        float starStartX = (Constants.WORLD_WIDTH - totalW) / 2f;
        float starY      = 484f;
        for (int i = 0; i < 3; i++) {
            Texture tex = (i < stars) ? starFull : starEmpty;
            game.batch.draw(tex, starStartX + i * (starSize + starGap), starY, starSize, starSize);
        }
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
