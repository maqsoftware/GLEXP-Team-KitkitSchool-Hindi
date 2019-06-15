//
//  SentenceBridgeInfo.hpp
//  KitkitSchool
//
//  Created by MilliSoftGames on 7/26/18.
//

#pragma once

#include "cocos2d.h"
USING_NS_CC;

#include "Common/Basic/KitkitGameInfo.hpp"

#include "SentenceBridgeScene.hpp"

#include <string>
#include <vector>

using namespace std;



class SentenceBridgeInfo : public KitkitGameInfo
{
public:
    virtual std::string getGameName() override { return "SentenceBridge"; }
    virtual std::string getSceneName() override { return "SentenceBridgeScene"; }
    
    virtual Scene* createScene(std::string levelID) override {
        return SentenceBridge::SentenceBridgeScene::createScene(levelID);
    }
    
    
    virtual std::vector<std::string> enumerateLevelIDs() override;
    
    
    
};

