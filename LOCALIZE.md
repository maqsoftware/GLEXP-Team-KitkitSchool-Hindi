## Steps to localize instructions
1. Add a new `productFlavor` in the [build.gradle](https://github.com/maqsoftware/Pehla-School/blob/newmaster/pehlaschool/proj.android-studio/app/build.gradle) file along with `applicationSuffixId` property in it.
2. Add the new language in the `localeType` enum in LanguageManager.hpp file.
3. Add a key for the new language specific strings in LanguageManager.cpp file.
4. Add a case statement in the `getLocalizedString()` method for a new language. Also, add a case statement for the new language in the files viz. `CompletePopup.cpp, TodoLoadingScene.cpp, NumberMatchingScene.cpp, MultipleChoicesScene.cpp, LetterMatchingScene.cpp, FillTheBlanksScene.cpp, LetterTraceMainDepot.cpp, ReorderingScene.cpp, CompTraceScene.cpp, MatchingScene.cpp, LRDividedTypeReorderingScene.cpp, LRDividedTypeFillTheBlanksScene.cpp, LRAllInOneTypeQuestionScene.cpp, LRDividedTypeQuestionScene.cpp, LRComprehensionScene.cpp, WordTraceMainDepot.cpp, AnswerPadMulti.cpp, AnswerPadSingle.cpp, GradeSelector.cpp.`
5. Localize the [game tutorial](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/localized/tutorialvideo) videos.
6. [curriculumdata_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/curriculumdata_en.tsv) file contains mapping of game names for different days. It contains titles like "PreSchool", "English 1" or "Math 3".
7. [eggquizliteracy_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/games/eggquiz/eggquizliteracy_levels_en.tsv) and [eggquizmath_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/games/eggquiz/eggquizmath_levels_en.tsv) files contain instructions for the questions to be answered in the quiz sections.
8. [wordwindow_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/games/wordwindow/wordwindow_level_en.tsv) contains word problems for doing math.
9. Add locale specific splash screen and falling Pehla School logo images in [localized->system](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/localized/system) and [main->system](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/main/system) folders respectively.
10. Localize all the resources in [res](https://github.com/maqsoftware/Pehla-School/tree/newmaster/pehlaschool/proj.android-studio/app/src/english/res) folder and create a new folder for the new language.
11. Add a new translation string to the vector in the CPP map initialization and a condition for the new language below it, in the `DailyScene2`.cpp file.
12. Update TTS locale settings according to the new language in `VoiceMoldManager.java` file.

## Steps to localize the entire application

### Pehla School

* #### Launcher module
	It is the entry point to various modules in the app.
    1. Translate strings present in all the strings.xml file present in the project.
    2. Create an [application logo](https://github.com/maqsoftware/Pehla-School/blob/newmaster/pehlaschool/proj.android-studio/app/src/hindi/res/drawable-nodpi/launcher_pehlaschool_logo.png) with translated text in it.

* #### Learning module
	It has a tailored curriculum for English and Mathematics.  
  1. Translate all the audio and TSV files present in [Resources/localized](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/localized) folder.
  2. Add locale specific splash screen and falling Pehla School logo images in [localized->system](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/localized/system) and [main->system](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/main/system) folders respectively.

* #### Writing Board module
  It uses images containing alphabets and words and a user is expected to trace them to learn to write.
  1.  Update the images in [assets/image](https://github.com/maqsoftware/Pehla-School/tree/newmaster/pehlaschool/proj.android-studio/app/src/main/assets/image/en-us) folder with translated text in them.
---
### Pehla School Library
It is a collection of storybooks and videos for reading and entertainment.
1. Translate the content of [strings.xml](https://github.com/maqsoftware/Pehla-School/blob/newmaster/pehlaschoollibrary/proj.android-studio/app/res/values/strings.xml) file in the [Pehla School Library](https://github.com/maqsoftware/Pehla-School/tree/newmaster/pehlaschoollibrary/) project.
  2. Images and videos which are specific to a language in the [Pehla-School-Library-Asset](https://github.com/maqsoftware/Pehla-School-Hindi-Assets-Library/tree/master/en-us) should be translated.
___
### Voice engine - Does not support localization
