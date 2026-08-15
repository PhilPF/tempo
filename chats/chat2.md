# Workout app interface feedback

_Started 2026-08-15 08:06 UTC_

---

## User

Continuing from "Workout app with 3D mannequin".

## Assistant

We've been iterating on REGIMEN, a minimal dark-themed workout app, from a bloated multi-feature design down to a lean, UX-first approach focused on session flow.

Early versions over-designed the UI (cards, complex layouts, interactive 3D mannequin). After feedback, we stripped back to essentials: home shows a routine list with progress checkmarks and a "continue session" CTA (restored from v1); the exercise screen merges detail + active session into one view with a top progress bar ("EXERCISE 01/05" + set dots), exercise name, static mannequin with muscle highlights (blue accent — bright for primary, dim for secondary), a rest timer, and a single "FINISH SET" button. No weight/rep editing, no navigation back to home per exercise — advancing auto-progresses to the next, returning home only when done.

Font strategy: Libre Caslon Text (serif) used minimally as accent — wordmark, headline, exercise name, rest countdown. Fragment Mono for all UI labels and captions. Dark background throughout, single blue accent color.

Current state: Just fixed a label-wrapping bug ("0 / 05"). The layout is minimal and functional; mannequin is a static silhouette with color-coded muscle zones that light up per exercise.

Next steps: Review the current flow end-to-end (home → exercise progression → session completion); test readability and visual hierarchy at small phone sizes; confirm the mannequin muscle zones map correctly to each exercise's primary/secondary targets.

Files: REGIMEN.dc.html (current), REGIMEN v3.dc.html (previous), support.js.

## User

I don't understand why it says Today's Session, Regimen and push day. Instead of showing Chest-Shoulders-Triceps, we could add a small mannequin that shows all the areas worked by the exercises. In the exercises page, I think that the top part is too crowded. Also notice the confusion in the rest page, as the big button is to finish the set and not to skip the rest, so someone may end the set thinking they are skipping the rest. Also, in the resting page, the mannequin should also appear, as that allows one to check the correct posture for the next exercise while resting. In general, I like wireframe or blueprint like designs. But I also like designs with rounded corners and transparency (with blue).

## Assistant

_[tool: read_file]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

Now add `todayPatches` (aggregate mannequin) to the logic class.

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: ready_for_verification]_

Changes made: home now shows "Push Day" once with a mini mannequin (aggregate of all muscles worked) instead of the redundant "Today's Session" text; exercise header condensed to one row (back arrow + name + counter); rest screen now shows the full mannequin with the countdown overlaid, and the primary button swaps to an outlined "SKIP REST →" during rest so it can't be confused with "FINISH SET"; cards, panels and buttons now use rounded corners with translucent blue blueprint-grid fills.

## User

The top part of the screen in the exercise page is still too crowded. I would show the countdown for the exercise in some other way that makes it easier to see without hiding the mannequin. In the main page, I would put the mannequin on the right instead of on the left of the name of the routine. Maybe we could start to draft the rest of the functionality of the app. I will give you creative freedom

## Assistant

_[tool: read_file]_

_[tool: dc_write]_

_[tool: ready_for_verification]_

Home now puts the mannequin on the right of the routine info. Exercise page top is decluttered: the set-count chip moved into a small badge on the mannequin card itself, and the rest countdown is now a small ring badge in the corner instead of overlaying the mannequin. Also drafted Progress (weekly streak + muscle balance mannequin) and Profile (account + preferences) tabs, with a working bottom nav across all three.

## User

Found issues — fixing…

## Assistant

_[tool: dc_html_str_replace]_

_[tool: ready_for_verification]_

Fixed — header now uses a margin-top spacer instead of stacking padding into a fixed-height box, so the title no longer overflows into the progress dots.

## User

This is much better now. Let us think on a better name for the app and use the monospaced font for the "logo". Let us do some brainstorming.  The button for starting or continuing the session could be less wide so that it does not overflow in the bottom of the mannequin. So that we have in the left the text and button and on the right the mannequin. I like that in the other tabs there is consistency on the format, so let us make sure of that. Also, I guess there should be some functionality to create routines by choosing exercices and then select them.

## Assistant



## Assistant

_[tool: run_script]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

Now renaming the app and drafting the routine builder.

_[tool: ask_user]_

Home's CTA button and mannequin are now side by side (button no longer spans under the mannequin), header spacing is unified across tabs. Waiting on your pick for the app name — I'll draft the routine-builder flow next.

## User

Answered — Asked for follow-up questions

## Assistant



## Assistant

_[tool: ask_user]_

Waiting on name direction and routine-builder scope.

## User

Answered — name_length: Short (4-6 letters); routine_builder_scope: Pick exercises from a library, Set order via drag/reorder, Set sets x reps per exercise, Assign primary/secondary muscles, Name & save as a new routine

## Assistant

_[tool: dc_write]_

_[tool: ready_for_verification]_

Named the app TEMPO with a monospace wordmark. Added a "New Routine" builder (3-step: pick exercises from library → set order and sets/reps → name & review with auto-generated muscle-balance mannequin), reachable from the Exercises section on Home.

## User

I don't think this makes sense. I don't like the positioning of the New Routine button, not it seems one can select it later, nor edit current Routines. So we should figure that out. In the exercise page, the countdown should also use one of these transparency or wireframe suggestions I made earlier for the design, and of course, it should use a monospaced font. The progress page should also have more functionalities. The logo could use the 0 instead of the O for TEMPO, which would make it look better.

## Assistant

_[tool: dc_write]_

_[tool: ready_for_verification]_

Restructured routines: tapping the routine name on Home opens a "Your Routines" screen listing all routines (Push/Pull/Leg Day), each selectable to make active and editable via a pencil icon — the builder now supports editing existing routines, not just creating new ones. "New Routine" moved there as a dashed outline button. Rest countdown badge is now transparent/dashed blueprint style in monospace. Wordmark now reads TEMP0 with a zero. Progress page gained session/streak stat chips and a recent sessions list.

## User

Found issues — fixing…

## Assistant

_[tool: dc_js_str_replace]_

_[tool: ready_for_verification]_

Shortened the exercise sets/reps chip to "4×8" so it fits on one line.

## User

Let us choose another monospaced font, more courier or terminal looking. I don't like the dashed lines in the countdown. Also, both the countdown and the set reps indicator should be bigger. The top progress bar could have some other design. I like that it shows the sets, but it should also show the exercises. So I was thinking that it could show lines for the exercices, and one we are in that exercices, that line expands to show the sets. And so we only see the individual sets when doing that exercise. It should be clear regardless the difference between exercise and set. In the edit of the routine, I would like a drag and drop for the order. Also, the icons should be much more minimalist to fit with the aesthetic of the design.

## Assistant

## Assistant

_[tool: run_script]_

_[tool: read_file]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: ready_for_verification]_

Switched to Courier Prime for a more terminal look. Rest countdown badge is now solid-bordered (no dashes) and larger; the sets×reps chip is also bigger. Exercise progress bar now shows one thin segment per exercise, with the current exercise's segment expanding into its individual set dots — so exercise vs. set is visually distinct. Routine editing now supports drag-and-drop reordering (minimalist dot-grip handle) instead of up/down arrows, and the routine-list edit action is now a plain "EDIT" label instead of a pencil icon.

## User

I don't like the font. Could you show some suggestions? I don't think we should add an active text in the routine selection. It would be enough to show some mark instead. For searching exercices, we should add a search bar. I also think that the three steps should appear in the same screen, as that allows for better fine tuning when creating a routine. I don't like to use arrows in buttons. The dots for the drag and drop should be subtle and in the left, that is, the text and buttons should appear right next to it. It would also be nice to add an option for setting weights in dumbbell exercises, for example, which should also appear later when doing the exercise. I don't like the choice for the design of the top progress bar, nor for the size and positioning of the reps and countdown. I'd rather the countdown have some transparency than being plain black. Also, recall the consistency of the accent color

## Assistant



## Assistant

_[tool: read_file]_

_[tool: dc_write]_

_[tool: ask_user]_

Made the other changes: routine list now shows a small dot mark instead of an "ACTIVE" text pill; builder is a single scrollable screen (name, order/volume with drag-reorder and weight steppers for weighted exercises, search bar, exercise list) instead of 3 steps; drag handles are subtle dim dots on the left; exercise screen has a two-tier progress indicator (thin ticks per exercise, larger dots for sets in the current one); rest countdown is now a large centered translucent glass panel; arrows removed from button labels. Waiting on your monospace font pick to finish the type change.

## User

Let us use the first monospaced font we chose

## User

Let us use the first monospaced font we chose

## Assistant

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: ready_for_verification]_

Switched to JetBrains Mono throughout.

## User

Re-running the check

## Assistant

_[tool: ready_for_verification]_

Re-ran verification — no issues reported.

## User

The choice of the weight should be by writing it, not with buttons, and the default unit should simply be that decided by the users in his config. Besides that, weight should not appear for all exercises. In the exercise panel, it is not necessary to show the count of the exercises, the bars are enough. I liked the reps appearing inside the blue grid thing. Maybe we can recover that and simply put the rep count next to it. I like the use of the dots like that. I like the style of the resting countdown, but maybe it should not block the mannequin so that one can see the correct posture if one wants to check

## Assistant

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: dc_js_str_replace]_

_[tool: ready_for_verification]_

Weight is now a typed number field (unit pulled from Profile settings, "kg" default). Removed exercise-count labels under the top ticks — bars alone now. Reps/set-dots badge moved back inside the mannequin card (top-left). Rest countdown badge shrunk to a corner panel instead of covering the mannequin, so posture stays visible during rest.

## User

Found issues — fixing…

## Assistant

_[tool: dc_html_str_replace]_

_[tool: dc_html_str_replace]_

_[tool: ready_for_verification]_

Fixed — badge text no longer wraps (nowrap + fit-content width).

## User

We should unify the view of the exercises of the routine when simply selecting it or viewing it, and it also should show the mannequin with the highlighted parts to exercise instead of the names of the parts. In the exercise panel, the dots of the sets should be at the right of the text instead, and the countdown should have a similar aesthetic. Maybe we could center it on the bottom, instead on the top right. Also, notice that the size of the font of the name of the routine does not coincide with that of the progress or profile. That all should also be uniformized

## Assistant

_[tool: read_file]_

_[tool: dc_html_str_replace]_

_[tool: str_replace_edit]_

_[tool: dc_js_str_replace]_

_[tool: ready_for_verification]_

Routines list rows now show a mini mannequin (highlighted muscles) instead of the text muscle summary, unifying with the Home detail view style. Exercise panel: set dots moved to the right of the sets/reps text, and rest countdown is now a bottom-centered pill matching the reps badge's aesthetic instead of a top-right glass panel. Unified headline size (32px) across Home, Progress, and Profile.

