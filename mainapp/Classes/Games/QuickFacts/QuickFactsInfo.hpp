//
//  QuickFactsInfo.hpp
//  KitkitSchool
//
//  Created by Eunil Park on 18/08/18.
//

#pragma once

#include "cocos2d.h"
USING_NS_CC;

#include "Common/Basic/KitkitGameInfo.hpp"

#include "QuickFactsScene.hpp"

#include <string>
#include <vector>

using namespace std;

class QuickFactsInfo : public KitkitGameInfo
{
public:
    virtual std::string getGameName() override { return "QuickFacts"; }
    virtual std::string getSceneName() override { return "QuickFactsScene"; }
    
    virtual Scene* createScene(std::string levelID) override {
        return QuickFactsScene::createScene(levelID);
    }
    
    virtual std::vector<std::string> enumerateLevelIDs() override;
    
    
    
};

