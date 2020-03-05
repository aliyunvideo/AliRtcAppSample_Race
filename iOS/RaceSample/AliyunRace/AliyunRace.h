//
//  AliyunRace.h
//  Tutorial
//
//  Created by gaoyu on 2019/12/11.
//  Copyright © 2019 tutan. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AliyunRace : NSObject

//实例化单例
+ (instancetype)shared;

- (void)create;

- (void)destroy;

- (int)processTextureToTexture:(uint32_t)textureId Width:(uint32_t)width Height:(uint32_t)height;

/**
* 美型开关
* @param switchOn 是否开启人脸检测及美型处理
*/
- (void)setfaceSwitch:(BOOL)switchOn;

/**
* 磨皮
* @param level 磨皮等级，常用取值范围 [0, 1]
* @return 0, ok; <0 ,error
*/
- (int)setSkinBuffing:(float)level;

/**
 * 锐化
 * @param level 锐化等级，常用取值范围 [0, 1]
 * @return 0, ok; <0 ,error
 */
- (int)setSharpen:(float)level;

/**
 * 美白
 * @param level 美白等级，常用取值范围 [0, 1]
 * @return 0, ok; <0 ,error
 */
- (int)setSkinWhitening:(float)level;

/**
 * 设置美型参数
 * @param level 美型等级参数，可以超出 ALRFaceShape 中定义的参数范围
 * @return 成功返回 ALR_OK，否则返回 < 0
 */
- (int)setFaceShape:(int)type Level:(float)level;

@end

