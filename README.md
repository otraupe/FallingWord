# FallingWord
Small language learning Android app gamifying vocabulary training

## What FallingWord does
This app was created for a code challenge. It is a small game for vocabulary training. It displays an English word together with a Spanish word "falling" down the screen. The falling animation represents a timer running out. The Spanish word may or may not be the correct translation. The user decides on its correctness by swiping it off the screen either to the right (correct) or to the left (incorrect). The remaining timer milliseconds are counted as score or penalty depending on the response being correct. The timer running out also incurs a slight penalty.

This way the app encourages to answer while training the user to balance response speed and correctness. The next trial is started automatically imposing a constant time pressure supposed to resemble a real language using scenario.

## Tech stack
The app is written in Kotlin following Clean architecture principles (organizing the code base into data, domain, and ui packages) and using the MVVM architecture pattern. I further uses:

- [Jetpack Compose](https://developer.android.com/jetpack/compose) for creating the UI
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) for updating the UI
- [Lifecycle-aware Coroutine Scopes](https://developer.android.com/topic/libraries/architecture/coroutines) for asynchronous work
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection

## Limitations and future improvements
The code challenge included a coding time target of 4h with a hard max. of 8h. As tasks such as retrieving the vocabulary data via REST calls is trivial, the emphasis of the development lay on the UI and gamification side. The goal was to create something usable while being at least a little bit fun. This app was developed in close to 8h.

There are many areas of improvement for future development, such as (in no particular order):
- use an alternative to SwipeToDismiss as it does not seem to play overly well with AnimatedVisibility
- prepare the app to deal with different languages
- implement a proper local and remote data sources
- REST calls for remote data
- display a score board
- use navigation and a default route for bringing up the main view
- use a db for storing scores and stats (such as average correctness, response speed etc.)
- use a scaffold for a more rounded UI
- implement an app icon and use the splash screen API
- style the app better (use a nice color theme, improve dark theme)
- display sounds (while word falling, on responses)
- spend some time on landscape view and various device sizes
- use proper compose animation instead of the countdown timer animating the falling word (tends to stutter a little)
- offer bottom corner buttons to swipe/dismiss the falling word (improved accessibility):
- prevent repetition of same words
- define an end of the game (training session length; or just display all words of a certain category)
- make the app survive configuration changes
- develop an audio Version for the hearing impaired
- improve text scaling (target word, falling word)
- fix a bug: sometimes the score animation is not shown completely
- fix a bug: clicking the start button quickly breaks the UI
- increase the unit test coverage
- implement UI test

## Licensing
This software is free for anyone to use as they seem fit.
