//
//  MultiplicationBoardInfo.hpp
//  KitkitSchool
//
//  Created by harunom on 7/21/18.
//

#pragma once

#include "cocos2d.h"
USING_NS_CC;

#include "Common/Basic/KitkitGameInfo.hpp"

#include "MultiplicationBoardScene.hpp"

#include <string>
#include <vector>

using namespace std;



class MultiplicationBoardInfo : public KitkitGameInfo
{
public:
    virtual std::string getGameName() override { return "MultiplicationBoard"; }
    virtual std::string getSceneName() override { return "MultiplicationBoardScene"; }
    
    virtual Scene* createScene(std::string levelID) override {
        return MultiplicationBoardScene::createScene(levelID);
    }
    
    
    virtual std::vector<std::string> enumerateLevelIDs() override;
    int getMaxLevel();
    
    
    
};

