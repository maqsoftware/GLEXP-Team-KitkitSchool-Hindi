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

LanguageManager *LanguageManager::_instance = 0;

LanguageManager *LanguageManager::getInstance() {
    if (!_instance) {
        _instance = new LanguageManager();
        _instance->init();
    }
    return _instance;
}


void LanguageManager::init() {
#if false
    auto defaultLang = LanguageType::ENGLISH;
#endif
    //auto defaultLang = LanguageType::SWAHILI;
    auto defaultLang = "en-US";

    auto localeCode = UserDefault::getInstance()->getStringForKey("LocaleCode", defaultLang);

    auto localeType = convertLocaleCodeToType(localeCode);
    if (localeType >= LocaleType_MAX) {
        localeType = en_US;
    }
    _supportedLocales.clear();
    for (int i = 0; i < LocaleType_MAX; i++) {
        LocaleType l = (LocaleType) i;
        auto lc = convertLocaleTypeToCode(l);
        auto lp = "localized/curriculumdata_" +
                  LanguageManager::getInstance()->getCurrentLanguageCode() + ".tsv";
        if (FileUtils::getInstance()->isFileExist(lp)) _supportedLocales.push_back(l);

    }
    if (_supportedLocales.size() == 0) {
        CCLOGERROR(
                "No curriculumdata.tsv is found for any language. check %s/PehlaSchool/location.txt, which is curretnly refers to %s",
                FileUtils::getInstance()->getWritablePath().c_str(),
                FileUtils::getInstance()->getDefaultResourceRootPath().c_str());
        exit(1);
    } else {
        if (std::find(_supportedLocales.begin(), _supportedLocales.end(), localeType) ==
            _supportedLocales.end())
            localeType = _supportedLocales.front();
    }


    setCurrentLocale(localeType);


    initLocalizationMap();


}

LanguageManager::LocaleType LanguageManager::convertLocaleCodeToType(std::string localeCode) {
    if (localeCode.length() < 5) return LocaleType_MAX;
    auto lang = localeCode.substr(0, 2);
    auto region = localeCode.substr(3, 2);

    if (lang == "en") {
        if (region == "US") return en_US;
    } else if (lang == "hi") {
        if (region == "IN") return hi_IN;
    } else if (lang == "ur") {
        if (region == "IN") return ur_IN;
    } else if (lang == "bn") {
        if (region == "IN") return bn_IN;
    } else if (lang == "sw") {
        if (region == "TZ") return sw_TZ;
    }
    return LocaleType_MAX;
}

std::string LanguageManager::convertLocaleTypeToCode(LanguageManager::LocaleType localeType) {
    switch (localeType) {
        case en_US:
            return std::string("en-US");
        case hi_IN:
            return std::string("hi-IN");
        case ur_IN:
            return std::string("ur-IN");
        case bn_IN:
            return std::string("bn-IN");
        case sw_TZ:
            return std::string("sw-TZ");
        case LocaleType_MAX:
            break;
    }

    return std::string("en-US");

}

void LanguageManager::setCurrentLocale(LocaleType type) {
    bool dirty = false;
    if (_currentLocale != type) dirty = true;


    _currentLocale = type;

    if (dirty) {
        auto langCode = convertLocaleTypeToCode(type);
        UserDefault::getInstance()->setStringForKey("LocaleCode", langCode);
        UserDefault::getInstance()->flush();
    }

    std::vector<std::string> paths = {};

    paths.push_back("localized/games");
    paths.push_back("games");
    paths.push_back("main");

    FileUtils::getInstance()->setSearchPaths(paths);
}

LanguageManager::LocaleType LanguageManager::getCurrentLocaleType() {
    return _currentLocale;
}

LanguageManager::LocaleType LanguageManager::findNextLocale() {
    LocaleType next = (LocaleType) ((int) _currentLocale + 1);

    while (next != _currentLocale) {
        if (std::find(_supportedLocales.begin(), _supportedLocales.end(), next) !=
            _supportedLocales.end())
            return next;
        next = (LocaleType) ((int) next + 1);
        if (next >= LocaleType_MAX) next = (LocaleType) 0;

    }

    return _currentLocale;
}


std::string LanguageManager::getCurrentLanguageCode() {

    std::string langCode = convertLocaleTypeToCode(_currentLocale);
    return langCode.substr(0, 2);

}

std::string LanguageManager::getCurrentLanguageTag() {
    return getCurrentLocaleCode();
}

std::string LanguageManager::getCurrentLocaleCode() {
    std::string langCode = convertLocaleTypeToCode(_currentLocale);
    return langCode;
}


std::string LanguageManager::soundPathForWordFile(std::string &wordFile) {
    std::string folder;

//    switch (_langType) {
//        case ENGLISH: folder = "Common/Sounds/Pam.en_US/"; break;
//        case SWAHILI: folder = "Common/Sounds/Imma.sw_TZ/"; break;
//    }

    std::string path = findLocalizedResource("LetterVoice/" + wordFile);
    if (path != "") return path;
    path = findLocalizedResource("WordVoice/" + wordFile);
    if (path != "") return path;

    return "";

}

std::string LanguageManager::getLocalizedString(std::string str) {

    std::string localized;

    switch (_currentLocale) {
        case en_US:
            localized = _localizationMapHindi[str];
            break;
        case hi_IN:
            localized = _localizationMapHindi[str];
            break;
        case ur_IN:
            localized = _localizationMapUrdu[str];
            break;
        case bn_IN:
            localized = _localizationMapBengali[str];
            break;
        case sw_TZ:
            localized = _localizationMapSwahili[str];
            break;
        case LocaleType_MAX:
            break;
    }

    if (localized.empty()) return str;

    return localized;

}

std::string LanguageManager::findLocalizedResource(std::string path) {

    // handled by Cocos...

    return path;

}

void LanguageManager::initLocalizationMap() {
    string delm = "$#$";
    _localizationMapHindi["Stop the test"] = "ÅðSÅ Õ‹Î ·UUUUçÚ°"; // टेस्ट बन्द करिए
    _localizationMapHindi["Go back to test"] = "ÅðSÅ ÂÚ ÕæÂâ Áæ§Øð"; // टेस्ट पर बापस जाइये

    _localizationMapHindi["Great!"] = "ÕãéÌ ¥‘Àð!"; // बहुत अच्छे
    _localizationMapSwahili["Great!"] = "Vizuri!";

    _localizationMapHindi["Are you ready for"] = "€UØæ Ìé× ÌñØæÚU ãô"; // क्या तुम तैयार हो
    _localizationMapSwahili["Are you ready for"] = "Je, uko tayari kwa";

    _localizationMapHindi["Prove it!"] = "âæçÕÌ ·¤ÚUô!"; // साबित करो!
    _localizationMapSwahili["Prove it!"] = "Thibitisha!";

    _localizationMapHindi["Try and get 8 questions correct!"] = "·UUUUôçàæàæ ·UUUUÚô ¥æñÚ ¥æÆ ÂýàæÙô ·UUUæ âãè ÁÕæÕ ÎèçÁØð"; // कोशिश करो और आठ प्रशनो का सही जबाब दीजिये
    _localizationMapSwahili["Try and get 8 questions correct!"] = "Jaribu na toa majibu sahihi manane!";

    _localizationMapHindi["Challenge"] = "¿éÙõÌè ãñ"; // चुनौती है
    _localizationMapSwahili["Challenge"] = "Jaribu";

    _localizationMapHindi["Congratulations!"] = "ÕÏæ§ü ãô!"; // बधाई हो!
    _localizationMapSwahili["Congratulations!"] = "Hongera!";

    _localizationMapHindi["You passed!"] = "Ìé× Âæâ ãô »Øð"; // तुम पास हो गये
    _localizationMapSwahili["You passed!"] = "Umefaulu!";

    _localizationMapHindi["You failed"] = "Ìé× ¥âÈ¤Ü ÚUãð"; // तुम असफल रहे
    _localizationMapSwahili["You failed"] = "Umeshindwa";

    _localizationMapHindi["Practice more and try again later."] = "¥çÏ·UUUU ¥zØæâ ·UUUUÚð¢ ¥æñÚ ÕæÎ ×ð¢ çÈUUUUÚ âð ÂýØæâ ·UUUUÚð¢Ð"; // अधिक अभ्यास करें और बाद में फिर से प्रयास करें।
    _localizationMapSwahili["Practice more and try again later."] = "Fanya mazoezi zaidi na rudi baadae.";

    _localizationMapHindi["Success!"] = "âÈ¤ÜÌæ!"; // सफलता!
    _localizationMapSwahili["Success!"] = "Mafanikio!";

    _localizationMapHindi["You are not ready."] = "Ìé× ÌñØæÚ Ùãè¢ ãôÐ"; // तुम तैयार नहीं हो।
    _localizationMapSwahili["You are not ready."] = "Hauko tayari.";

    _localizationMapHindi["You need more practice."] = "¥æÂ·UUUUô ¥æñÚ ¥zØæâ ·UUUè ¥æßàØ·UUUUÌæ ãñÐ"; // आपको और अभ्यास की आवश्यकता है।
    _localizationMapSwahili["You need more practice."] = "Unahitaji mazoezi zaidi.";

    _localizationMapHindi["Welcome!"] = "Sßæ»Ì ãñ!"; // स्वागत है!
    _localizationMapSwahili["Welcome!"] = "Karibu!";

    _localizationMapHindi["Start"] = "àæéLUUUU ·UUUUÚô"; // शुरु करो
    _localizationMapSwahili["Start"] = "Anza";

    _localizationMapHindi["Next"] = "ÂèÀð"; // आगे
    _localizationMapSwahili["Next"] = "Nenda mbele";

    _localizationMapHindi["Back"] = "ÂèÀð"; // पीछे
    _localizationMapSwahili["Back"] = "Rudi nyuma";

    _localizationMapHindi["OK"] = "¥ô·Ô¤"; // ओके
    _localizationMapSwahili["OK"] = "OK";

    _localizationMapHindi["Error"] = "»ÜÌè"; // गलती
    _localizationMapSwahili["Error"] = "Hitilafu";

    _localizationMapHindi["Enter"] = "çÜ¹Ùæ ÂýæÚ{Ö ·UUUUÚð¢"; // लिखना प्रारम्भ करें
    _localizationMapSwahili["Enter"] = "Chomeka";

    _localizationMapHindi["Clear"] = "âæÈ¤ ·¤ÚUð"; // साफ करे
    _localizationMapSwahili["Clear"] = "Futa";

    _localizationMapHindi["English"] = "¥´»ýðÁ¸è"; // अंग्रेज़ी
    _localizationMapSwahili["English"] = "Kiswahili";

    _localizationMapHindi["Math"] = "»ç‡æÌ"; // गणित
    _localizationMapSwahili["Math"] = "Hesabu";

    _localizationMapHindi["TutorialTrace"] = "Üæ§Ù ¹è¿ð¢" + delm + "Line Tracing"; // लाइन खीचें
    _localizationMapSwahili["TutorialTrace"] = "Kufuatisha Mstari";

    _localizationMapHindi["FindTheMatch"] =
            "ÁôÇ¸è ¹ôçÁØð" + delm + "Find The Match"; // जोड़ी खोजिये
    _localizationMapSwahili["FindTheMatch"] = "Tafuta Sare";

    _localizationMapHindi["NumberMatching"] =
            "â¢wØæ ç×ÜæÙæ" + delm + "Number Matching"; // संख्या मिलाना
    _localizationMapSwahili["NumberMatching"] = "Kufananisha Nambari";

    _localizationMapHindi["Tapping"] = "ÕÕÜ ÂæòÂ" + delm + "Bubble Pop"; // बबल पॉप
    _localizationMapSwahili["Tapping"] = "Pasua Povu la Sabuni";

    _localizationMapHindi["LetterMatching"] =
            "¥ÿæÚ ç×ÜæÙæ" + delm + "Letter Matching"; // अक्षर मिलाना
    _localizationMapSwahili["LetterMatching"] = "Kufananisha Kusoma na Kuandika";

    _localizationMapHindi["AnimalPuzzle"] =
            "Âàæé¥ô¢ ·UUUè ÂãðÜè" + delm + "Animal Puzzle"; // पशुओं की पहेली
    _localizationMapSwahili["AnimalPuzzle"] = "Fumbo la Picha";

    _localizationMapHindi["PatternTrain"] = "ÂñÅÙü ÅþñÙ" + delm + "Pattern Train"; // पैटर्न ट्रैन
    _localizationMapSwahili["PatternTrain"] = "Reli ya Garimoshi";

    _localizationMapHindi["Video"] = "ßèçÇØô"; // वीडियो
    _localizationMapSwahili["Video"] = "Video";

    _localizationMapHindi["Counting"] = "ç»ÙÌè" + delm + "Counting"; // गिनती
    _localizationMapSwahili["Counting"] = "Kuhesabu";

    _localizationMapHindi["EquationMaker"] =
            "â×è·¤ÚU‡æ ÕÙæÙð ßæÜæ" + delm + "Equation Maker"; // समीकरण बनाने वाला
    _localizationMapSwahili["EquationMaker"] = "Kiumba Mlinganyo";

    _localizationMapHindi["NumberTrain"] = "â¢wØæ ÅþðÙ" + delm + "Number Train"; // संख्या ट्रेन
    _localizationMapSwahili["NumberTrain"] = "Nambari ya Garimoshi";

    _localizationMapHindi["AlphabetPuzzle"] =
            "ß‡æü×æÜæ ·UUUè ÂãðÜè" + delm + "Alphabet Puzzle"; // वर्णमाला की पहेली
    _localizationMapSwahili["AlphabetPuzzle"] = "Fumbo la Alfabeti";

    _localizationMapHindi["Book"] = "ç·¤ÌæÕ"; // किताब
    _localizationMapSwahili["Book"] = "Kitabu";

    _localizationMapHindi["Comprehension"] = "â×Ûæ ÕêÛæ" + delm + "Comprehension"; // समझ बूझ
    _localizationMapSwahili["Comprehension"] = "Maswali ya Ufahamu";

    _localizationMapHindi["DoubleDigit"] =
            "Îô ¥¢·UUUUô ·UUUè »ç‡æÌ" + delm + "Double Digit Math"; // दो अंको की गणित
    _localizationMapSwahili["DoubleDigit"] = "Hisabati ya Tarakimu Mbili";

    _localizationMapHindi["FishTank"] = "×ÀÜè ƒæÚU" + delm + "Fish Tank"; // मछली घर
    _localizationMapSwahili["FishTank"] = "Tangi ya Samaki";

    _localizationMapHindi["HundredPuzzle"] = "100 ÂãðÜè" + delm + "100 Puzzle"; // 100 पहेली
    _localizationMapSwahili["HundredPuzzle"] = "Fumbo la Nambari 100";

    _localizationMapHindi["LetterTrace"] = "¥ÿæÚ ÕÙæ°¢" + delm + "Letter Trace"; // अक्षर बनाएं
    _localizationMapSwahili["LetterTrace"] = "Kufuatisha Herufi";

    _localizationMapHindi["MovingInsects"] =
            "·UUUèÇ¸ð ·UUUè  ç»ÙÌè" + delm + "Bug Math"; // कीड़े की गिनती
    _localizationMapSwahili["MovingInsects"] = "Mchezo wa Mdudu";

    _localizationMapHindi["SentenceMaker"] =
            "ßævØ ÕÙæÙð ßæÜæ" + delm + "Sentence Maker"; // वाक्य बनाने वाला
    _localizationMapSwahili["SentenceMaker"] = "Kiumba Sentensi";

    _localizationMapHindi["ShapeMatching"] =
            "¥æ·¤æÚU ç×ÜæÙæ" + delm + "Shape Matching"; // आकार मिलाना
    _localizationMapSwahili["ShapeMatching"] = "Kufananisha Maumbo";

    _localizationMapHindi["SoundTrain"] = "ŠßçÙ ÅþðÙ" + delm + "Sound Train"; // ध्वनि ट्रेन
    _localizationMapSwahili["SoundTrain"] = "Sauti ya Garimoshi";

    _localizationMapHindi["Spelling"] = "ßÌüÙè" + delm + "Spelling"; // वर्तनी
    _localizationMapSwahili["Spelling"] = "Matamshi";

    _localizationMapHindi["WordTracing"] =
            "àæyÎ ¥ÙéÚð¹‡æ" + delm + "Word Tracing"; // शब्द अनुरेखण
    _localizationMapSwahili["WordTracing"] = "Kufuatisha Maneno";

    _localizationMapHindi["NumberTracing"] =
            "â¢wØæ ¥ÙéÚð¹‡æ" + delm + "Number Tracing"; // संख्या अनुरेखण
    _localizationMapSwahili["NumberTracing"] = "Jifunze Mpaka 10";

    _localizationMapHindi["StarFall"] = "Åæ§çÂ¢»" + delm + "Typing"; // टाइपिंग
    _localizationMapSwahili["StarFall"] = "Kuchapa";

    _localizationMapHindi["WordMachine"] =
            "àæyÎô¢ ·UUUè ×àæèÙ" + delm + "Word Machine"; // शब्दों की मशीन
    _localizationMapSwahili["WordMachine"] = "Mashine ya Maneno";

    _localizationMapHindi["NumberTracingExt"] =
            "â¢wØæ ¥ÙéÚð¹‡æ" + delm + "Number Tracing"; // संख्या अनुरेखण
    _localizationMapSwahili["NumberTracingExt"] = "Kufuatisha Nambari";

    _localizationMapHindi["LetterTracingCard"] =
            "¥ÿæÚ Åþðçâ¢» ·UUUæÇü" + delm + "Letter Tracing Card"; // अक्षर ट्रेसिंग कार्ड
    _localizationMapSwahili["LetterTracingCard"] = "Fuatisha Mara 3";

    _localizationMapHindi["NumberPuzzle"] =
            "â¢wØæØô¢ ·UUUè ÂãðçÜØæ¡" + delm + "Number Blocks"; // संख्यायों की पहेलियाँ
    _localizationMapSwahili["NumberPuzzle"] = "Fumbo la Nambari";

    _localizationMapHindi["Arrange the numbers in order from smallest to largest"] = "ÀôÅè âð Üð·UUUUÚ ÕÇ¸è ¥¢·UUUU ·ðUUU RUUUU× ×ð¢ â¢wØæ¥ô¢ ·UUUè RUUUU×Õh ·UUUUÚð¢Ð"; // छोटी से लेकर बड़ी अंक के क्रम में संख्याओं की क्रमबद्ध करें|
    _localizationMapSwahili["Arrange the numbers in order from smallest to largest"] = "panga kwa mpangilio kutoka ndogo zaidi kwenda kubwa zaidi";

    _localizationMapHindi["Largest number"] = "âÕâð ÕÇ¸è â¢wØæ"; // सबसे बड़ी संख्या
    _localizationMapSwahili["Largest number"] = "Namba ipi ndiyo kubwa zaidi?";

    _localizationMapHindi["BirdPhonics"] =
            "Âÿæè ·UUUè ŠßçÙ" + delm + "Bird Phonics"; // पक्षी की ध्वनि
    _localizationMapSwahili["BirdPhonics"] = "Sauti Ndege";

    _localizationMapHindi["FeedingTime"] =
            "ç¹ÜæÙð ·UUUæ â×Ø" + delm + "Feeding Time"; // खिलाने का समय
    _localizationMapSwahili["FeedingTime"] = "Wakati wa kula";

    _localizationMapHindi["LineMatching"] = "Úð¹æ ç×ÜæÙ" + delm + "Line Matching"; // रेखा मिलान
    _localizationMapSwahili["LineMatching"] = "Linganisha mistari";

    _localizationMapHindi["MangoShop"] =
            "¥æ× ·UUUè Îé·UUUæÙ" + delm + "Mango Shop"; // आम की दुकान
    _localizationMapSwahili["MangoShop"] = "Duka la embe";

    _localizationMapHindi["MissingNumber"] =
            "¥ÙéÂçSÍÌ â¢wØæ" + delm + "Missing Number"; // अनुपस्थित संख्या
    _localizationMapSwahili["MissingNumber"] = "Namba iliyopotea";

    _localizationMapHindi["ReadingBird"] =
            "ÂÉ¸Ùð ßæÜæ Âÿæè" + delm + "Reading Bird"; // पढ़ने वाला पक्षी
    _localizationMapSwahili["ReadingBird"] = "Ndege anayesoma";

    _localizationMapHindi["WhatIsThis"] = "Øã vØæ ãñ?" + delm + "What Is This?"; // यह क्या है?
    _localizationMapSwahili["WhatIsThis"] = "Hii ni nini?";

    _localizationMapHindi["ThirtyPuzzle"] = "30 ÂãðÜè" + delm + "30 Puzzle"; // 30 पहेली
    _localizationMapSwahili["ThirtyPuzzle"] = "Panga Namba";

    _localizationMapHindi["WordNote"] = "àæyÎ ÙôÅ" + delm + "Word Note"; // शब्द नोट
    _localizationMapSwahili["WordNote"] = "Tunga neno";

    _localizationMapHindi["QuickFacts"] = "ÌéÚ‹Ì Ì‰Ø" + delm + "Quick Facts"; // ततुरन्त तथ्य
    _localizationMapSwahili["QuickFacts"] = "Ukweli wa Uhakika";

    _localizationMapHindi["MultiplicationBoard"] =
            "»é‡ææ Â^" + delm + "Multiplication Lamp"; // गुणा पट्ट
    _localizationMapSwahili["MultiplicationBoard"] = "Taa ya Kuzidishia";

    _localizationMapHindi["WordMatrix"] = "àæyÎ ¥æÃØêã" + delm + "Word Matrix"; // शब्द आव्यूह
    _localizationMapSwahili["WordMatrix"] = "Chanzo cha Neno";

    _localizationMapHindi["SentenceBridge"] =
            "ßævØô¢ ·UUUæ ÂéÜ" + delm + "Sentence Bridge"; // वाक्यों का पुल
    _localizationMapSwahili["SentenceBridge"] = "Daraja la Sentensi";

    _localizationMapHindi["WordWindow"] = "ßÇü çß¢Çô" + delm + "Word Window"; // वर्ड विंडो
    _localizationMapSwahili["WordWindow"] = "Dirisha la Neno";

    _localizationMapHindi["WordKicker"] = "àæyÎ ç·UUUU·UUUUÚ" + delm + "Word Kicker"; // शब्द किकर
    _localizationMapSwahili["WordKicker"] = "Mpigo wa Neno";

    _localizationMapHindi["MathKicker"] =
            "×ñ‰â ç·UUUU·UUUUÚ" + delm + "Math Kicker"; // मैथ्स किकर
    _localizationMapSwahili["MathKicker"] = "Mpigo wa Hisabati";

    _localizationMapHindi["PlaceValue"] = "×æÙ Ú¹ð" + delm + "Place Value"; // मान रखे
    _localizationMapSwahili["PlaceValue"] = "Fungu la Thamani";

    _localizationMapHindi["Labeling"] =
            "¥¢ç·UUUUÌ·UUUU Ü»æÙæ" + delm + "Labeling"; // अंकितक लगाना
    _localizationMapSwahili["Labeling"] = "Pachika Jina";

    _localizationMapHindi["LRComprehension"] = "â×Ûæ ÕêÛæ" + delm + "Comprehension"; // समझ बूझ
    _localizationMapSwahili["LRComprehension"] = "Ufahamu";

    _localizationMapHindi["BookwithQuiz"] = "ÂýoAô}æÚè ·ðUUU âæÍ Õé·UUUU ·UUUUÚð¢"; // प्रश्नोत्तरी के साथ बुक करें
    _localizationMapSwahili["BookwithQuiz"] = "Kitabu Chenye Jaribio";

    _localizationMapHindi["Do you want to take a test on this egg?"] = "vØæ ¥æÂ §â ¥¢Çð ÂÚ ÅðSÅ ÜðÙæ ¿æãÌð ãñ¢?"; // क्या आप इस अंडे पर टेस्ट लेना चाहते हैं?
    _localizationMapSwahili["Do you want to take a test on this egg?"] = "Je, unataka kufanya jaribio kuhusu hili yai?";

    _localizationMapHindi["Take the quiz to add me to your sea world!"] = "×éÛæð ¥ÂÙè â×é¼ýè ÎéçÙØæ ×ð¢ ÁôÇ¸Ùð ·ðUUU çÜ° ÂýoAô}æÚè Üô!"; // मुझे अपनी समुद्री दुनिया में जोड़ने के लिए प्रश्नोत्तरी लो!
    _localizationMapSwahili["Take the quiz to add me to your sea world!"] = "Fanya jaribio ili uniongeze kwenye\ndunia yako ya bahari!";

    _localizationMapHindi["Congratulations!\nSee you at your sea world!"] = "ÕÏæ§ü ãô! ¥æÂ·UUUè â×é¼ýè ÎéçÙØæ ×ð¢ ç×ÜÌð ãñ!"; // बधाई हो! आपकी समुद्री दुनिया में मिलते है!
    _localizationMapSwahili["Congratulations!\nSee you at your sea world!"] = "Hongera!\nTuonane kwenye\ndunia yako ya bahari!";

    _localizationMapHindi["Try again to add me to your sea world!"] = "¥ÂÙè â×é¼ýè ÎéçÙØæ ×ð¢ ×éÛæð ÁôÇ¸Ùð ·ðUUU çÜ° çÈUUUUÚ âð ·UUUUôçàæàæ ·UUUUÚð¢!"; // अपनी समुद्री दुनिया में मुझे जोड़ने के लिए फिर से कोशिश करें!
    _localizationMapSwahili["Try again to add me to your sea world!"] = "Jaribu tena kuniongeza kwenye\ndunia yako ya bahari!";

    _localizationMapHindi["Don't give up! Let's try it again!"] = "ãæÚ ×Ì ×æÙô! ¿Üô çÈUUUUÚ âð ·UUUUôçàæàæ ·UUUUÚô!"; // हार मत मानो! चलो फिर से कोशिश करो!
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
            {"चेहरा",    "uso"},
            {"चेहरे के", "nyuso"},
            {"पक्ष",     "upande"},
            {"पक्षों",   "pande"},
            {"विशाल",    "kubwa"},
            {"मध्यम",    "wastani"},
            {"छोटा",     "dogo"}
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
    for (auto it: _localizationMapHindi) {
        rt.push_back(it.first);
    }
    return rt;
}

bool LanguageManager::isSignLanguageMode() {
    //return true;
    auto ret = UserDefault::getInstance()->getBoolForKey("sign_language_mode_on", false);
    return ret;
}
