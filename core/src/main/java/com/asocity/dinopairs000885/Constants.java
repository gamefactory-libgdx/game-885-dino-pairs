package com.asocity.dinopairs000885;

public class Constants {

    // ── World ──────────────────────────────────────────────────────────────
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 854f;

    // ── Board sizes ────────────────────────────────────────────────────────
    public static final int BOARD_HATCHLING = 4;   // 4×4 = 16 cards
    public static final int BOARD_JUVENILE  = 6;   // 6×6 = 36 cards
    public static final int BOARD_ALPHA     = 8;   // 8×8 = 64 cards

    // ── Timer limits (seconds) ─────────────────────────────────────────────
    public static final float TIMER_HATCHLING = 90f;
    public static final float TIMER_JUVENILE  = 180f;
    public static final float TIMER_ALPHA     = 300f;

    // ── Card layout — Hatchling (4×4) ─────────────────────────────────────
    public static final float HATCHLING_BOARD_X      = 30f;
    public static final float HATCHLING_BOARD_Y      = 854f - 120f - 420f; // top-Y=120 → libgdxY
    public static final float HATCHLING_BOARD_W      = 420f;
    public static final float HATCHLING_BOARD_H      = 420f;
    public static final float HATCHLING_CARD_PADDING = 6f;

    // ── Card layout — Juvenile (6×6) ─────────────────────────────────────
    public static final float JUVENILE_BOARD_X       = 20f;
    public static final float JUVENILE_BOARD_Y       = 854f - 100f - 560f; // top-Y=100 → libgdxY
    public static final float JUVENILE_BOARD_W       = 440f;
    public static final float JUVENILE_BOARD_H       = 560f;
    public static final float JUVENILE_CARD_PADDING  = 5f;

    // ── Card layout — Alpha (8×8) ─────────────────────────────────────────
    public static final float ALPHA_BOARD_X          = 12f;
    public static final float ALPHA_BOARD_Y          = 854f - 80f - 640f;  // top-Y=80  → libgdxY
    public static final float ALPHA_BOARD_W          = 456f;
    public static final float ALPHA_BOARD_H          = 640f;
    public static final float ALPHA_CARD_PADDING     = 4f;

    // ── Card flip timing ──────────────────────────────────────────────────
    public static final float CARD_FLIP_DURATION     = 0.3f;
    public static final float CARD_MISMATCH_DELAY    = 0.8f;

    // ── Scoring ───────────────────────────────────────────────────────────
    public static final int SCORE_PER_MATCH          = 100;
    public static final int SCORE_TIME_BONUS_PER_SEC = 5;
    public static final int STAR_3_MOVES_HATCHLING   = 14;
    public static final int STAR_2_MOVES_HATCHLING   = 20;
    public static final int STAR_3_MOVES_JUVENILE    = 30;
    public static final int STAR_2_MOVES_JUVENILE    = 45;
    public static final int STAR_3_MOVES_ALPHA       = 55;
    public static final int STAR_2_MOVES_ALPHA       = 75;

    // ── Golden fossil chance ──────────────────────────────────────────────
    public static final float GOLDEN_FOSSIL_CHANCE   = 0.1f;
    public static final int   GOLDEN_FOSSIL_BONUS    = 250;

    // ── Leaderboard ───────────────────────────────────────────────────────
    public static final int LEADERBOARD_MAX_ENTRIES  = 10;

    // ── UI button sizes ───────────────────────────────────────────────────
    public static final float BTN_W_MAIN   = 280f;
    public static final float BTN_H_MAIN   = 64f;
    public static final float BTN_W_WIDE   = 300f;
    public static final float BTN_H_WIDE   = 90f;
    public static final float BTN_W_STD    = 280f;
    public static final float BTN_H_STD    = 56f;
    public static final float BTN_W_SMALL  = 160f;
    public static final float BTN_H_SMALL  = 52f;
    public static final float BTN_W_PAUSE  = 160f;
    public static final float BTN_H_PAUSE  = 52f;
    public static final float BTN_W_ROUND  = 56f;
    public static final float BTN_H_ROUND  = 56f;

    // ── SharedPreferences keys ─────────────────────────────────────────────
    public static final String PREFS_NAME            = "GamePrefs";
    public static final String PREF_MUSIC            = "musicEnabled";
    public static final String PREF_SFX              = "sfxEnabled";
    public static final String PREF_VIBRATION        = "vibrationEnabled";
    public static final String PREF_HAPTICS          = "hapticsEnabled";

    // Leaderboard — Hatchling (stores JSON)
    public static final String PREF_LB_HATCHLING     = "lb_hatchling";
    // Leaderboard — Juvenile
    public static final String PREF_LB_JUVENILE      = "lb_juvenile";
    // Leaderboard — Alpha
    public static final String PREF_LB_ALPHA         = "lb_alpha";

    // Stats
    public static final String PREF_GAMES_PLAYED     = "gamesPlayed";
    public static final String PREF_BEST_TIME_H      = "bestTimeHatchling";
    public static final String PREF_BEST_TIME_J      = "bestTimeJuvenile";
    public static final String PREF_BEST_TIME_A      = "bestTimeAlpha";
    public static final String PREF_TOTAL_MATCHES    = "totalMatches";
    public static final String PREF_GOLDEN_FOSSILS   = "goldenFossils";
    public static final String PREF_PERFECT_GAMES    = "perfectGames";

    // ── Font sizes ─────────────────────────────────────────────────────────
    public static final int FONT_SIZE_TITLE   = 52;
    public static final int FONT_SIZE_BODY    = 28;
    public static final int FONT_SIZE_SMALL   = 20;
    public static final int FONT_SIZE_SCORE   = 42;
    public static final int FONT_SIZE_HUD     = 22;
}
