//
//  AliyunRace.m
//  Tutorial
//
//  Created by gaoyu on 2019/12/11.
//  Copyright © 2019 tutan. All rights reserved.
//

#import "AliyunRace.h"
#import <AliyunRace/aliyun_beautify.h>  //美颜

@interface AliyunRace ()
{
    race_t aliRaceBuautyHandle;   //美颜
    float SkinBuffing;
    float Sharpen;
    float SkinWhitening;
}
@end

@implementation AliyunRace

//实例化单例
+ (instancetype)shared {
    static AliyunRace *_instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [[self alloc] init];
    });
    return _instance;
}

- (void)create{
    if(aliRaceBuautyHandle){
        aliyun_beautify_destroy(aliRaceBuautyHandle);
        aliRaceBuautyHandle = nullptr;
    }

    aliyun_beautify_create(&aliRaceBuautyHandle);
    aliyun_beautify_setFaceSwitch(aliRaceBuautyHandle, true);
//    aliyun_beautify_setFaceDebug(aliRaceBuautyHandle, true);
    aliyun_setLogLevel(ALR_LOG_LEVEL_ERROR);
    
    SkinBuffing = 1;
    Sharpen = 1;
    SkinWhitening = 0.5;
    aliyun_beautify_setSkinBuffing(aliRaceBuautyHandle, SkinBuffing);
    aliyun_beautify_setSharpen(aliRaceBuautyHandle, Sharpen);
    aliyun_beautify_setSkinWhitening(aliRaceBuautyHandle, SkinWhitening);
}

- (void)destroy{
    if(aliRaceBuautyHandle){
        aliyun_beautify_destroy(aliRaceBuautyHandle);
        aliRaceBuautyHandle = nullptr;
    }
}

- (int)processTextureToTexture:(uint32_t)textureId Width:(uint32_t)width Height:(uint32_t)height{
    aliyun_beautify_setSkinBuffing(aliRaceBuautyHandle, SkinBuffing);
    aliyun_beautify_setSharpen(aliRaceBuautyHandle, Sharpen);
    aliyun_beautify_setSkinWhitening(aliRaceBuautyHandle, SkinWhitening);
    return aliyun_beautify_processTextureToTexture(aliRaceBuautyHandle,textureId,width,height,ALR_ROTATE_180_CW,0);
}

- (void)setfaceSwitch:(BOOL)switchOn{
    aliyun_beautify_setFaceSwitch(aliRaceBuautyHandle, switchOn);
}

- (int)setSkinBuffing:(float)level{
    SkinBuffing = level;
    return 1;
}

- (int)setSharpen:(float)level{
    Sharpen = level;
    return 1;
}

- (int)setSkinWhitening:(float)level{
    SkinWhitening = level;
    return 1;
}

- (int)setFaceShape:(int)type Level:(float)level{
    return aliyun_beautify_setFaceShape(aliRaceBuautyHandle, (ALRFaceShape)type, level);
}

@end
