#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

USING_NS_CC;

Scene* HelloWorld::createScene()
{
    // 'scene' is an autorelease object
    auto scene = Scene::create();
    
    // 'layer' is an autorelease object
    auto layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !Layer::init() )
    {
        return false;
    }
	
	cocos2d::network::HttpRequest *request = new cocos2d::network::HttpRequest();
	char imgUrl[256] = "https://koenig-media.raywenderlich.com/uploads/2015/04/cocos2d-x-thumb.png";
	request->setUrl(imgUrl);
	request->setResponseCallback(CC_CALLBACK_2(HelloWorld::onHttpRequestCompleted, this));
	request->setRequestType(cocos2d::network::HttpRequest::Type::GET);
	cocos2d::network::HttpClient::getInstance()->send(request);
	request->release();
    
	return true;
}

void HelloWorld::onHttpRequestCompleted(cocos2d::network::HttpClient *sender, cocos2d::network::HttpResponse *response)
{	
	/*
		Load image from a public URL
	*/
	std::vector<char> *imageBuffer = response->getResponseData();
	Image* image = new Image();
	image->initWithImageData(reinterpret_cast<const unsigned char*>(&(imageBuffer->front())), imageBuffer->size());
	Texture2D* texture = new Texture2D();
	texture->initWithImage(image);
	Sprite* imgSprite = Sprite::createWithTexture(texture);
	imgSprite->setPosition(200, 300);
	addChild(imgSprite);
}
