//
//  PublishConfigMenuView.h
//  SampleManualMode
//
//  Created by 高宇 on 2019/4/24.
//  Copyright © 2019 tiantian. All rights reserved.
//

#import <UIKit/UIKit.h>

/** 回调方法 */
typedef void (^PublishMenuBlock)(void);    //显示该view的block

typedef void (^BeautifySwitch)(BOOL);

@interface SenseMeView : UIView

//美颜开关
@property (nonatomic, strong) UILabel *switchLabel;
@property (nonatomic, strong) UISwitch *switchBeautify;

//可滑动view
@property (nonatomic, strong) UIScrollView *scrollView;

@property (nonatomic, strong) UISlider *SkinWhiteningSlider;    //美白    [0,1]
@property (nonatomic, strong) UISlider *SkinBuffingSlider;      //磨皮    [0,1]
@property (nonatomic, strong) UISlider *SharpenSlider;          //锐化    [0,1]
@property (nonatomic, strong) UISlider *CUT_CHEEKSlider;           //颧骨        [0,1]
@property (nonatomic, strong) UISlider *CutFaceSlider;   //削脸        [0,1]
@property (nonatomic, strong) UISlider *ThinFaceSlider;  //瘦脸       [0,1]
@property (nonatomic, strong) UISlider *LongFaceSlider;  //脸长       [0,1]
@property (nonatomic, strong) UISlider *LowerJawSlider;  //下巴缩短       [-1,1]
@property (nonatomic, strong) UISlider *HIGHER_JAWSlider;  //下巴拉长    [-1,1]
@property (nonatomic, strong) UISlider *THIN_JAWSlider;   //瘦下巴    [-1,1]
@property (nonatomic, strong) UISlider *THIN_MANDIBLESlider;   //瘦下颌   [0,1]
@property (nonatomic, strong) UISlider *BigEyeSlider;    //大眼        [0,1]
@property (nonatomic, strong) UISlider *EyeAngle1Slider; //眼角1     [0,1]
@property (nonatomic, strong) UISlider *CanthusSlider;   //眼距       [-1,1]
@property (nonatomic, strong) UISlider *CANTHUS1Slider;  //拉宽眼距   [-1,1]
@property (nonatomic, strong) UISlider *EyeAngle2Slider; //眼角2    [-1,1]
@property (nonatomic, strong) UISlider *EYE_TDANGLESlider;  //眼睛高度 [-1,1]
@property (nonatomic, strong) UISlider *ThinNoseSlider;  //瘦鼻      [0,1]
@property (nonatomic, strong) UISlider *NosewingSlider;  //鼻翼      [0,1]
@property (nonatomic, strong) UISlider *NasalHeightSlider;  //鼻长   [-1,1]
@property (nonatomic, strong) UISlider *NOSE_TIP_HEIGHTSlider;  //鼻头长  [-1,1]
@property (nonatomic, strong) UISlider *MouthWidthSlider;   //唇宽    [-1,1]
@property (nonatomic, strong) UISlider *MOUTH_SIZESlider;   //嘴唇大小  [-1,1]
@property (nonatomic, strong) UISlider *MouthHighSlider;    //唇高     [-1,1]
@property (nonatomic, strong) UISlider *PHILTRUMSlider;     //人中     [0,1]

@property (nonatomic, copy) PublishMenuBlock publishBlock;
@property (nonatomic, copy) BeautifySwitch beautifySwitchBlock;


+ (instancetype)instanceView;

/** 初始化视图窗口 并且设置回调方法*/
- (void)showViewSelectBlock:(PublishMenuBlock)block;

/** 显示视图*/
- (void)show;

/** 隐藏视图*/
- (void)hide;

@end
