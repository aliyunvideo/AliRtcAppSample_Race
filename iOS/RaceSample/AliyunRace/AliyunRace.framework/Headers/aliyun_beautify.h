#ifndef ALIYUN_RACE_BEAUTIFY_H
#define ALIYUN_RACE_BEAUTIFY_H
#include <stdint.h>
#include "aliyun_common.h"

#ifdef __ANDROID__
#include <jni.h>
#elif defined(__APPLE__)
#import <CoreMedia/CoreMedia.h>
#endif

#ifdef __cplusplus
extern "C" {
#endif

/**
 * 美型形状类型
 */
typedef enum ALRFaceShape
{
    ALR_FACE_TYPE_CUT_CHEEK       = 0,//颧骨        [0,1]
    ALR_FACE_TYPE_CUT_FACE        = 1,//削脸        [0,1]
    ALR_FACE_TYPE_THIN_FACE       = 2,//瘦脸       [0,1]
    ALR_FACE_TYPE_LONG_FACE       = 3,//脸长       [0,1]
    ALR_FACE_TYPE_LOWER_JAW       = 4,//下巴缩短    [-1,1]
    ALR_FACE_TYPE_HIGHER_JAW      = 5,//下巴拉长    [-1,1]
    ALR_FACE_TYPE_THIN_JAW        = 6,//瘦下巴    [-1,1]
    ALR_FACE_TYPE_THIN_MANDIBLE   = 7,//瘦下颌   [0,1]
    ALR_FACE_TYPE_BIG_EYE         = 8, //大眼        [0,1]
    ALR_FACE_TYPE_EYE_ANGLE1      = 9,//眼角1     [0,1]
    ALR_FACE_TYPE_CANTHUS         = 10,//眼距       [-1,1]
    ALR_FACE_TYPE_CANTHUS1        = 11,//拉宽眼距   [-1,1]
    ALR_FACE_TYPE_EYE_ANGLE2      = 12,//眼角2    [-1,1]
    ALR_FACE_TYPE_EYE_TDANGLE     = 13,//眼睛高度 [-1,1]
    ALR_FACE_TYPE_THIN_NOSE       = 14,//瘦鼻      [0,1]
    ALR_FACE_TYPE_NOSE_WING       = 15,//鼻翼      [0,1]
    ALR_FACE_TYPE_NASAL_HEIGHT    = 16,//鼻长   [-1,1]
    ALR_FACE_TYPE_NOSE_TIP_HEIGHT = 17,//鼻头长[-1,1]
    ALR_FACE_TYPE_MOUTH_WIDTH     = 18,//唇宽    [-1,1]
    ALR_FACE_TYPE_MOUTH_SIZE      = 19,//嘴唇大小  [-1,1]
    ALR_FACE_TYPE_MOUTH_HIGH      = 20,//唇高     [-1,1]
    ALR_FACE_TYPE_PHILTRUM        = 21, //人中
    ALR_FACE_TYPE_MAX             = 22
} ALRFaceShape;

/**
 *  获取当前美颜美型版本号
 *  @param major 主版本号
 *  @param minor 次版本号
 */
RACE_EXTERN void aliyun_beautify_getVersion(int &major, int &minor);
#ifdef __ANDROID__
/**
 * 创建美颜美型实例
 * @param handle 美颜美型句柄指针
 * @param env
 * @param resDir 资源文件目录绝对路径
 * @param rid 数据上报标识符
 * @param lid 日志上报标识符
 * @return 成功返回 ALR_OK，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_create(race_t *handle, JNIEnv *env, const char *resDir, int64_t rid, int64_t lid);
#else
/**
 * 创建美颜美型实例
 * @param handle 美颜美型句柄指针
 * @return 成功返回 ALR_OK，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_create(race_t *handle);
#endif
/**
 * 美颜美型调试开关
 * @param handle 美颜美型句柄
 * @param enable 调试开启标志
 * @return 成功返回 ALR_OK，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_setFaceDebug(race_t handle, bool enable);
/**
 * 美型开关
 * @param handle 美颜美型句柄
 * @param switchOn 是否开启人脸检测及美型处理
 */
RACE_EXTERN void aliyun_beautify_setFaceSwitch(race_t handle, bool switchOn);

/**
 * 磨皮
 * @param handle 美颜美型句柄
 * @param level 磨皮等级，常用取值范围 [0, 1]
 * @return 0, ok; <0 ,error
 */
RACE_EXTERN int aliyun_beautify_setSkinBuffing(race_t handle, float level);
/**
 * 锐化
 * @param handle 美颜美型句柄
 * @param level 锐化等级，常用取值范围 [0, 1]
 * @return 0, ok; <0 ,error
 */
RACE_EXTERN int aliyun_beautify_setSharpen(race_t handle, float level);
/**
 * 美白
 * @param handle 美颜美型句柄
 * @param level 美白等级，常用取值范围 [0, 1]
 * @return 0, ok; <0 ,error
 */
RACE_EXTERN int aliyun_beautify_setSkinWhitening(race_t handle, float level);
/**
 * 设置美型参数
 * @param handle 美颜美型句柄
 * @param level 美型等级参数，可以超出 ALRFaceShape 中定义的参数范围
 * @return 成功返回 ALR_OK，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_setFaceShape(race_t handle, ALRFaceShape type, float level);
/**
 * 双输入美颜美型渲染
 * @param handle 美颜美型句柄
 * @param textureIn GPU纹理 id
 * @param buffer CPU图像内存地址
 * @param bufferSize CPU图像内存字节数
 * @param format 图像像素格式
 * @param width 图像宽度
 * @param height 图像高度
 * @param bytesPerRow CPU图像内存一行的字节数
 * @param rotation 图像旋转角度（顺进针方向）
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @return 成功则返回输出纹理句柄，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_processDualInputToTexture(race_t handle,
                                                          uint32_t textureIn,
                                                          uint8_t *buffer,
                                                          uint32_t bufferSize,
                                                          aliyun_image_format_t format,
                                                          uint32_t width,
                                                          uint32_t height,
                                                          uint32_t bytesPerRow,
                                                          aliyun_rotation_t rotation,
                                                          uint8_t flags);
/**
 * 双输入美颜美型渲染
 * @param handle 美颜美型句柄
 * @param textureIn GPU纹理 id
 * @param buffer CPU图像内存地址
 * @param bufferSize CPU图像内存字节数
 * @param format 图像像素格式
 * @param width 图像宽度
 * @param height 图像高度
 * @param bytesPerRow CPU图像内存一行的字节数
 * @param rotation 图像旋转角度（顺进针方向）
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @param bufferOut 输出图像地址
 * @return 成功返回 0，失败返回 <0
 */
RACE_EXTERN int aliyun_beautify_processDualInputToBuffer(race_t handle,
                                                         uint32_t textureIn,
                                                         uint8_t *buffer,
                                                         uint32_t bufferSize,
                                                         aliyun_image_format_t format,
                                                         uint32_t width,
                                                         uint32_t height,
                                                         uint32_t bytesPerRow,
                                                         aliyun_rotation_t rotation,
                                                         uint8_t flags,
                                                         uint8_t *bufferOut);
/**
 * 美颜美型纹理输入渲染
 * @param handle 美颜美型句柄
 * @param textureIn 输入图像纹理
 * @param width 输入图像纹理宽度
 * @param height 输入图像纹理高度
 * @param rotation 输入图像纹理旋转角度（顺时针）
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @return 成功则返回输出纹理句柄，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_processTextureToTexture(race_t handle,
                                                        uint32_t textureIn,
                                                        uint32_t width,
                                                        uint32_t height,
                                                        aliyun_rotation_t rotation,
                                                        uint8_t flags);
/**
 * 美颜美型纹理输入渲染
 * @param handle 美颜美型句柄
 * @param textureIn 输入图像纹理
 * @param width 输入图像纹理宽度
 * @param height 输入图像纹理高度
 * @param rotation 输入图像纹理旋转角度（顺时针）
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @param bufferOut 输出图像地址
 * @return 成功返回 0，失败返回 <0
 */
RACE_EXTERN int aliyun_beautify_processTextureToBuffer(race_t handle,
                                                       uint32_t textureIn,
                                                       uint32_t width,
                                                       uint32_t height,
                                                       aliyun_rotation_t rotation,
                                                       uint8_t flags,
                                                       uint8_t *bufferOut);
/**
 * 单输入美颜美型渲染
 * @param handle 美颜美型句柄
 * @param buffer CPU图像内存地址
 * @param bufferSize CPU图像内存字节数
 * @param format 图像像素格式
 * @param width 图像宽度
 * @param height 图像高度
 * @param bytesPerRow CPU图像内存一行的字节数
 * @param rotation 图像旋转角度（顺进针方向）
 * @param range 颜色色域范围
 * @param standard 色彩标准
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @return 成功则返回输出纹理句柄，否则返回 < 0
 */
RACE_EXTERN int aliyun_beautify_processBufferToTexture(race_t handle,
                                                       uint8_t *buffer,
                                                       uint32_t bufferSize,
                                                       aliyun_image_format_t format,
                                                       uint32_t width,
                                                       uint32_t height,
                                                       uint32_t bytesPerRow,
                                                       aliyun_rotation_t rotation,
                                                       aliyun_color_range_t range,
                                                       aliyun_color_standard_t standard,
                                                       uint8_t flags);
/**
 * 单输入美颜美型渲染
 * @param handle 美颜美型句柄
 * @param buffer CPU图像内存地址
 * @param bufferSize CPU图像内存字节数
 * @param format 图像像素格式
 * @param width 图像宽度
 * @param height 图像高度
 * @param bytesPerRow CPU图像内存一行的字节数
 * @param rotation 图像旋转角度（顺进针方向）
 * @param range 颜色色域范围
 * @param standard 色彩标准
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @param bufferOut 输出图像地址
 * @return 成功返回 0，失败返回 <0
 */
RACE_EXTERN int aliyun_beautify_processBufferToBuffer(race_t handle,
                                                      uint8_t *buffer,
                                                      uint32_t bufferSize,
                                                      aliyun_image_format_t format,
                                                      uint32_t width,
                                                      uint32_t height,
                                                      uint32_t bytesPerRow,
                                                      aliyun_rotation_t rotation,
                                                      aliyun_color_range_t range,
                                                      aliyun_color_standard_t standard,
                                                      uint8_t flags,
                                                      uint8_t *bufferOut);
/**
 * 美颜美型渲染
 * @param handle 美颜美型句柄
 * @param textureIn 输入图像纹理
 * @param buffer CPU图像内存地址
 * @param bufferSize CPU图像内存字节数
 * @param format 图像像素格式
 * @param width 图像宽度
 * @param height 图像高度
 * @param bytesPerRow CPU图像内存一行的字节数
 * @param rotation 图像旋转角度（顺进针方向）
 * @param range 颜色色域范围
 * @param standard 色彩标准
 * @param flags 详见 ALR_FLAG_XXX 定义，如 OES 纹理、镜像、输出图像旋转等
 * @param textureOut 输出纹理 id
 * @param bufferOut 输出图像地址
 * @param formatOut 输出图像格式
 * @param widthOut 输出图像宽度
 * @param heightOut 输出图像高度
 * @return 成功返回 0，失败返回 <0
 */
RACE_EXTERN int aliyun_beautify_process(race_t handle,
                                        uint32_t textureIn,
                                        uint8_t *buffer,
                                        uint32_t bufferSize,
                                        aliyun_image_format_t format,
                                        uint32_t width,
                                        uint32_t height,
                                        uint32_t bytesPerRow,
                                        aliyun_rotation_t rotation,
                                        aliyun_color_range_t range,
                                        aliyun_color_standard_t standard,
                                        uint8_t flags,
                                        uint32_t textureOut,
                                        uint8_t *bufferOut,
                                        aliyun_image_format_t formatOut,
                                        uint32_t widthOut,
                                        uint32_t heightOut);
#ifdef __APPLE__
/**
 *  高级美颜，输入是类型 CMSampleBufferRef 的图像数据，返回与输入同样大小的纹理 id
 *  @param handle 创建成功的句柄
 *  @param sampleBuffer 图像数据 CMSampleBufferRef
 *  @return 成功则返回渲染纹理 id，失败返回 0
 */
RACE_EXTERN int aliyun_beautify_processSampleBuffer(race_t handle, CMSampleBufferRef sampleBuffer);
#endif
/**
 * 销毁美颜美型实例
 * @param handle 美颜美型句柄
 */
RACE_EXTERN void aliyun_beautify_destroy(race_t handle);

#ifdef __cplusplus
}
#endif

#endif // ALIYUN_RACE_BEAUTIFY_H
