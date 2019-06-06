//
//  GameVideoScene.hpp
//  KitkitSchool
//
//  Created by Sungwoo Kang on 9/6/17.
//
//

#ifndef GameVideoScene_hpp
#define GameVideoScene_hpp


#pragma once

#include <cocos/cocos2d.h>
#include <vector>
#include <functional>
#include <string>

#include "Common/Controls/Subtitle.hpp"


class GameVideoScene: public cocos2d::Node {
public:
    typedef cocos2d::Node Super;
    
    
private:
    std::string _filename;
    
    
    Node *_videoNode;
    cocos2d::Sprite *_frame;
    Subtitle* _subtitle;
    
    
public:
    CREATE_FUNC(GameVideoScene);
    GameVideoScene();
    virtual bool init() override;
    
    //virtual void onEnter() override;
    virtual void onEnterTransitionDidFinish() override;
    
    
public:
    // NB(xenosoz, 2016): Create a minimal scene containing a GradeSelector.
    static cocos2d::Scene* createScene(const std::string& filename);
    
    static bool videoExists(const std::string& filename);
    static std::string getVideoFile(const std::string& filename);
    
    
};

#endif /* GameVideoScene_hpp */
