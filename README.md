# Need for Speed

> ...or is there?
> Check how different driving speeds affect fuel consumption and travel time.

My first ClojureScript project.
Also my entry to
[Solidabis's 2021 coding challenge](#coding-challenge).

[👉 Run the app](https://need-for-speed.netlify.app/)

---

**Update:**
my entry won the coding challenge! 🏆🥳

I was a bit surprised
since my app is very basic.
On the other hand,
the coding challenge instructions were also very basic.
Maybe ClojureScript was so exotic a choice
that it netted me extra points in the contest.

I got a 300&euro; gift card to [Verkkokauppa.com](https://www.verkkokauppa.com/),
a Finnish online store selling geeky stuff
(but many other kinds of stuff as well).
Hmm,
I spent ~16.5 hours on the project,
so the effective net hourly rate became ~18&euro;.
Not bad for a fun little side project.

[Read about the coding challenge entries on Solidabis's blog (in Finnish) &rarr;](https://www.solidabis.com/ajankohtaista/koodihaasteen-2021-voittajat/)

---

- [Tech stack](#tech-stack)
- [Development mode](#development-mode)
- [Building for production](#building-for-production)
- [Coding challenge](#coding-challenge)
- [Diary](#diary)
- [Issues](#issues)

---

## Tech stack

- [ClojureScript: a Clojure compiler that targets JavaScript](https://clojurescript.org/)
- [Reagent: a React wrapper for ClojureScript](https://reagent-project.github.io/)
- [reagent-frontend-template](https://github.com/reagent-project/reagent-frontend-template)
  for generating the project structure
- [Twind: a "Tailwind-in-JS" library](https://twind.dev/)
  for styling
- [Tailwind UI: official Tailwind CSS components](https://tailwindui.com/)

I did the development on Windows
using the [Atom editor](https://atom.io/).
See [Diary entry on 2021-05-25](#2021-05-25-) for details.

The app is very simple
but took quite a long time
(see the [Diary section](#diary))
because this was my first ClojureScript project.
There are still many things that I'd like to improve.
Maybe I'll revisit this project after a while
so I can take a look using fresh eyes.

I really like Reagent.
It makes UI building and state management very easy and simple.
It reminds me of [Mithril.js](https://mithril.js.org/)
which I also like very much.
React seems quite convoluted in comparison.

I look forward to using
[re-frame for more complex state management](https://github.com/Day8/re-frame)
in the future.

## Development mode

0. If you haven't used ClojureScript before,
   see [ClojureScript's Quick Start guide](https://clojurescript.org/guides/quick-start)
   and install the required dependencies
1. `npm install`
2. `npm start`
3. Open <http://localhost:3000/>
4. Optional:
   - In Atom,
     press <kbd>Ctrl</kbd>+<kbd>Shift</kbd>+<kbd>P</kbd>
     and select "Chlorine: Connect Socket Repl"
     and "Chlorine: Connect Embedded"
   - Now you can evaluate code in Atom e.g. with
     [Chlorine's example keybindings](https://github.com/mauricioszabo/atom-chlorine/blob/master/README.md#keybindings)

## Building for production

1. `npm run build`

The ClojureScript code is compiled to JavaScript
and outputted to the `public/js/` directory.
Then you can deploy the whole `public/` directory.

I have deployed the app on Netlify:
[need-for-speed.netlify.app](https://need-for-speed.netlify.app/).
It took only like 5 minutes.

## Coding challenge

This project is my solution to
a coding challenge published by
[Solidabis, a Finnish IT firm (the site is in Finnish)](https://www.solidabis.com/).

Here's a summary of the original instructions:

- The user can input a distance
- The user can input two speeds
- The user can choose between three cars
  with the following fuel consumptions at 1 km/h:
  - Car A: 3 liters / 100km
  - Car B: 3.5 liters / 100km
  - Car C: 4 liters / 100km
- As speed increases by 1 km/h,
  fuel consumption is multiplied by 1.009
- The app shows the travel time and fuel consumption for both speeds
  and also the differences between the values
- Tech stack can be chosen freely
- Back-end is optional
- Usage of third party libraries and services
  that perform the comparisons
  is forbidden

[Original instructions (in Finnish) on Internet Archive &rarr;](https://web.archive.org/web/20210521044322if_/https://koodihaaste.solidabis.com/)

## Diary

- 1×🍅 = 1 pomodoro = ~30 minutes
- Total time spent: 33×🍅 = ~16.5 hours

### 2021-05-24: 🍅

Set up the project.

### 2021-05-25: 🍅🍅

Got distracted looking for a nice Clojure IDE.

Previously I have used [Cursive IDE](https://cursive-ide.com/)
when doing Clojure exercises on [Exercism](https://exercism.org/).
Cursive uses Paredit for "structural editing."
I'm sure it's very handy and useful,
but it seems too much of a hassle to set up,
especially as I'm also using IdeaVim (Vim plugin).
(Update: see [2021-05-30](#2021-05-30-).)

I think I'll try:

- [Atom](https://atom.io/) (code editor)
- vim-mode-plus (Vim plugin)
- Chlorine (Clojure REPL plugin)
- [Parinfer](https://shaunlebron.github.io/parinfer/)
  (plugin which simplifies writing and editing Lisp code like Clojure).

### 2021-05-26: 🍅🍅

Finished configuring Atom for now.
So far so good!

Also [asked about Reagent template's license file](https://github.com/reagent-project/reagent-template/issues/182).

### 2021-05-27: 🍅🍅🍅

Committed this readme file.
(I have been updating this every day.)

Removed the Reagent template's license file
by [editing the initial commit](https://stackoverflow.com/a/2309391/1079869)
and added my own license file.

Set up [Twind, a Tailwind-in-JS library](https://twind.dev/).
I first tried to install it via npm and load it in `core.cljs`:

```clj
(ns need-for-speed.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      ["twind/shim"])) ;; <- Same as `import 'twind/shim'` in JS
```

But for some reason I got errors in the browser's console:

```
app.js:1551
An error occurred when loading need_for_speed.core.js

app.js:1552
TypeError: (0 , module.pathToFileURL) is not a function
    at Object.shadow$provide.module$node_modules$twind$shim$shim_cjs (:3000/js/cljs-runtime/module$node_modules$twind$shim$shim_cjs.js:3)
    at Object.shadow.js.jsRequire (:3000/js/cljs-runtime/shadow.js.js:34)
    at Object.shadow.js.require (:3000/js/cljs-runtime/shadow.js.js:59)
    at eval (:3000/js/cljs-runtime/need_for_speed.core.js:2)
    at eval (<anonymous>)
    at Object.goog.globalEval (app.js:486)
    at Object.env.evalLoad (app.js:1549)
    at app.js:1733

(index):18
Uncaught TypeError: need_for_speed.core.init_BANG_ is not a function
    at localhost/:18
```

So that's why I'm loading it via a CDN for now.

Started implementing the UI:

![First progress picture showing a very crude UI. Not beautiful!](./docs/progress-pic-1.png)

Beautiful. 😂

### 2021-05-28: 🍅

Started handling form elements' state in React/Reagent.

### 2021-05-29: 🍅🍅🍅🍅

Finished handling form elements' state in React/Reagent.

Created initial component for showing the calculation results.

Split code into multiple files.

### 2021-05-30: 😴

Didn't work on the project on this day.
Sundays are for sleeping and resting
(thus the sleeping emoji).

I wrote before (see 2021-05-25)
that I'd like to use Cursive IDE,
but chose Atom because Atom has a Parinfer plugin,
and Cursive only uses Paredit (built in).

But I was wrong:
[Cursive supports also Parinfer out of the box](https://mtsknn.fi/weekly-log/2021/21/#clojure-cursive-ide-supports-parinfer-out-of-the-box).
There's just no mention of Parinfer _anywhere_ in the Cursive docs. 🤦‍♂️

Anyway,
I'm going to stick with Atom in this project.
I might use Cursive for future projects
because I liked it when I used it earlier.
But I like Atom as well... 🤔

### 2021-05-31: 🍅🍅

Cleaned up code.

Started showing diffs (e.g. "-3.12 liters" or "+0.22 hours") in results.
Had trouble with using JavaScript's
[`Number.toLocaleString()`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/toLocaleString).
Gotta continue tomorrow.

### 2021-06-01: 🍅🍅

Got number formatting working!
Not sure what was wrong yesterday
because today my code is quite much the same. 😅
Anyway, here's what's working:

```clj
(.toLocaleString 1234 "en" #js {:signDisplay "exceptZero"})
;; "+1,234"
```

### 2021-06-02: 🍅🍅

Improved number formatting
and cleaned up code.

Started formatting times,
e.g. "1.67 hours" → "1 hour 40 minutes."

### 2021-06-03: 🍅🍅🍅

Finished formatting times
and cleaned up code.

Started styling the UI.

### 2021-06-04: 😴

Busy day so didn't work on the project.

### 2021-06-05: 🍅🍅🍅🍅🍅🍅

Continued styling the UI...
lol why did I spend so much time on this?

### 2021-06-06: 🍅🍅🍅🍅🍅

Final day!

- Finished UI styling.
- Improved input handling.
- Wrote more stuff to this readme file.
- Published the app on Netlify.

Then I noticed that the production builds don't work. 😅
This code:

```clj
(ns need-for-speed.form
  (:require
   [goog.string :as gstring]))

;; Definition of `key` and `consumption` not shown here
(gstring/format "Car %s (%.1f liters/100 km at 1 km/h)" (name key) consumption)]])))
```

...caused these errors in the console:

> Uncaught TypeError: ka.format is not a function

...and `ka.format` referred to `gstring/format`.

No idea why and there was no time to investigate. 🤔
So I replaced `gstring/format` with `str`.

I'd really like to do at least some unit tests
but I ran out of time. 🙈
Maybe I'll do them afterwards.
**Edit**:
I spent 1×🍅 trying to create tests,
but received the following error when running the initial tests in Atom:

> TypeError: "cljs.test is undefined"

All right, no tests then. ¯\\(ツ)/¯ 😀

## Issues

### Number diffs are sometimes off

E.g. with distance 3,500 and speeds 110 and 95:

- Fuel consumptions per 100km are 7.97 and 6.96 liters.
  The diff is shown as plus/minus 1.00 liter,
  but it should be 0.01 liters more.
- The times are 31 h 49 min and 36 h 51 min.
  The diff is shown as plus/minus 5 h 1 min,
  but it should be one minute more.

The fix might be as simple as
storing and handling
the fuel amounts as centiliters
and the times as minutes.
Before showing them in the UI,
the centiliters could be divided by 100
and the minutes by 60.
