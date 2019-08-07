## Steps to localize only instructions
1. Add a new `productFlavor` in the [build.gradle](https://github.com/maqsoftware/Pehla-School/blob/newmaster/pehlaschool/proj.android-studio/app/build.gradle) file along with `applicationSuffixId` property in it.
2. Add the new language in the `localeType` enum in LanguageManager.hpp file.
3. Add a case statement in the `getLocalizedString()` method for a new language. Also, add a case statement for the new language in the files viz. `CompletePopup.cpp, TodoLoadingScene.cpp, NumberMatchingScene.cpp, MultipleChoicesScene.cpp, LetterMatchingScene.cpp, FillTheBlanksScene.cpp, LetterTraceMainDepot.cpp, ReorderingScene.cpp, CompTraceScene.cpp, MatchingScene.cpp, LRDividedTypeReorderingScene.cpp, LRDividedTypeFillTheBlanksScene.cpp, LRAllInOneTypeQuestionScene.cpp, LRDividedTypeQuestionScene.cpp, LRComprehensionScene.cpp, WordTraceMainDepot.cpp, AnswerPadMulti.cpp, AnswerPadSingle.cpp, GradeSelector.cpp.`
4. Localize the [game tutorial](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/localized/tutorialvideo) videos.
4. [curriculumdata_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/curriculumdata_en.tsv) file contains mapping of game names for different days. It contains titles like "PreSchool", "English 1" or "Math 3" that can be localized.
5. [eggquizliteracy_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/games/eggquiz/eggquizliteracy_levels_en.tsv) and [eggquizmath_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/games/eggquiz/eggquizmath_levels_en.tsv) files contains instructions for the questions to be answered in the quiz sections that can be localized.
6. [wordwindow_levels_en.tsv](https://github.com/maqsoftware/Pehla-School-Assets/blob/master/localized/games/wordwindow/wordwindow_level_en.tsv) contains word problems for doing math that can be localized.
7. Add locale specific splash screen and falling Pehla School logo images in [localized->system](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/localized/system) and [main->system](https://github.com/maqsoftware/Pehla-School-Assets/tree/master/main/system) folders respectively.
8. Localize all the resources in [res](https://github.com/maqsoftware/Pehla-School/tree/newmaster/pehlaschool/proj.android-studio/app/src/english/res) folder and create a new folder the new language.
9. Add a new translation string to the vector in the CPP map initialization and a condition for the new language below it in the `DailyScene2`.cpp file.
10. Update the TTS locale settings according to the new language in VoiceMoldManager.java.

## Steps to localize the entire application

### Pehla School

* #### Launcher module
1. The Launcher application is used for launching different applications in Pehla School world.
2. The [strings.xml](https://github.com/XPRIZE/GLEXP-Team-Pehla School/blob/master/launcher/app/src/main/res/values/strings.xml) file defines the strings being used in the Launcher to display names for various elements. Change the value for each element in this file except for the following element:  
    `<string name="app_name">Kitkit Launcher</string>`

* #### Learning module
1. The Main App is the primary learning application in Pehla School with a tailored curriculum. This application consists of content for literacy and numeracy training.  
2. Each section of the application consists of section-specific audios, videos and images. These assets can be localized as follows:  
    1. Set locale in the [configuration.xml](https://github.com/XPRIZE/GLEXP-Team-Pehla School/blob/master/Pehla Schoollogger/kitkitlogger/src/main/res/values/configuration.xml) file.  
    2. PocketSphinx library is being used for speech recognition. Languages supported by PocketSphinx can be found [here](https://cmusphinx.github.io/wiki/download/). Replace the contents in [assets](https://github.com/XPRIZE/GLEXP-Team-Pehla School/tree/master/mainapp/proj.android-studio/models/src/main/assets) folder with a supported language.
    3. Replace content in the [Resources](https://github.com/XPRIZE/GLEXP-Team-Pehla School/tree/master/mainapp/Resources) folder with locale-specific content.  

* #### Writing Board module
  Writing Board application uses images containing alphabets and words and the user is expected to trace them to learn to write. For localizing this application, the images containing alphabets and words need to be replaced with locale-specific images as follows:
  
  1.  Change the language at following places in the _onCreate_ function of [MainActivity.java](https://github.com/XPRIZE/GLEXP-Team-Pehla School/blob/master/writingboard/app/src/main/java/com/enuma/writingboard/activity/MainActivity.java) file:  
          `mAppLanguage = extras.getString("LANGUAGE", "sw-TZ").toLowerCase();`
          `mAppLanguage = "sw-tz";`
     
       For example, replace `"sw-TZ"` with `"hi-IN"` and `"sw-tz"` with `"hi-in"` for Hindi India language.  

  2.  Create a new folder in [assets/image/](https://github.com/XPRIZE/GLEXP-Team-Pehla School/tree/master/writingboard/app/src/main/assets/image) with locale-specific language:
    
       For example, create a folder named _"hi-in"_ for Hindi India language.

  3.  Create locale-specific images similar to the images available in the [en-us](https://github.com/XPRIZE/GLEXP-Team-Pehla School/tree/master/writingboard/app/src/main/assets/image/en-us) folder and place them in the newly created locale-specific folder.
---
### Pehla School Library

  1. The Library application consists of a collection of story books and videos.  
  		1. A storybook is comprised of images located at [library/localized](https://github.com/XPRIZE/GLEXP-Team-Pehla School/tree/master/library/localized)/en-us/assets/en-us.  
  		2. The videos are located at [library/localized](https://github.com/XPRIZE/GLEXP-Team-Pehla School/tree/master/library/localized)/en-us/res/raw.  
        
      Replace these files with locale-specific content.  
  2. Strings are being used in the Library to display names for various elements. These can be localized by changing string values at the following locations:   
  		1. Change the value for each element in [strings.xml](https://github.com/XPRIZE/GLEXP-Team-Pehla School/blob/master/library/app/src/main/res/values/strings.xml) file except for the following element (i.e. the app title):  
  `<string name="app_name">Pehla School Library</string>`
  		2. Navigate to [activity_bookdetail.xml](https://github.com/XPRIZE/GLEXP-Team-Pehla School/blob/master/library/app/src/main/res/layout/activity_bookdetail.xml) file. Replace the value of `android:text` attribute in TextView element with the localized text.
___
### Voice Engine - Does not support localization
