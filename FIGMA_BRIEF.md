# Figma AI Design Brief — Dino Pairs

---

## 1. Art Style & Color Palette

**Style:** Lush prehistoric jungle meets hand-painted storybook. Thick ink outlines, warm earthy textures, and cel-shaded foliage give every screen the feel of an illustrated field guide brought to life. Cards have a worn parchment quality with subtle fossil imprints on their backs. The overall mood is adventurous and playful — aimed at all ages but skewing toward families and casual players.

**Primary Palette**
| Role | Hex | Use |
|---|---|---|
| Jungle Canopy | `#2D5A27` | Main background fills, board border |
| Warm Amber | `#C8762B` | Card backs, tile frames, UI panels |
| Fossil Cream | `#F2E8C6` | Card face backgrounds, text surfaces |
| Deep Terra | `#6B3A1F` | Outlines, shadows, soil textures |

**Accent Palette**
| Role | Hex | Use |
|---|---|---|
| Electric Gold | `#FFD700` | Golden fossil tiles, star ratings, matched glow |
| Roar Red | `#E84040` | Danger/timer warnings, highlights |

**Font Mood:** Chunky rounded serif display font for titles (think carved stone but friendly — e.g. `YosterIsland` or `KirbyKiraStar`); clean rounded sans for counts and labels. Never sharp or mechanical — everything should feel organic and carved.

---

## 2. App Icon — `icon_512.png` (512×512px)

**Canvas:** 512×512px square, full bleed, no transparency, no text.

**Background:** Deep radial gradient from `#2D5A27` jungle green at center expanding to `#1A3A14` dark forest at edges — rich, lush, slightly vigneted.

**Central Symbol:** A single large T-Rex head in bold cel-shaded illustration style, three-quarter view facing slightly left, rendered in warm amber-brown (`#C8762B`) with `#6B3A1F` ink outlines. The dinosaur wears a mischievous grin and one eye is rendered as a bold cartoon starburst to suggest a "found you!" match moment.

**Overlay Details:** Two overlapping card rectangles peek from behind the T-Rex — parchment cream (`#F2E8C6`) with rounded corners and a subtle fossil texture — suggesting the memory card mechanic. A small golden fossil icon (`#FFD700`) glints on the lower-right card, barely visible, rewarding close inspection.

**Glow & Mood:** Warm amber rim light along the T-Rex jaw and top of skull; a soft golden radial glow (`#FFD700` at 30% opacity) blooms behind the head. Drop shadow on the cards: `#1A3A14` at 50% opacity, 8px blur, offset 4px down. Energy is playful, warm, and confident.

---

## 3. UI Screens (480×854 portrait)

---

### MainMenuScreen

**A) BACKGROUND IMAGE**
Full-screen prehistoric jungle panorama: towering fern fronds and palm-like cycad leaves frame the left and right edges in deep `#2D5A27` and `#1A3A14`. The center-upper zone opens to a hazy amber sky gradient (`#E8A44A` → `#C8762B`) suggesting a warm Cretaceous dusk. Distant volcano silhouettes in `#3D2010` sit low on the horizon. Ground level shows mossy earth with large fossil ammonite shapes half-buried, rendered in `#6B3A1F` outline style. A large blank parchment banner shape (`#F2E8C6`, rounded rectangle with torn edge detail, no text inside) occupies the vertical center — this is where the game title will be drawn by code. Two decorative blank card shapes lean against either side of the banner like propped-up tiles, their backs showing embossed dinosaur egg patterns in `#C8762B`. Floating dust motes and warm bokeh spheres in `#FFD700` at low opacity drift across the scene.

**B) BUTTON LAYOUT**
```
DINO PAIRS (title label)  | top-Y=80px  | x=centered          | size=360x90
PLAY                      | top-Y=420px | x=centered          | size=280x64
LEADERBOARD               | top-Y=502px | x=centered          | size=280x56
STATS                     | top-Y=572px | x=centered          | size=280x56
SETTINGS                  | top-Y=642px | x=centered          | size=280x56
```

---

### SizeSelectScreen

**A) BACKGROUND IMAGE**
Same jungle atmosphere as MainMenu but the center is dominated by three large blank decorative tile-grid silhouettes arranged vertically — a small 4×4 ghost grid, a medium 6×6 ghost grid, and a large 8×8 ghost grid — all rendered as faint `#F2E8C6` outlines at 25% opacity against the warm amber-lit clearing background. Each grid sits inside its own empty parchment frame panel (`#F2E8C6` with `#C8762B` border, rounded corners) that widens from top to bottom reflecting the increasing board sizes. Jungle foliage crowds the sides. A worn rope-and-wood banner shape hangs at the top — completely blank inside, ready for "Choose Your Board" text from code. Small fossil ammonite decorations accent the corners of each panel frame.

**B) BUTTON LAYOUT**
```
CHOOSE YOUR BOARD (label)   | top-Y=60px  | x=centered | size=360x60
HATCHLING (4×4) button      | top-Y=200px | x=centered | size=300x90
JUVENILE (6×6) button       | top-Y=360px | x=centered | size=300x90
ALPHA (8×8) button          | top-Y=520px | x=centered | size=300x90
BACK                        | top-Y=760px | x=left@20px| size=120x48
```

---

### HatchlingScreen

**A) BACKGROUND IMAGE**
Light, airy jungle clearing bathed in soft morning gold (`#E8C97A` → `#F2E8C6` center glow). The board zone — a large empty rounded rectangle framing area in `#6B3A1F` with double-border detail and small claw-mark corner decorations — occupies the center, sized to accommodate a 4×4 card grid drawn by code. Mossy ground texture at the bottom; fern fronds peek in at upper corners. A narrow horizontal decorative band at the top holds blank space for score/timer HUD elements drawn by code. Baby dinosaur egg motifs in `#C8762B` dot the border decoration — cracked but cute, suggesting "beginner" level. Ambient firefly-style `#FFD700` sparkles scatter lightly across the darker jungle border areas.

**B) BUTTON LAYOUT**
```
SCORE label        | top-Y=22px  | x=left@30px  | size=160x36
TIMER label        | top-Y=22px  | x=right@30px | size=160x36
MOVES label        | top-Y=22px  | x=centered   | size=160x36
BOARD (4×4 grid)   | top-Y=120px | x=centered   | size=420x420
PAUSE              | top-Y=770px | x=centered   | size=160x52
```

---

### JuvenileScreen

**A) BACKGROUND IMAGE**
Denser jungle at midday — canopy shifts to richer `#2D5A27` with shafts of warm light breaking through. The central board frame is larger than Hatchling, same double-border style but with subtle dinosaur footprint stamps along the border edges (inkstamp silhouette style in `#6B3A1F`). Board zone sized for 6×6 grid. Mossy boulders and twisted roots frame the lower portion. The top HUD band is slightly taller than Hatchling to accommodate more data drawn by code. Mid-level pterosaur silhouettes in dark `#1A3A14` glide across the upper background sky gap. Small golden fossil fragments are scattered in the earth at the board's base.

**B) BUTTON LAYOUT**
```
SCORE label        | top-Y=18px  | x=left@20px  | size=140x36
TIMER label        | top-Y=18px  | x=right@20px | size=140x36
MOVES label        | top-Y=18px  | x=centered   | size=140x36
BOARD (6×6 grid)   | top-Y=100px | x=centered   | size=440x560
PAUSE              | top-Y=780px | x=centered   | size=160x52
```

---

### AlphaScreen

**A) BACKGROUND IMAGE**
Dark, dramatic late-afternoon jungle — deep `#1A3A14` canopy, volcanic glow at horizon (deep orange `#C8762B` to `#E84040` gradient low on horizon, partially obscured by silhouetted tree ferns). The board frame is the largest variant — heavy carved-stone border aesthetic with dinosaur scale texture pressed into the frame edge. Board zone fills most of the screen, sized for 8×8 grid. The top HUD strip is compact and dark (`#1A3A14` panel). Glowing fossil veins in `#FFD700` thread through the stone border decoration, suggesting ancient power. Embers and ash mote particles drift upward from the bottom corners of the screen.

**B) BUTTON LAYOUT**
```
SCORE label        | top-Y=14px  | x=left@16px  | size=130x32
TIMER label        | top-Y=14px  | x=right@16px | size=130x32
MOVES label        | top-Y=14px  | x=centered   | size=130x32
BOARD (8×8 grid)   | top-Y=80px  | x=centered   | size=456x640
PAUSE              | top-Y=792px | x=centered   | size=160x48
```

---

### WinScreen

**A) BACKGROUND IMAGE**
Celebration explosion of warm amber and gold across the entire canvas. Background: radial burst pattern in `#FFD700` and `#E8A44A` radiating from center, overlaid with the jungle framing at edges still visible. Confetti-style shapes — tiny fossil silhouettes, leaf bursts, claw marks — scatter across the canvas in `#C8762B`, `#2D5A27`, and `#F2E8C6`. A large decorative circular medallion in the upper center — thick `#6B3A1F` outlined ring with amber fill and embossed fossil textures inside the ring body — sits blank in the center ready for score display. Below it, three blank star outline shapes arranged horizontally in `#FFD700` (for 1–3 star rating drawn by code). Bottom area shows a blank parchment scroll unfurled horizontally — open, empty, ready for time/bonus stats text from code.

**B) BUTTON LAYOUT**
```
YOU WIN! (label)         | top-Y=60px  | x=centered | size=360x70
SCORE (large label)      | top-Y=200px | x=centered | size=300x80
STARS (3× star icons)    | top-Y=310px | x=centered | size=280x60
TIME BONUS label         | top-Y=520px | x=centered | size=300x48
PLAY AGAIN               | top-Y=620px | x=centered | size=280x64
MENU                     | top-Y=702px | x=centered | size=280x56
LEADERBOARD              | top-Y=770px | x=centered | size=280x52
```

---

### GameOverScreen

**A) BACKGROUND IMAGE**
Somber but not crushing — the jungle at twilight, `#1A3A14` deep background with cool blue-grey fog (`#3A4A5A` at 40% overlay) rolling across the midground. A cracked parchment panel fills the center vertically — torn at top and bottom edges, slightly tilted, `#F2E8C6` with age-spot texture and `#6B3A1F` crack lines running through it — empty inside, ready for "Out of Time" / score text from code. Small dinosaur footprints trail off the bottom of the panel into the fog. Rain-like diagonal streaks in `#3A4A5A` at 20% opacity give a gentle "wash out" feeling without being harsh.

**B) BUTTON LAYOUT**
```
GAME OVER (label)   | top-Y=80px  | x=centered | size=360x70
SCORE label         | top-Y=280px | x=centered | size=300x60
TRY AGAIN           | top-Y=520px | x=centered | size=280x64
CHANGE SIZE         | top-Y=600px | x=centered | size=280x56
MENU                | top-Y=672px | x=centered | size=280x56
```

---

### LeaderboardScreen

**A) BACKGROUND IMAGE**
Grand stone-tablet aesthetic. Background: dark earth `#3D2010` with carved stone texture fills the entire canvas. A large blank stone slab occupies the center — thick, slightly 3D-beveled, lighter `#6B3A1F` stone face with subtle horizontal line guides etched in (for rows of scores to be drawn by code — lines only, no text). Torch-flame effects in `#E8A44A` and `#FFD700` flicker from wall-bracket decorations on either side. Three decorative empty trophy/medallion outlines in `#FFD700` mark the top-3 row positions on the slab. Jungle moss creeps along the stone bottom edge. Top: a carved stone banner shape — blank inside, arched top, embossed border.

**B) BUTTON LAYOUT**
```
LEADERBOARD (label)     | top-Y=40px  | x=centered | size=360x60
TAB: HATCHLING          | top-Y=110px | x=left@20px  | size=140x44
TAB: JUVENILE           | top-Y=110px | x=centered   | size=140x44
TAB: ALPHA              | top-Y=110px | x=right@20px | size=140x44
SCORE ROWS (×10)        | top-Y=170px | x=centered   | size=440x560
BACK                    | top-Y=790px | x=centered   | size=280x48
```

---

### StatsScreen

**A) BACKGROUND IMAGE**
Field-journal / naturalist-notebook aesthetic. Full background in aged parchment `#F2E8C6` with faint grid-paper lines and subtle watercolor botanical sketches of ferns at corners (muted, non-distracting, `#C8762B` at 15% opacity). A large central journal-page panel — white `#FEFDF8` with slight drop shadow and deckled edge on both sides — fills the center vertically, completely empty inside ready for stat text rows from code. Ink-splatter accent marks in `#6B3A1F` pepper the corners. Top: a decorative ink-drawn horizontal band with a magnifying glass and fossil bone motifs flanking a blank central title space.

**B) BUTTON LAYOUT**
```
STATS (label)               | top-Y=40px  | x=centered | size=320x60
GAMES PLAYED label          | top-Y=160px | x=centered | size=420x44
BEST TIME: HATCHLING label  | top-Y=220px | x=centered | size=420x44
BEST TIME: JUVENILE label   | top-Y=280px | x=centered | size=420x44
BEST TIME: ALPHA label      | top-Y=340px | x=centered | size=420x44
TOTAL MATCHES label         | top-Y=400px | x=centered | size=420x44
GOLDEN FOSSILS label        | top-Y=460px | x=centered | size=420x44
PERFECT GAMES label         | top-Y=520px | x=centered | size=420x44
RESET STATS                 | top-Y=680px | x=centered | size=240x52
BACK                        | top-Y=748px | x=centered | size=280x52
```

---

### SettingsScreen

**A) BACKGROUND IMAGE**
Same field-journal parchment base as StatsScreen for visual continuity. The central panel is styled as a carved wooden control board — dark walnut `#6B3A1F` panel face with inset recessed slots (empty horizontal rectangles with slight bevel, `#4A2810`, no labels) suggesting toggle/slider positions that code will fill. Decorative brass rivet circles at each corner of the panel. Fern watermarks at screen edges. Top area: same decorative ink band as Stats but with a gear-fossil hybrid illustration (gear teeth replaced with bone segments) flanking the blank title space.

**B) BUTTON LAYOUT**
```
SETTINGS (label)       | top-Y=40px  | x=centered          | size=320x60
MUSIC label            | top-Y=180px | x=left@40px         | size=180x44
MUSIC TOGGLE           | top-Y=180px | x=right@40px        | size=100x44
SFX label              | top-Y=250px | x=left@40px         | size=180x44
SFX TOGGLE             | top-Y=250px | x=right@40px        | size=100x44
VIBRATION label        | top-Y=320px | x=left@40px         | size=180x44
VIBRATION TOGGLE       | top-Y=320px | x=right@40px        | size=100x44
HAPTICS label          | top-Y=390px | x=left@40px         | size=180x44
HAPTICS TOGGLE         | top-Y=390px | x=right@40px        | size=100x44
RESET PROGRESS         | top-Y=560px | x=centered          | size=260x56
BACK                   | top-Y=760px | x=centered          | size=280x52
```

---

## 4. Export Checklist

```
- icon_512.png (512x512)
- ui/main_menu_screen.png (480x854)
- ui/size_select_screen.png (480x854)
- ui/hatchling_screen.png (480x854)
- ui/juvenile_screen.png (480x854)
- ui/alpha_screen.png (480x854)
- ui/win_screen.png (480x854)
- ui/game_over_screen.png (480x854)
- ui/leaderboard_screen.png (480x854)
- ui/stats_screen.png (480x854)
- ui/settings_screen.png (480x854)
```