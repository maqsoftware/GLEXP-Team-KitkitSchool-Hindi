//
//  LanguageManager.cpp
//  PehlaSchool
//
//  Created by Sungwoo Kang on 6/30/16.
//
//

#include "LanguageManager.hpp"
#include "cocos2d.h"
#include <utility>

USING_NS_CC;
using namespace std;

LanguageManager* LanguageManager::_instance = 0;

LanguageManager* LanguageManager::getInstance()
{
    if(!_instance) {
        _instance = new LanguageManager();
        _instance->init();
    }
    return _instance;
}


void LanguageManager::init()
{
#if false
    auto defaultLang = LanguageType::ENGLISH;
#endif
    std::string localeCode = "en-US";
    auto localeType = convertLocaleCodeToType(localeCode);
    if (localeType>=LocaleType_MAX) localeType = en_US;

    _supportedLocales.clear();
    for (int i=0; i<LocaleType_MAX; i++) {
        LocaleType l = (LocaleType)i;
        auto lc = convertLocaleTypeToCode(l);
        auto lp = "Localized/"+ lc + "/CurriculumData_" + lc.substr(0, 2) + ".tsv";
        if (FileUtils::getInstance()->isFileExist(lp)) _supportedLocales.push_back(l);

    }
    if (_supportedLocales.size() == 0) {
        CCLOGERROR("No curriculumdata.tsv is found for any language. check %sPehlaSchool/location.txt, which is currently refers to %s",
                   FileUtils::getInstance()->getWritablePath().c_str(),
                   FileUtils::getInstance()->getDefaultResourceRootPath().c_str());
        exit(1);
    } else{
        if (std::find(_supportedLocales.begin(), _supportedLocales.end(), localeType)==_supportedLocales.end()) {
            localeType = _supportedLocales.front();
        }
    }

    setCurrentLocale(localeType);

    initLocalizationMap();
}

LanguageManager::LocaleType LanguageManager::convertLocaleCodeToType(std::string localeCode)
{
    if (localeCode.length()<5) return LocaleType_MAX;
    auto lang = localeCode.substr(0, 2);
    auto region = localeCode.substr(3, 2);

    if (lang == "en" and region == "US") {
        return en_US;
    } else if (lang == "hi" and region == "IN") {
        return hi_IN;
    } else if (lang == "ur" and region == "IN") {
        return ur_IN;
    } else if (lang == "bn" and region == "IN") {
        return bn_IN;
    } else if (lang == "sw" and region == "TZ") {
        return sw_TZ;
    }

    return LocaleType_MAX;
}

std::string LanguageManager::convertLocaleTypeToCode(LanguageManager::LocaleType localeType)
{
    switch (localeType) {
        case en_US: return "en-US"; break;
        case hi_IN: return "hi-IN"; break;
        case ur_IN: return "ur-IN"; break;
        case bn_IN: return "bn-IN"; break;
        case sw_TZ: return "sw-TZ"; break;
        default: break;
    }

    return "en-US";

}

void LanguageManager::setCurrentLocale(LocaleType type)
{
    bool dirty = false;
    if (_currentLocale != type) dirty = true;


    _currentLocale = type;

    if (dirty) {
        auto langCode = convertLocaleTypeToCode(type);
        UserDefault::getInstance()->setStringForKey("LocaleCode", langCode);
        UserDefault::getInstance()->flush();
    }

    std::vector<std::string> paths = {};

    paths.push_back("localized/en-US");
    paths.push_back("localized/en-US/games");
    paths.push_back("games");
    paths.push_back("main");

    FileUtils::getInstance()->setSearchPaths(paths);
}

LanguageManager::LocaleType LanguageManager::getCurrentLocaleType()
{
    return _currentLocale;
}

LanguageManager::LocaleType LanguageManager::findNextLocale()
{
    LocaleType next = (LocaleType)((int)_currentLocale+1);

    while (next!=_currentLocale) {
        if (std::find(_supportedLocales.begin(), _supportedLocales.end(), next)!=_supportedLocales.end()) return next;
        next = (LocaleType)((int)next+1);
        if (next>=LocaleType_MAX) next = (LocaleType)0;

    }

    return _currentLocale;
}


std::string LanguageManager::getCurrentLanguageCode()
{

    std::string langCode = convertLocaleTypeToCode(_currentLocale);
    return langCode.substr(0, 2);

}

std::string LanguageManager::getCurrentLanguageTag()
{
    return getCurrentLocaleCode();
}

std::string LanguageManager::getCurrentLocaleCode()
{
    std::string langCode=convertLocaleTypeToCode(_currentLocale);
    return langCode;
}


std::string LanguageManager::soundPathForWordFile(std::string& wordFile)
{
    std::string folder;

    std::string path = findLocalizedResource("LetterVoice/"+wordFile);
    if (path!="") return path;
    path = findLocalizedResource("WordVoice/"+wordFile);
    if (path!="") return path;

    return "";

}

std::string LanguageManager::getLocalizedString(std::string str, bool isBilingualRequired)
{
    std::string localized;
    std::string delimiter =  "$#$";
    // todo July 21, 2019: change the map suffix to correct language once the translations are in place
    if (customLanguageCode == "en") {
        localized = _localizationMapEnglish[str];
    } else if (customLanguageCode == "hi") {
        localized = _localizationMapHindi[str];
    } else if (customLanguageCode == "ur") {
        localized = _localizationMapUrdu[str];
    } else if (customLanguageCode == "bn") {
        localized = _localizationMapBengali[str];
    } else if (customLanguageCode == "sw") {
        localized = _localizationMapSwahili[str];
    }
    if (localized.empty()) return str;

    if (isBilingualRequired && customLanguageCode != "en") {
        localized += delimiter + _localizationMapEnglish[str];
    }

    return localized;
}

std::string LanguageManager::findLocalizedResource(std::string path)
{

    // handled by Cocos...

    return path;
}

void LanguageManager::initLocalizationMap()
{
    string delm =  "$#$";
    _localizationMapEnglish["Stop the test"] = "Stop the test";
    _localizationMapHindi["Stop the test"] = "टेस्ट बन्द करिए";
//    _localizationMapUrdu["Stop the test"] = "";
//    _localizationMapBengali["Stop the test"] = "";
//    _localizationMapSwahili["Stop the test"] = "";

    _localizationMapEnglish["Go back to test"] = "Go back to test";
    _localizationMapHindi["Go back to test"] = "टेस्ट पर बापस जाइये";
//    _localizationMapUrdu["Go back to test"] = "";
//    _localizationMapBengali["Go back to test"] = "";
//    _localizationMapSwahili["Go back to test"] = "";

    _localizationMapEnglish["Great!"] = "Great!";
    _localizationMapHindi["Great!"] = "बहुत अच्छे!";
    // _localizationMapUrdu["Great!"] = "";
    _localizationMapBengali["Great!"] = "খুব ভালো";
    _localizationMapSwahili["Great!"] = "Vizuri!";

    _localizationMapEnglish["Are you ready for"] = "Are you ready for"; //
     _localizationMapHindi["Are you ready for"] = "क्या तुम तैयार हो";
    // _localizationMapUrdu["Are you ready for"] = "";
    _localizationMapBengali["Are you ready for"] = "আপনি পরীক্ষা জন্য প্রস্তুত?";
    _localizationMapSwahili["Are you ready for"] = "Je, uko tayari kwa";

    _localizationMapEnglish["Prove it!"] = "Prove it!";
    _localizationMapHindi["Prove it!"] = "साबित करो!";
    // _localizationMapUrdu["Prove it!"] = "";
    _localizationMapBengali["Prove it!"] = "প্রমান কর";
    _localizationMapSwahili["Prove it!"] = "Thibitisha!";

    _localizationMapEnglish["Try and get 8 questions correct!"] = "Try and get 8 questions correct!";
    _localizationMapHindi["Try and get 8 questions correct!"] = "कोशिश करो और आठ प्रशनो का सही जबाब दीजिये!";
    // _localizationMapUrdu["Try and get 8 questions correct!"] = "";
    _localizationMapBengali["Try and get 8 questions correct!"] = "চেষ্টা করুন এবং 8 টি প্রশ্ন সঠিক উত্তর দিন !";
    _localizationMapSwahili["Try and get 8 questions correct!"] = "Jaribu na toa majibu sahihi manane!";

    _localizationMapEnglish["Challenge"] = "Challenge";
    _localizationMapHindi["Challenge"] = "चुनौती";
    // _localizationMapUrdu["Challenge"] = "";
    _localizationMapBengali["Challenge"] = "চ্যালেঞ্জ";
    _localizationMapSwahili["Challenge"] = "Jaribu";

    _localizationMapEnglish["Congratulations!"] = "Congratulations!";
    _localizationMapHindi["Congratulations!"] = "बधाई हो!";
    // _localizationMapUrdu["Congratulations!"] = "";
    _localizationMapBengali["Congratulations!"] = "অভিনন্দন.";
    _localizationMapSwahili["Congratulations!"] = "Hongera!";

    _localizationMapEnglish["You passed!"] = "You passed!";
    _localizationMapHindi["You passed!"] = "तुम पास हो गये!";
    // _localizationMapUrdu["You passed!"] = "";
    _localizationMapBengali["You passed!"] = "পাশ করেছেন!";
    _localizationMapSwahili["You passed!"] = "Umefaulu!";

    _localizationMapEnglish["You failed"] = "You failed";
    _localizationMapHindi["You failed"] = "तुम असफल रहे";
    // _localizationMapUrdu["You failed"] = "";
    _localizationMapBengali["You failed"] = "আপনি অসফল হলেন ";
    _localizationMapSwahili["You failed"] = "Umeshindwa";

    _localizationMapEnglish["Practice more and try again later."] = "Practice more and try again later.";
    _localizationMapHindi["Practice more and try again later."] = "अधिक अभ्यास करें और बाद में फिर से प्रयास करें।";
    // _localizationMapUrdu["Practice more and try again later."] = "";
    _localizationMapBengali["Practice more and try again later."] = "আরো অনুশীলন করুন এবং পরে আবার চেষ্টা করুন।";
    _localizationMapSwahili["Practice more and try again later."] = "Fanya mazoezi zaidi na rudi baadae.";

    _localizationMapEnglish["Success!"] = "Success!";
    _localizationMapHindi["Success!"] = "सफलता!";
    // _localizationMapUrdu["Success!"] = "";
    _localizationMapBengali["Success!"] = "সফল!";
    _localizationMapSwahili["Success!"] = "Mafanikio!";

    _localizationMapEnglish["You are not ready."] =  "You are not ready.";
    _localizationMapHindi["You are not ready."] = "तुम तैयार नहीं हो।";
    // _localizationMapUrdu["You are not ready."] = "";
    _localizationMapBengali["You are not ready."] = "আপনি প্রস্তুত না।";
    _localizationMapSwahili["You are not ready."] = "Hauko tayari.";

    _localizationMapEnglish["You need more practice."] = "You need more practice.";
    _localizationMapHindi["You need more practice."] = "आपको और अभ्यास की आवश्यकता है।";
    // _localizationMapUrdu["You need more practice."] = "";
    _localizationMapBengali["You need more practice."] = "আপনি আরো অনুশীলন প্রয়োজন।";
    _localizationMapSwahili["You need more practice."] = "Unahitaji mazoezi zaidi.";

    _localizationMapEnglish["Welcome!"] = "Welcome!";
    _localizationMapHindi[""] = "स्वागत है!";
    // _localizationMapUrdu["Welcome!"] = "";
    _localizationMapBengali["Welcome!"] = "স্বাগত!";
    _localizationMapSwahili["Welcome!"] = "Karibu!";

    _localizationMapEnglish["Start"] = "Start";
    _localizationMapHindi["Start"] = "शुरु करो";
    // _localizationMapUrdu["Start"] = "";
    _localizationMapBengali["Start"] = "শুরু করুন।";
    _localizationMapSwahili["Start"] = "Anza";

    _localizationMapEnglish["Next"] = "Next";
    _localizationMapHindi["Next"] = "अगला";
    // _localizationMapUrdu["Next"] = "";
    _localizationMapBengali["Next"] = "পরবর্তী.";
    _localizationMapSwahili["Next"] = "Nenda mbele";

    _localizationMapEnglish["Back"] = "Back";
    _localizationMapHindi["Back"] = "पिछला";
    // _localizationMapUrdu["Back"] = "";
    _localizationMapBengali["Back"] = "পিছনে।";
    _localizationMapSwahili["Back"] = "Rudi nyuma";

    _localizationMapEnglish["OK"] = "OK";
    _localizationMapHindi["OK"] = "ओके";
    // _localizationMapUrdu["OK"] = "";
    _localizationMapBengali["OK"] = "ঠিক আছে.";
    _localizationMapSwahili["OK"] = "OK";

    _localizationMapEnglish["Error"] = "Error";
    _localizationMapHindi["Error"] = "त्रुटि";
    // _localizationMapUrdu["Error"] = "";
    _localizationMapBengali["Error"] = "ওকে";
    _localizationMapSwahili["Error"] = "Hitilafu";

    _localizationMapEnglish["Enter"] = "Start writing";
    _localizationMapHindi["Enter"] = "लिखना प्रारम्भ करें";
    // _localizationMapUrdu["Enter"] = "";
    _localizationMapBengali["Enter"] = "সন্নিবেশ করান।";
    _localizationMapSwahili["Enter"] = "Chomeka";

    _localizationMapEnglish["Clear"] = "Clear it";
    _localizationMapHindi["Clear"] = "साफ करे";
    // _localizationMapUrdu["Clear"] = "";
    _localizationMapBengali["Clear"] = "পরিষ্কার.";
    _localizationMapSwahili["Clear"] = "Futa";

    _localizationMapEnglish["English"] = "English";
    _localizationMapHindi["English"] = "अंग्रेज़ी";
    // _localizationMapUrdu["English"] = "";
    _localizationMapBengali["English"] = "ইংরেজি";
    _localizationMapSwahili["English"] = "Kiswahili";

    _localizationMapEnglish["Math"] = "Math";
    _localizationMapHindi["Math"] = "गणित";
    // _localizationMapUrdu["Math"] = "";
    _localizationMapBengali["Math"] = "ম্যাথ";
    _localizationMapSwahili["Math"] = "Hesabu";

    _localizationMapEnglish["TutorialTrace"] = "Line Tracing";
    _localizationMapHindi["TutorialTrace"] = "लाइन खीचें";
    // _localizationMapUrdu["TutorialTrace"] = "";
    _localizationMapBengali["TutorialTrace"] = "লাইন ট্রেসিং";
    _localizationMapSwahili["TutorialTrace"] = "Kufuatisha Mstari";

    _localizationMapEnglish["FindTheMatch"] = "Find The Match";
    _localizationMapHindi["FindTheMatch"] = "जोड़ी खोजिये";
    // _localizationMapUrdu["FindTheMatch"] = "";
    _localizationMapBengali["FindTheMatch"] = "ম্যাচ খুঁজুন";
    _localizationMapSwahili["FindTheMatch"] = "Tafuta Sare";

    _localizationMapEnglish["NumberMatching"] = "Number Matching";
    _localizationMapHindi["NumberMatching"] = "संख्या मिलाना";
    // _localizationMapUrdu["NumberMatching"] = "";
    _localizationMapBengali["NumberMatching"] = "সংখ্যা ম্যাচিং";
    _localizationMapSwahili["NumberMatching"] = "Kufananisha Nambari";

    _localizationMapEnglish["Tapping"] = "Bubble Pop";
    _localizationMapHindi["Tapping"] = "बबल पॉप";
    // _localizationMapUrdu["Tapping"] = "";
    _localizationMapBengali["Tapping"] = "বাবল পপ";
    _localizationMapSwahili["Tapping"] = "Pasua Povu la Sabuni";

    _localizationMapEnglish["LetterMatching"] = "Letter Matching";
    _localizationMapHindi["LetterMatching"] = "अक्षर मिलाना";
    // _localizationMapUrdu["LetterMatching"] = "";
    _localizationMapBengali["LetterMatching"] = "বর্ণমালা ম্যাচিং";
    _localizationMapSwahili["LetterMatching"] = "Kufananisha Kusoma na Kuandika";

    _localizationMapEnglish["AnimalPuzzle"] = "Animal Puzzle";
    _localizationMapHindi["AnimalPuzzle"] = "पशुओं की पहेली";
    // _localizationMapUrdu["AnimalPuzzle"] = "";
    _localizationMapBengali["AnimalPuzzle"] = "পশু পাজল";
    _localizationMapSwahili["AnimalPuzzle"] = "Fumbo la Picha";

    _localizationMapEnglish["PatternTrain"] = "Pattern Train";
    _localizationMapHindi["PatternTrain"] = "पैटर्न ट्रैन";
    // _localizationMapUrdu["PatternTrain"] = "";
    _localizationMapBengali["PatternTrain"] = "প্যাটার্ন ট্রেন";
    _localizationMapSwahili["PatternTrain"] = "Reli ya Garimoshi";

    _localizationMapEnglish["Video"] = "Video";
    _localizationMapHindi["Video"] = "वीडियो";
    // _localizationMapUrdu["Video"] = "";
    _localizationMapBengali["Video"] = "ভিডিও";
    _localizationMapSwahili["Video"] = "Video";

    _localizationMapEnglish["Counting"] = "Counting";
    _localizationMapHindi["Counting"] = "गिनती";
    // _localizationMapUrdu["Counting"] = "";
    _localizationMapBengali["Counting"] = "গণনাকারী";
    _localizationMapSwahili["Counting"] = "Kuhesabu";

    _localizationMapEnglish["EquationMaker"] = "Equation Maker";
    _localizationMapHindi["EquationMaker"] = "समीकरण बनाने वाला";
    // _localizationMapUrdu["EquationMaker"] = "";
    _localizationMapBengali["EquationMaker"] = "সমীকরণ মেকার";
    _localizationMapSwahili["EquationMaker"] = "Kiumba Mlinganyo";

    _localizationMapEnglish["NumberTrain"] = "Number Train";
    _localizationMapHindi["NumberTrain"] = "संख्या ट्रेन";
    // _localizationMapUrdu["NumberTrain"] = "";
    _localizationMapBengali["NumberTrain"] = "নম্বর ট্রেন";
    _localizationMapSwahili["NumberTrain"] = "Nambari ya Garimoshi";

    _localizationMapEnglish["AlphabetPuzzle"] = "Alphabet Puzzle";
    _localizationMapHindi["AlphabetPuzzle"] = "वर्णमाला की पहेली";
    // _localizationMapUrdu["AlphabetPuzzle"] = "";
    _localizationMapBengali["AlphabetPuzzle"] = "বর্ণমালা পাজল";
    _localizationMapSwahili["AlphabetPuzzle"] = "Fumbo la Alfabeti";

    _localizationMapEnglish["Book"] = "Book";
    _localizationMapHindi["Book"] = "किताब";
    // _localizationMapUrdu["Book"] = "";
    _localizationMapBengali["Book"] = "বই";
    _localizationMapSwahili["Book"] = "Kitabu";

    _localizationMapEnglish["Comprehension"] = "Comprehension";
    _localizationMapHindi["Comprehension"] = "समझ बूझ";
    // _localizationMapUrdu["Comprehension"] = "";
    _localizationMapBengali["Comprehension"] = "কম্প্রিহেনশন";
    _localizationMapSwahili["Comprehension"] = "Maswali ya Ufahamu";

    _localizationMapEnglish["DoubleDigit"] = "Double Digit Math";
    _localizationMapHindi["DoubleDigit"] = "दो अंको की गणित";
    // _localizationMapUrdu["DoubleDigit"] = "";
    _localizationMapBengali["DoubleDigit"] = "ডাবল ডিজিট গণিত";
    _localizationMapSwahili["DoubleDigit"] = "Hisabati ya Tarakimu Mbili";

    _localizationMapEnglish["FishTank"] = "Fish Tank";
    _localizationMapHindi["FishTank"] = "मछली घर";
    _localizationMapBengali["FishTank"] = "মাছের ট্যাঁক";
    _localizationMapSwahili["FishTank"] = "Tangi ya Samaki";

    _localizationMapEnglish["HundredPuzzle"] = "100 Puzzle";
    _localizationMapHindi["HundredPuzzle"] = "100 पहेली";
    // _localizationMapUrdu["HundredPuzzle"] = "";
    _localizationMapBengali["HundredPuzzle"] = "100 পাজল";
    _localizationMapSwahili["HundredPuzzle"] = "Fumbo la Nambari 100";

    _localizationMapEnglish["LetterTrace"] = "Letter Trace";
    _localizationMapHindi["LetterTrace"] = "अक्षर बनाएं";
    // _localizationMapUrdu["LetterTrace"] = "";
    _localizationMapBengali["LetterTrace"] = "বর্ণমালা ট্রেস";
    _localizationMapSwahili["LetterTrace"] = "Kufuatisha Herufi";

    _localizationMapEnglish["MovingInsects"] = "Moving Insects";
    _localizationMapHindi["MovingInsects"] = "कीड़े की गिनती";
    // _localizationMapUrdu["MovingInsects"] = "";
    _localizationMapBengali["MovingInsects"] = "বাগ ম্যাথ";
    _localizationMapSwahili["MovingInsects"] = "Mchezo wa Mdudu";

    _localizationMapEnglish["SentenceMaker"] = "Sentence Maker";
    _localizationMapHindi["SentenceMaker"] = "वाक्य बनाने वाला";
    // _localizationMapUrdu["SentenceMaker"] = "";
    _localizationMapBengali["SentenceMaker"] = "বাক্য মেকার";
    _localizationMapSwahili["SentenceMaker"] = "Kiumba Sentensi";

    _localizationMapEnglish["ShapeMatching"] = "Shape Matching";
    _localizationMapHindi["ShapeMatching"] = "आकार मिलाना";
    // _localizationMapUrdu["ShapeMatching"] = "";
    _localizationMapBengali["ShapeMatching"] = "আকার ম্যাচিং";
    _localizationMapSwahili["ShapeMatching"] = "Kufananisha Maumbo";

    _localizationMapEnglish["SoundTrain"] = "Sound Train";
    _localizationMapHindi["SoundTrain"] = "ध्वनि ट्रेन";
    // _localizationMapUrdu["SoundTrain"] = "";
    _localizationMapBengali["SoundTrain"] = "সাউন্ড ট্রেন";
    _localizationMapSwahili["SoundTrain"] = "Sauti ya Garimoshi";

    _localizationMapEnglish["Spelling"] = "Spelling";
    _localizationMapHindi["Spelling"] = "वर्तनी";
    // _localizationMapUrdu["Spelling"] = "";
    _localizationMapBengali["Spelling"] = "বানান";
    _localizationMapSwahili["Spelling"] = "Matamshi";

    _localizationMapEnglish["WordTracing"] = "Word Tracing";
    _localizationMapHindi["WordTracing"] = "शब्द अनुरेखण";
    // _localizationMapUrdu["WordTracing"] = "";
    _localizationMapBengali["WordTracing"] = "শব্দ ট্রেসিং";
    _localizationMapSwahili["WordTracing"] = "Kufuatisha Maneno";

    _localizationMapEnglish["NumberTracing"] = "Number Tracing";
    _localizationMapHindi["NumberTracing"] = "संख्या अनुरेखण";
    // _localizationMapUrdu["NumberTracing"] = "";
    _localizationMapBengali["NumberTracing"] = "সংখ্যা ট্রেসিং";
    _localizationMapSwahili["NumberTracing"] = "Jifunze Mpaka 10";

    _localizationMapEnglish["StarFall"] = "Typing";
    _localizationMapHindi["StarFall"] = "टाइपिंग";
    // _localizationMapUrdu["StarFall"] = "";
    _localizationMapBengali["StarFall"] = "টাইপিং";
    _localizationMapSwahili["StarFall"] = "Kuchapa";

    _localizationMapEnglish["WordMachine"] = "Word Machine";
    _localizationMapHindi["WordMachine"] = "शब्दों की मशीन";
    // _localizationMapUrdu["WordMachine"] = "";
    _localizationMapBengali["WordMachine"] = "শব্দ মেশিন";
    _localizationMapSwahili["WordMachine"] = "Mashine ya Maneno";

    _localizationMapEnglish["NumberTracingExt"] = "Number Tracing";
    _localizationMapHindi["NumberTracingExt"] = "संख्या अनुरेखण";
    // _localizationMapUrdu["NumberTracingExt"] = "";
    _localizationMapBengali["NumberTracingExt"] = "সংখ্যা ট্রেসিং";
    _localizationMapSwahili["NumberTracingExt"] = "Kufuatisha Nambari";

    _localizationMapEnglish["LetterTracingCard"] = "Letter Tracing Card";
    _localizationMapHindi["LetterTracingCard"] = "अक्षर ट्रेसिंग कार्ड";
    // _localizationMapUrdu["LetterTracingCard"] = "";
    _localizationMapBengali["LetterTracingCard"] = "বর্ণমালা ট্রেসিং কার্ড";
    _localizationMapSwahili["LetterTracingCard"] = "Fuatisha Mara 3";

    _localizationMapEnglish["NumberPuzzle"] = "Number Puzzle";
    _localizationMapHindi["NumberPuzzle"] = "संख्यायों की पहेलियाँ";
    // _localizationMapUrdu["NumberPuzzle"] = "";
    _localizationMapBengali["NumberPuzzle"] = "সংখ্যা ব্লক";
    _localizationMapSwahili["NumberPuzzle"] = "Fumbo la Nambari";

    _localizationMapEnglish["Arrange the numbers in order from smallest to largest"] = "Arrange the numbers in order from smallest to largest.";
    _localizationMapHindi["Arrange the numbers in order from smallest to largest"] = "छोटी से लेकर बड़ी अंक के क्रम में संख्याओं की क्रमबद्ध करें।";
    // _localizationMapUrdu["Arrange the numbers in order from smallest to largest"] = "";
    _localizationMapBengali["Arrange the numbers in order from smallest to largest"] = "ছোট থেকে বৃহত্তম করার জন্য সংখ্যা সাজান";
    _localizationMapSwahili["Arrange the numbers in order from smallest to largest"] = "panga kwa mpangilio kutoka ndogo zaidi kwenda kubwa zaidi";

    _localizationMapEnglish["Largest number"] = "Largest number";
    _localizationMapHindi["Largest number"] = "सबसे बड़ी संख्या";
    // _localizationMapUrdu["Largest number"] = "";
    _localizationMapBengali["Largest number"] = "বৃহত্তম সংখ্যা";
    _localizationMapSwahili["Largest number"] = "Namba ipi ndiyo kubwa zaidi?";

    _localizationMapEnglish["BirdPhonics"] = "Bird Phonics";
    _localizationMapHindi["BirdPhonics"] = "पक्षी की ध्वनि";
    // _localizationMapUrdu["BirdPhonics"] = "";
    _localizationMapBengali["BirdPhonics"] = "বার্ড ফনিক্স ";
    _localizationMapSwahili["BirdPhonics"] = "Sauti Ndege";

    _localizationMapEnglish["FeedingTime"] = "Feeding Time";
    _localizationMapHindi["FeedingTime"] = "खिलाने का समय";
    // _localizationMapUrdu["FeedingTime"] = "";
    _localizationMapBengali["FeedingTime"] = "খাওয়ানোর সময়";
    _localizationMapSwahili["FeedingTime"] = "Wakati wa kula";

    _localizationMapEnglish["LineMatching"] = "Line Matching";
    _localizationMapHindi["LineMatching"] = "रेखा मिलान";
    // _localizationMapUrdu["LineMatching"] = "";
    _localizationMapBengali["LineMatching"] = "লাইন ম্যাচিং";
    _localizationMapSwahili["LineMatching"] = "Linganisha mistari";

    _localizationMapEnglish["MangoShop"] = "Mango Shop";
    _localizationMapHindi["MangoShop"] = "आम की दुकान";
    // _localizationMapUrdu["MangoShop"] = "";
    _localizationMapBengali["MangoShop"] = "আমের দোকান";
    _localizationMapSwahili["MangoShop"] = "Duka la embe";

    _localizationMapEnglish["MissingNumber"] = "Missing Number";
    _localizationMapHindi["MissingNumber"] = "अनुपस्थित संख्या";
    // _localizationMapUrdu["MissingNumber"] = "";
    _localizationMapBengali["MissingNumber"] = "অনুপস্থিত সংখ্যা";
    _localizationMapSwahili["MissingNumber"] = "Namba iliyopotea";

    _localizationMapEnglish["ReadingBird"] = "Reading Bird";
    _localizationMapHindi["ReadingBird"] = "पढ़ने वाला पक्षी";
    // _localizationMapUrdu["ReadingBird"] = "";
    _localizationMapBengali["ReadingBird"] = "পাখি পড়া";
    _localizationMapSwahili["ReadingBird"] = "Ndege anayesoma";

    _localizationMapEnglish["WhatIsThis"] = "What Is This?";
    _localizationMapHindi["WhatIsThis"] = "यह क्या है?";
    // _localizationMapUrdu["WhatIsThis"] = "";
    _localizationMapBengali["WhatIsThis"] = "এটা কি?";
    _localizationMapSwahili["WhatIsThis"] = "Hii ni nini?";

    _localizationMapEnglish["ThirtyPuzzle"] = "30 Puzzle";
    _localizationMapHindi["ThirtyPuzzle"] = "30 पहेली";
    // _localizationMapUrdu["ThirtyPuzzle"] = "";
    _localizationMapBengali["ThirtyPuzzle"] = "30 পাজল";
    _localizationMapSwahili["ThirtyPuzzle"] = "Panga Namba";

    _localizationMapEnglish["WordNote"] = "Word Note";
    _localizationMapHindi["WordNote"] = "शब्द नोट";
    // _localizationMapUrdu["WordNote"] = "";
    _localizationMapBengali["WordNote"] = "শব্দ নোট";
    _localizationMapSwahili["WordNote"] = "Tunga neno";

    _localizationMapEnglish["QuickFacts"] = "Quick Facts";
    _localizationMapHindi["QuickFacts"] = "तुरन्त तथ्य";
    // _localizationMapUrdu["QuickFacts"] = "";
    _localizationMapBengali["QuickFacts"] = "দ্রুত ঘটনা";
    _localizationMapSwahili["QuickFacts"] = "Ukweli wa Uhakika";

    _localizationMapEnglish["MultiplicationBoard"] = "Multiplication Lamp";
    _localizationMapHindi["MultiplicationBoard"] = "गुणा पट्ट";
    // _localizationMapUrdu["MultiplicationBoard"] = "";
    _localizationMapBengali["MultiplicationBoard"] = "গুণমান ল্যাম্প";
    _localizationMapSwahili["MultiplicationBoard"] = "Taa ya Kuzidishia";

    _localizationMapEnglish["WordMatrix"] = "Word Matrix";
    _localizationMapHindi["WordMatrix"] = "शब्द आव्यूह";
    // _localizationMapUrdu["WordMatrix"] = "";
    _localizationMapBengali["WordMatrix"] = "শব্দ ম্যাট্রিক্স";
    _localizationMapSwahili["WordMatrix"] = "Chanzo cha Neno";

    _localizationMapEnglish["SentenceBridge"] = "Sentence Bridge";
    _localizationMapHindi["SentenceBridge"] = "वाक्यों का पुल";
    // _localizationMapUrdu["SentenceBridge"] = "";
    _localizationMapBengali["SentenceBridge"] = "বাক্য ব্রিজ";
    _localizationMapSwahili["SentenceBridge"] = "Daraja la Sentensi";

    _localizationMapEnglish["WordWindow"] = "Word Window";
    _localizationMapHindi["WordWindow"] = "वर्ड विंडो";
    // _localizationMapUrdu["WordWindow"] = "";
    _localizationMapBengali["WordWindow"] = "শব্দ উইন্ডো";
    _localizationMapSwahili["WordWindow"] = "Dirisha la Neno";

    _localizationMapEnglish["WordKicker"] = "Word Kicker";
    _localizationMapHindi["WordKicker"] = "शब्द किकर";
    // _localizationMapUrdu["WordKicker"] = "";
    _localizationMapBengali["WordKicker"] = "শব্দ কিকের";
    _localizationMapSwahili["WordKicker"] = "Mpigo wa Neno";

    _localizationMapEnglish["MathKicker"] = "Math Kicker";
    _localizationMapHindi["MathKicker"] = "मैथ्स किकर";
    // _localizationMapUrdu["MathKicker"] = "";
    _localizationMapBengali["MathKicker"] = "স্থানিক কিকের";
    _localizationMapSwahili["MathKicker"] = "Mpigo wa Hisabati";

    _localizationMapEnglish["PlaceValue"] = "Place Value";
    _localizationMapHindi["PlaceValue"] = "मान रखे";
    // _localizationMapUrdu["PlaceValue"] = "";
    _localizationMapBengali["PlaceValue"] = "স্থানিক মূল্য";
    _localizationMapSwahili["PlaceValue"] = "Fungu la Thamani";

    _localizationMapEnglish["Labeling"] = "Labeling";
    _localizationMapHindi["Labeling"] = "अंकितक लगाना";
    // _localizationMapUrdu["Labeling"] = "";
    _localizationMapBengali["Labeling"] = "লেবেল";
    _localizationMapSwahili["Labeling"] = "Pachika Jina";

    _localizationMapEnglish["LRComprehension"] = "Comprehension";
    _localizationMapHindi["LRComprehension"] = "समझ बूझ";
    // _localizationMapUrdu["LRComprehension"] = "";
    _localizationMapBengali["LRComprehension"] = "কম্প্রিহেনশন";
    _localizationMapSwahili["LRComprehension"] = "Ufahamu";

    _localizationMapEnglish["BookwithQuiz"] = "Book with Quiz";
    _localizationMapHindi["BookwithQuiz"] = "प्रश्नोत्तरी के साथ बुक करें";
    // _localizationMapUrdu["BookwithQuiz"] = "";
    _localizationMapBengali["BookwithQuiz"] = "কুইজ সঙ্গে বুক";
    _localizationMapSwahili["BookwithQuiz"] = "Kitabu Chenye Jaribio";

    _localizationMapEnglish["Do you want to take a test on this egg?"] = "Do you want to take a test on this egg?";
    _localizationMapHindi["Do you want to take a test on this egg?"] = "क्या आप इस अंडे पर टेस्ट लेना चाहते हैं?";
    // _localizationMapUrdu["Do you want to take a test on this egg?"] = "";
    _localizationMapBengali["Do you want to take a test on this egg?"] = "আপনি কি এই ডিমের পরীক্ষা নিতে চান?";
    _localizationMapSwahili["Do you want to take a test on this egg?"] = "Je, unataka kufanya jaribio kuhusu hili yai?";

    _localizationMapEnglish["Take the quiz to add me to your sea world!"] = "Take the quiz to add me to your sea world!";
    _localizationMapHindi["Take the quiz to add me to your sea world!"] = "मुझे अपनी समुद्री दुनिया में जोड़ने के लिए प्रश्नोत्तरी लो!";
    // _localizationMapUrdu[""Take the quiz to add me to your sea world!""] = "";
    _localizationMapBengali["Take the quiz to add me to your sea world!"] = "আপনার সমুদ্র বিশ্বের আমাকে যোগ করার জন্য কুইজ নিন!";
    _localizationMapSwahili["Take the quiz to add me to your sea world!"] = "Fanya jaribio ili uniongeze kwenye\ndunia yako ya bahari!";

    _localizationMapEnglish["Congratulations!\nSee you at your sea world!"] = "Congratulations!\nSee you at your sea world!";
    _localizationMapHindi["Congratulations!\nSee you at your sea world!"] = "बधाई हो! आपकी समुद्री दुनिया में मिलते है!";
    // _localizationMapUrdu["Congratulations!\nSee you at your sea world!"] = "";
    _localizationMapBengali["Congratulations!\nSee you at your sea world!"] = "অভিনন্দন! আপনার সমুদ্র বিশ্বের আপনি দেখুন!";
    _localizationMapSwahili["Congratulations!\nSee you at your sea world!"] = "Hongera!\nTuonane kwenye\ndunia yako ya bahari!";

    _localizationMapEnglish["Try again to add me to your sea world!"] = "Try again to add me to your sea world!";
    _localizationMapHindi["Try again to add me to your sea world!"] = "अपनी समुद्री दुनिया में मुझे जोड़ने के लिए फिर से कोशिश करें!";
    // _localizationMapUrdu["Try again to add me to your sea world!"] = "";
    _localizationMapBengali["Try again to add me to your sea world!"] = "আপনার সমুদ্র বিশ্বের যোগ করার জন্য আবার চেষ্টা করুন!";
    _localizationMapSwahili["Try again to add me to your sea world!"] = "Jaribu tena kuniongeza kwenye\ndunia yako ya bahari!";

    _localizationMapEnglish["Don't give up! Let's try it again!"] = "Don't give up! Let's try it again!";
    _localizationMapHindi["Don't give up! Let's try it again!"] = "हार मत मानो! चलो फिर से कोशिश करो!";
    // _localizationMapUrdu["Don't give up! Let's try it again!"] = "";
    _localizationMapBengali["Don't give up! Let's try it again!"] = "ছেড়ে দিও না! চল আবার চেষ্টা করি!";
    _localizationMapSwahili["Don't give up! Let's try it again!"] = "Usikate tamaa! Jaribu tena!";

    // NB(xenosoz, 2018): Migrated from ShapeMatching. Datasheet? I agree.
    vector<pair<string, string>> words_enUS_swTZ = {
            /*
            {"circle", "duara"},
            {"square", "mraba"},
            {"triangle", "pembe_tatu"},
            {"rectangle", "mstatili"},
            {"star", "nyota"},
            {"rhombus", "rombasi"},
            {"diamond", "almasi"},
            {"ova", "mviringo"},
            {"hexagon", "pembe_sita"},
            {"pentagon", "pembe_tano"},
            {"trapezoid", "trapeza"},
            {"parallelogram", "msambamba"},
            {"octagon", "pembe_nane"},
            {"cone", "pia"},
            {"sphere", "nyanja"},
            {"cylinder", "mcheduara"},
            {"cube", "mche_mraba"},
            {"rectangular_prism", "mche_mstatili"},
            {"triangular_prism", "mche_pembe_tatu"},
            {"pyramid", "piramidi"},
             */
            {"चेहरा", "uso"},
            {"चेहरे के", "nyuso"},
            {"पक्ष", "upande"},
            {"पक्षों", "pande"},
            {"विशाल", "kubwa"},
            {"मध्यम", "wastani"},
            {"छोटा", "dogo"}
    };

    for (auto item : words_enUS_swTZ) {
        string enUS = item.first;
        string swTZ = item.second;

        auto key = enUS;
        _localizationMapEnglish[key] = enUS;
        _localizationMapSwahili[key] = swTZ;
    }

}

std::vector<std::string> LanguageManager::getLocalizationMapKeys() {
    std::vector<std::string> rt;
    rt.clear();
    for (auto it: _localizationMapEnglish) {
        rt.push_back(it.first);
    }
    return rt;
}

bool LanguageManager::isSignLanguageMode()
{
    //return true;
    auto ret = UserDefault::getInstance()->getBoolForKey("sign_language_mode_on", false);
    return ret;
}