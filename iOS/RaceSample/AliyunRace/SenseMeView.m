//
//  PublishConfigMenuView.m
//  SampleManualMode
//
//  Created by 高宇 on 2019/4/24.
//  Copyright © 2019 tiantian. All rights reserved.
//

#import "SenseMeView.h"
#import "AliyunRace.h"

@interface SenseMeView ()

@end

@implementation SenseMeView

/** 获取实例 */
+ (instancetype)instanceView {
    NSString *nibName = NSStringFromClass([SenseMeView class]);
    NSArray *views = [[NSBundle mainBundle]loadNibNamed:nibName owner:self options:nil];
    return views.firstObject;
}


/** 显示视图窗口 并且设置回调方法*/
- (void)showViewSelectBlock:(PublishMenuBlock)block {
    self.publishBlock = block;
    
    //初始化UI等内容
    [self setUI];
}

- (void)setUI{
    self.layer.cornerRadius = 5;
    self.layer.masksToBounds = YES;
    
    UILabel *publishConfigLabel = [[UILabel alloc] initWithFrame:CGRectMake(8, 0, self.frame.size.width-16, 30)];
    publishConfigLabel.text = @"美颜设置";
    publishConfigLabel.textColor = [UIColor cyanColor];
    [self addSubview:publishConfigLabel];
    
    self.scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 40, self.frame.size.width, self.frame.size.height-80)];
    self.scrollView.contentSize = CGSizeMake(0, 1080);
    [self addSubview:self.scrollView];
    
    self.switchLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 80, 31)];
    self.switchLabel.textColor = [UIColor whiteColor];
    self.switchLabel.text = @"美颜开关";
    [self.scrollView addSubview:self.switchLabel];
    
    self.switchBeautify = [[UISwitch alloc] initWithFrame:CGRectMake(100, self.switchLabel.frame.origin.y, self.frame.size.width-110, 31)];
    self.switchBeautify.on = YES;
    [self.switchBeautify addTarget:self action:@selector(switchBeautifyClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.scrollView addSubview:self.switchBeautify];
    
    UILabel *SkinWhiteningLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, _switchLabel.frame.origin.y+40, 50, 31)];
    SkinWhiteningLabel.textColor = [UIColor whiteColor];
    SkinWhiteningLabel.text = @"美白";
    [self.scrollView addSubview:SkinWhiteningLabel];
    
    _SkinWhiteningSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, SkinWhiteningLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _SkinWhiteningSlider.value = 1;
    _SkinWhiteningSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_SkinWhiteningSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_SkinWhiteningSlider];
    
    UILabel *SkinBuffingLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, SkinWhiteningLabel.frame.origin.y+40, 50, 31)];
    SkinBuffingLabel.textColor = [UIColor whiteColor];
    SkinBuffingLabel.text = @"磨皮";
    [self.scrollView addSubview:SkinBuffingLabel];
    
    _SkinBuffingSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, SkinBuffingLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _SkinBuffingSlider.value = 1;
    _SkinBuffingSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_SkinBuffingSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_SkinBuffingSlider];
    
    UILabel *SharpenLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, SkinBuffingLabel.frame.origin.y+40, 50, 31)];
    SharpenLabel.textColor = [UIColor whiteColor];
    SharpenLabel.text = @"锐化";
    [self.scrollView addSubview:SharpenLabel];
    
    _SharpenSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, SharpenLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _SharpenSlider.value = 0.5;
    _SharpenSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_SharpenSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_SharpenSlider];
    
    UILabel *CUT_CHEEKLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, SharpenLabel.frame.origin.y+40, 50, 31)];
    CUT_CHEEKLabel.textColor = [UIColor whiteColor];
    CUT_CHEEKLabel.text = @"颧骨";
    [self.scrollView addSubview:CUT_CHEEKLabel];
    
    _CUT_CHEEKSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, CUT_CHEEKLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _CUT_CHEEKSlider.value = 0;
    //小于当前滑动的颜色
    _CUT_CHEEKSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_CUT_CHEEKSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_CUT_CHEEKSlider];
    
    UILabel *CutFaceLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, CUT_CHEEKLabel.frame.origin.y+40, 50, 31)];
    CutFaceLabel.textColor = [UIColor whiteColor];
    CutFaceLabel.text = @"削脸";
    [self.scrollView addSubview:CutFaceLabel];
    
    _CutFaceSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, CutFaceLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _CutFaceSlider.value = 0;
    //小于当前滑动的颜色
    _CutFaceSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_CutFaceSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_CutFaceSlider];
    
    UILabel *ThinFaceLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, CutFaceLabel.frame.origin.y+40, 50, 31)];
    ThinFaceLabel.textColor = [UIColor whiteColor];
    ThinFaceLabel.text = @"瘦脸";
    [self.scrollView addSubview:ThinFaceLabel];

    _ThinFaceSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, ThinFaceLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _ThinFaceSlider.value = 0;
    //小于当前滑动的颜色
    _ThinFaceSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_ThinFaceSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_ThinFaceSlider];

    UILabel *LongFaceLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, ThinFaceLabel.frame.origin.y+40, 50, 31)];
    LongFaceLabel.textColor = [UIColor whiteColor];
    LongFaceLabel.text = @"脸长";
    [self.scrollView addSubview:LongFaceLabel];

    _LongFaceSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, LongFaceLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _LongFaceSlider.value = 0;
    //小于当前滑动的颜色
    _LongFaceSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_LongFaceSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_LongFaceSlider];
    
    UILabel *LowerJawLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, LongFaceLabel.frame.origin.y+40, 80, 31)];
    LowerJawLabel.textColor = [UIColor whiteColor];
    LowerJawLabel.text = @"下巴缩短";
    [self.scrollView addSubview:LowerJawLabel];
    
    _LowerJawSlider = [[UISlider alloc] initWithFrame:CGRectMake(90, LowerJawLabel.frame.origin.y, self.frame.size.width-100, 31)];
    _LowerJawSlider.minimumValue = -1;
    _LowerJawSlider.maximumValue = 1;
    _LowerJawSlider.value = 0;
    //小于当前滑动的颜色
    _LowerJawSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_LowerJawSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_LowerJawSlider];
    
    UILabel *HIGHER_JAWLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, LowerJawLabel.frame.origin.y+40, 80, 31)];
    HIGHER_JAWLabel.textColor = [UIColor whiteColor];
    HIGHER_JAWLabel.text = @"下巴拉长";
    [self.scrollView addSubview:HIGHER_JAWLabel];
    
    _HIGHER_JAWSlider = [[UISlider alloc] initWithFrame:CGRectMake(90, HIGHER_JAWLabel.frame.origin.y, self.frame.size.width-100, 31)];
    _HIGHER_JAWSlider.minimumValue = -1;
    _HIGHER_JAWSlider.maximumValue = 1;
    _HIGHER_JAWSlider.value = 0;
    //小于当前滑动的颜色
    _HIGHER_JAWSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_HIGHER_JAWSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_HIGHER_JAWSlider];
    
    UILabel *THIN_JAWLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, HIGHER_JAWLabel.frame.origin.y+40, 80, 31)];
    THIN_JAWLabel.textColor = [UIColor whiteColor];
    THIN_JAWLabel.text = @"瘦下巴";
    [self.scrollView addSubview:THIN_JAWLabel];
    
    _THIN_JAWSlider = [[UISlider alloc] initWithFrame:CGRectMake(90, THIN_JAWLabel.frame.origin.y, self.frame.size.width-100, 31)];
    _THIN_JAWSlider.minimumValue = -1;
    _THIN_JAWSlider.maximumValue = 1;
    _THIN_JAWSlider.value = 0;
    //小于当前滑动的颜色
    _THIN_JAWSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_THIN_JAWSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_THIN_JAWSlider];
    
    UILabel *THIN_MANDIBLELabel = [[UILabel alloc] initWithFrame:CGRectMake(10, THIN_JAWLabel.frame.origin.y+40, 80, 31)];
    THIN_MANDIBLELabel.textColor = [UIColor whiteColor];
    THIN_MANDIBLELabel.text = @"瘦下颌";
    [self.scrollView addSubview:THIN_MANDIBLELabel];
    
    _THIN_MANDIBLESlider = [[UISlider alloc] initWithFrame:CGRectMake(90, THIN_MANDIBLELabel.frame.origin.y, self.frame.size.width-100, 31)];
    _THIN_MANDIBLESlider.value = 0;
    //小于当前滑动的颜色
    _THIN_MANDIBLESlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_THIN_MANDIBLESlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_THIN_MANDIBLESlider];
    
    UILabel *BigEyeLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, THIN_MANDIBLELabel.frame.origin.y+40, 50, 31)];
    BigEyeLabel.textColor = [UIColor whiteColor];
    BigEyeLabel.text = @"大眼";
    [self.scrollView addSubview:BigEyeLabel];
    
    _BigEyeSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, BigEyeLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _BigEyeSlider.value = 0;
    //小于当前滑动的颜色
    _BigEyeSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_BigEyeSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_BigEyeSlider];
    
    UILabel *EyeAngle1Label = [[UILabel alloc] initWithFrame:CGRectMake(10, BigEyeLabel.frame.origin.y+40, 50, 31)];
    EyeAngle1Label.textColor = [UIColor whiteColor];
    EyeAngle1Label.text = @"眼角1";
    [self.scrollView addSubview:EyeAngle1Label];
    
    _EyeAngle1Slider = [[UISlider alloc] initWithFrame:CGRectMake(60, EyeAngle1Label.frame.origin.y, self.frame.size.width-70, 31)];
    _EyeAngle1Slider.value = 0;
    //小于当前滑动的颜色
    _EyeAngle1Slider.minimumTrackTintColor = [UIColor cyanColor];;
    [_EyeAngle1Slider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_EyeAngle1Slider];
    
    UILabel *CanthusLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, EyeAngle1Label.frame.origin.y+40, 50, 31)];
    CanthusLabel.textColor = [UIColor whiteColor];
    CanthusLabel.text = @"眼距";
    [self.scrollView addSubview:CanthusLabel];
    
    _CanthusSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, CanthusLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _CanthusSlider.minimumValue = -1;
    _CanthusSlider.maximumValue = 1;
    _CanthusSlider.value = 0;
    _CanthusSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_CanthusSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_CanthusSlider];
    
    UILabel *CANTHUS1Label = [[UILabel alloc] initWithFrame:CGRectMake(10, CanthusLabel.frame.origin.y+40, 80, 31)];
    CANTHUS1Label.textColor = [UIColor whiteColor];
    CANTHUS1Label.text = @"拉宽眼距";
    [self.scrollView addSubview:CANTHUS1Label];
    
    _CANTHUS1Slider = [[UISlider alloc] initWithFrame:CGRectMake(90, CANTHUS1Label.frame.origin.y, self.frame.size.width-100, 31)];
    _CANTHUS1Slider.minimumValue = -1;
    _CANTHUS1Slider.maximumValue = 1;
    _CANTHUS1Slider.value = 0;
    //小于当前滑动的颜色
    _CANTHUS1Slider.minimumTrackTintColor = [UIColor cyanColor];;
    [_CANTHUS1Slider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_CANTHUS1Slider];
    
    UILabel *EyeAngle2Label = [[UILabel alloc] initWithFrame:CGRectMake(10, CANTHUS1Label.frame.origin.y+40, 50, 31)];
    EyeAngle2Label.textColor = [UIColor whiteColor];
    EyeAngle2Label.text = @"眼角2";
    [self.scrollView addSubview:EyeAngle2Label];
    
    _EyeAngle2Slider = [[UISlider alloc] initWithFrame:CGRectMake(60, EyeAngle2Label.frame.origin.y, self.frame.size.width-70, 31)];
    _EyeAngle2Slider.minimumValue = -1;
    _EyeAngle2Slider.maximumValue = 1;
    _EyeAngle2Slider.value = 0;
    _EyeAngle2Slider.minimumTrackTintColor = [UIColor cyanColor];;
    [_EyeAngle2Slider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_EyeAngle2Slider];
    
    UILabel *EYE_TDANGLELabel = [[UILabel alloc] initWithFrame:CGRectMake(10, EyeAngle2Label.frame.origin.y+40, 80, 31)];
    EYE_TDANGLELabel.textColor = [UIColor whiteColor];
    EYE_TDANGLELabel.text = @"眼睛高度";
    [self.scrollView addSubview:EYE_TDANGLELabel];
    
    _EYE_TDANGLESlider = [[UISlider alloc] initWithFrame:CGRectMake(90, EYE_TDANGLELabel.frame.origin.y, self.frame.size.width-100, 31)];
    _EYE_TDANGLESlider.minimumValue = -1;
    _EYE_TDANGLESlider.maximumValue = 1;
    _EYE_TDANGLESlider.value = 0;
    _EYE_TDANGLESlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_EYE_TDANGLESlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_EYE_TDANGLESlider];
    
    UILabel *ThinNoseLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, EYE_TDANGLELabel.frame.origin.y+40, 50, 31)];
    ThinNoseLabel.textColor = [UIColor whiteColor];
    ThinNoseLabel.text = @"瘦鼻";
    [self.scrollView addSubview:ThinNoseLabel];
    
    _ThinNoseSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, ThinNoseLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _ThinNoseSlider.value = 0;
    _ThinNoseSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_ThinNoseSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_ThinNoseSlider];
    
    UILabel *NosewingLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, ThinNoseLabel.frame.origin.y+40, 50, 31)];
    NosewingLabel.textColor = [UIColor whiteColor];
    NosewingLabel.text = @"鼻翼";
    [self.scrollView addSubview:NosewingLabel];
    
    _NosewingSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, NosewingLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _NosewingSlider.value = 0;
    _NosewingSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_NosewingSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_NosewingSlider];
    
    UILabel *NasalHeightLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, NosewingLabel.frame.origin.y+40, 50, 31)];
    NasalHeightLabel.textColor = [UIColor whiteColor];
    NasalHeightLabel.text = @"鼻长";
    [self.scrollView addSubview:NasalHeightLabel];
    
    _NasalHeightSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, NasalHeightLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _NasalHeightSlider.minimumValue = -1;
    _NasalHeightSlider.maximumValue = 1;
    _NasalHeightSlider.value = 0;
    _NasalHeightSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_NasalHeightSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_NasalHeightSlider];
    
    UILabel *NOSE_TIP_HEIGHTLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, NasalHeightLabel.frame.origin.y+40, 80, 31)];
    NOSE_TIP_HEIGHTLabel.textColor = [UIColor whiteColor];
    NOSE_TIP_HEIGHTLabel.text = @"鼻头长";
    [self.scrollView addSubview:NOSE_TIP_HEIGHTLabel];
    
    _NOSE_TIP_HEIGHTSlider = [[UISlider alloc] initWithFrame:CGRectMake(90, NOSE_TIP_HEIGHTLabel.frame.origin.y, self.frame.size.width-100, 31)];
    _NOSE_TIP_HEIGHTSlider.minimumValue = -1;
    _NOSE_TIP_HEIGHTSlider.maximumValue = 1;
    _NOSE_TIP_HEIGHTSlider.value = 0;
    _NOSE_TIP_HEIGHTSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_NOSE_TIP_HEIGHTSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_NOSE_TIP_HEIGHTSlider];
    
    UILabel *MouthWidthLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, NOSE_TIP_HEIGHTLabel.frame.origin.y+40, 50, 31)];
    MouthWidthLabel.textColor = [UIColor whiteColor];
    MouthWidthLabel.text = @"唇宽";
    [self.scrollView addSubview:MouthWidthLabel];
    
    _MouthWidthSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, MouthWidthLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _MouthWidthSlider.minimumValue = -1;
    _MouthWidthSlider.maximumValue = 1;
    _MouthWidthSlider.value = 0;
    _MouthWidthSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_MouthWidthSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_MouthWidthSlider];
    
    UILabel *MOUTH_SIZELabel = [[UILabel alloc] initWithFrame:CGRectMake(10, MouthWidthLabel.frame.origin.y+40, 80, 31)];
    MOUTH_SIZELabel.textColor = [UIColor whiteColor];
    MOUTH_SIZELabel.text = @"嘴唇大小";
    [self.scrollView addSubview:MOUTH_SIZELabel];
    
    _MOUTH_SIZESlider = [[UISlider alloc] initWithFrame:CGRectMake(90, MOUTH_SIZELabel.frame.origin.y, self.frame.size.width-100, 31)];
    _MOUTH_SIZESlider.minimumValue = -1;
    _MOUTH_SIZESlider.maximumValue = 1;
    _MOUTH_SIZESlider.value = 0;
    _MOUTH_SIZESlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_MOUTH_SIZESlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_MOUTH_SIZESlider];
    
    UILabel *MouthHighLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, MOUTH_SIZELabel.frame.origin.y+40, 50, 31)];
    MouthHighLabel.textColor = [UIColor whiteColor];
    MouthHighLabel.text = @"唇高";
    [self.scrollView addSubview:MouthHighLabel];
    
    _MouthHighSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, MouthHighLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _MouthHighSlider.minimumValue = -1;
    _MouthHighSlider.maximumValue = 1;
    _MouthHighSlider.value = 0;
    _MouthHighSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_MouthHighSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_MouthHighSlider];
    
    UILabel *PHILTRUMLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, MouthHighLabel.frame.origin.y+40, 50, 31)];
    PHILTRUMLabel.textColor = [UIColor whiteColor];
    PHILTRUMLabel.text = @"人中";
    [self.scrollView addSubview:PHILTRUMLabel];
    
    _PHILTRUMSlider = [[UISlider alloc] initWithFrame:CGRectMake(60, PHILTRUMLabel.frame.origin.y, self.frame.size.width-70, 31)];
    _PHILTRUMSlider.value = 0;
    _PHILTRUMSlider.minimumTrackTintColor = [UIColor cyanColor];;
    [_PHILTRUMSlider addTarget:self action:@selector(sliderValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.scrollView addSubview:_PHILTRUMSlider];
    
    
    // 确定
    UIButton *determineButton = [UIButton buttonWithType:UIButtonTypeCustom];
    determineButton.frame = CGRectMake(0, self.frame.size.height - 35, self.frame.size.width, 30);
    [determineButton setTitle:@"确定" forState:0];
    [determineButton setTitleColor:[UIColor cyanColor] forState:0];
    [determineButton addTarget:self action:@selector(determineButtonClick:) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:determineButton];
    
}

- (void)show{
    self.transform = CGAffineTransformMakeScale(1.1, 1.1);
    self.alpha = 0.0;
    [UIView animateWithDuration:0.3 delay:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        self.transform = CGAffineTransformIdentity;
        self.alpha = 1.0;
    } completion:^(BOOL finished) {
        
    }];
}

- (void)hide{
    //动画隐藏
    [UIView animateWithDuration:0.2 delay:0 options:UIViewAnimationOptionCurveEaseOut animations:^{
        self.transform = CGAffineTransformMakeScale(0.9, 0.9);
        self.alpha = 0.0;;
    } completion:^(BOOL finished) {
        
    }];
}

//确定按钮
- (void)determineButtonClick:(UIButton *)button{
    if (self.publishBlock){
        self.publishBlock();
    }
}

//美颜开关按钮
- (void)switchBeautifyClick:(UISwitch *)sw{
    if (self.beautifySwitchBlock){
        self.beautifySwitchBlock(sw.on);
    }
}

//滑动条触发事件
-(void)sliderValueChanged:(UISlider *)slider
{
    if (slider == self.SkinWhiteningSlider){
        [[AliyunRace shared] setSkinWhitening:slider.value];
    }else if (slider == self.SkinBuffingSlider){
        [[AliyunRace shared] setSkinBuffing:slider.value];
    }else if (slider == self.SharpenSlider){
        [[AliyunRace shared] setSharpen:slider.value];
    }else{
        int type;
        if (slider == self.CUT_CHEEKSlider) {
            type = 0;
        }else if (slider == self.CutFaceSlider) {
            type = 1;
        }else if (slider == self.ThinFaceSlider){
            type = 2;
        }else if (slider == self.LongFaceSlider){
            type = 3;
        }else if (slider == self.LowerJawSlider){
            type = 4;
        }else if (slider == self.HIGHER_JAWSlider){
            type = 5;
        }else if (slider == self.THIN_JAWSlider){
            type = 6;
        }else if (slider == self.THIN_MANDIBLESlider){
            type = 7;
        }else if (slider == self.BigEyeSlider){
            type = 8;
        }else if (slider == self.EyeAngle1Slider){
            type = 9;
        }else if (slider == self.CanthusSlider){
            type = 10;
        }else if (slider == self.CANTHUS1Slider){
            type = 11;
        }else if (slider == self.EyeAngle2Slider){
            type = 12;
        }else if (slider == self.EYE_TDANGLESlider){
            type = 13;
        }else if (slider == self.ThinNoseSlider){
            type = 14;
        }else if (slider == self.NosewingSlider){
            type = 15;
        }else if (slider == self.NasalHeightSlider){
            type = 16;
        }else if (slider == self.NOSE_TIP_HEIGHTSlider){
            type = 17;
        }else if (slider == self.MouthWidthSlider){
            type = 18;
        }else if (slider == self.MOUTH_SIZESlider){
            type = 19;
        }else if (slider == self.MouthHighSlider){
            type = 20;
        }else if (slider == self.PHILTRUMSlider){
            type = 21;
        }else{
            return;
        }
        [[AliyunRace shared] setFaceShape:type Level:slider.value];
    }
}





@end
