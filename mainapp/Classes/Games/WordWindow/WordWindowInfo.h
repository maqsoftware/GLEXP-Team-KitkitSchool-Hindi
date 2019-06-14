#pragma once

#include "cocos2d.h"
USING_NS_CC;

#include "Common/Basic/KitkitGameInfo.hpp"

#include "WordWindowScene.h"

#include <string>
#include <vector>

using namespace std;

class WordWindowInfo : public KitkitGameInfo
{
public:
    virtual std::string getGameName() override { return "WordWindow"; }
    virtual std::string getSceneName() override { return "WordWindowScene"; }
    
    virtual Scene* createScene(std::string levelID) override
	{
		return WordWindowScene::createScene(levelID);
    }
    
    virtual std::vector<std::string> enumerateLevelIDs() override;
};

