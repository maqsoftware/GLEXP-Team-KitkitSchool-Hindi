//
//  LabelingInfo.hpp
//  KitkitSchool
//
//  Created by Seungbum Son on 9/5/18.
//

#pragma once

#include "cocos2d.h"
USING_NS_CC;

#include "Common/Basic/KitkitGameInfo.hpp"

#include "LabelingScene.hpp"

#include <string>
#include <vector>

using namespace std;



class LabelingInfo : public KitkitGameInfo
{
public:
    virtual std::string getGameName() override { return "Labeling"; }
    virtual std::string getSceneName() override { return "LabelingScene"; }
    
    virtual Scene* createScene(std::string levelID) override {
        return LabelingScene::createScene(levelID);
    }
    
    
    virtual std::vector<std::string> enumerateLevelIDs() override;
    
    
    
};

