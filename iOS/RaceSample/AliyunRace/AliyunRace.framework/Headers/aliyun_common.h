#ifndef RACEBUSINESS_ALR_COMMON_H
#define RACEBUSINESS_ALR_COMMON_H

#ifndef RACE_EXTERN
#if defined(_MSC_VER_)
#ifdef RACE_SHARED_LIB
#define RACE_EXTERN __declspec(dllexport)
#else
#define RACE_EXTERN __declspec(dllimport)
#endif
#else
#define RACE_EXTERN __attribute__((visibility("default")))
#endif
#endif

#ifdef __cplusplus
extern "C" {
#endif

#define ALR_FLAG_TEXTURE_OES_EXTERNAL    0x01   // OES 纹理
#define ALR_FLAG_IMAGE_FLIP_X            0x02   // 水平方向翻转
#define ALR_FLAG_IMAGE_FLIP_Y            0x04   // 垂直方向翻转
#define ALR_FLAG_OUTPUT_ROTATE_0         0x08   // 输出图像正置处理
#define ALR_FLAG_OUTPUT_FLIP_X           0x10   // 输出图像水平方向翻转处理
#define ALR_FLAG_OUTPUT_FLIP_Y           0x20   // 输出图像垂直方向翻转处理

/**
 * 美颜美型句柄
 */
typedef void* race_t;

/**
 * 图像色彩格式类型
 */
typedef enum aliyun_image_format_t
{
    ALR_IMAGE_FORMAT_RGB   = 2,
    ALR_IMAGE_FORMAT_BGRA  = 4,
    ALR_IMAGE_FORMAT_RGBA  = 5,
    ALR_IMAGE_FORMAT_NV21  = 7,
    ALR_IMAGE_FORMAT_NV12  = 8
} aliyun_image_format_t;

/**
 * 图像颜色范围
 */
typedef enum
{
    ALR_COLOR_RANGE_FULL,
    ALR_COLOR_RANGE_LIMITED
} aliyun_color_range_t;

/**
 * 图像色彩标准
 */
typedef enum
{
    ALR_COLOR_STANDARD_BT709 = 1,
    ALR_COLOR_STANDARD_BT601 = 2,
} aliyun_color_standard_t;

/**
 * 旋转角度（顺时针方向）
 */
typedef enum
{
    ALR_ROTATE_0_CW    = 0,
    ALR_ROTATE_90_CW   = 90,
    ALR_ROTATE_180_CW  = 180,
    ALR_ROTATE_270_CW  = 270,
} aliyun_rotation_t;

/**
 * 日志等级
 */
enum
{
    ALR_LOG_LEVEL_VERBOSE = 2,
    ALR_LOG_LEVEL_DEBUG,
    ALR_LOG_LEVEL_INFO,
    ALR_LOG_LEVEL_WARN,
    ALR_LOG_LEVEL_ERROR,
    ALR_LOG_LEVEL_FATAL,
};

/**
 * 返回值
 */
enum
{
    ALR_OK                   = 0,
    ALR_FAIL                 = -1,
    ALR_INVALID_HANDLE       = -2,
    ALR_INVALID_VALUE        = -3,
    ALR_INVALID_LICENSE      = -4,
    ALR_INVALID_FORMAT       = -5,
};

/**
 * 创建纹理，支持 RGB/RGBA/BGRA
 * @param width 纹理宽度
 * @param height 纹理高度
 * @param format 图像格式
 * @return 成功返回纹理 id，失败 <0
 */
RACE_EXTERN int aliyun_createTexture(int width, int height, aliyun_image_format_t format);
/**
 * 销毁纹理
 * @param texture 纹理 id
 */
RACE_EXTERN void aliyun_destroyTexture(int texture);
/**
 * 设置输出日志等级
 * @param level 日志等级
 */
RACE_EXTERN void aliyun_setLogLevel(int level);

#ifdef __cplusplus
}
#endif

#endif //RACEBUSINESS_ALR_COMMON_H
