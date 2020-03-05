//
// Created by wangty on 2019-11-06.
//

#ifndef ALI_ALR_FACE_DETECT_H
#define ALI_ALR_FACE_DETECT_H
#ifdef __ANDROID__
#include <jni.h>
#endif
#include <stdint.h>
#include "aliyun_common.h"
#ifdef __cplusplus
extern "C" {
#endif

#define ALR_FACE_DETECT_MODE_VIDEO           0x10000000  // video
#define ALR_FACE_DETECT_MODE_IMAGE           0x20000000  // image
/// 网络模型
#define ALR_FACE_DETECT_NETWORK_HBN          0x00000001  // HBN
#define ALR_FACE_DETECT_NETWORK_FASTERRCNN   0x00000002  // Faster RCNN

typedef struct aliyun_rect_t
{
    int left;    //left of face rectangle
    int top;     //top of face rectangle
    int right;   //right of face rectangle
    int bottom;  //bottom of face rectangle
} aliyun_rect_t;

typedef struct aliyun_point_t
{
    float x;
    float y;
} aliyun_point_t;

typedef struct aliyun_face_t
{
    aliyun_rect_t rect;                        // face rectangle
    float score;                         // confidence
    aliyun_point_t landmarks_array[106];       // 106 facial points
    float landmarks_visible_array[106];  // visibility of 106 facial points
    float yaw;                           // 水平转角，真实度量的左负右正
    float pitch;                         // 俯仰角，真实度量的上负下正
    float roll;                          // 旋转角，真实度量的左负右正
    float eye_distance;                  // 两眼间距
    int faceID;
} aliyun_face_t;

typedef struct aliyun_face_info_t
{
    aliyun_face_t *p_faces;                 //face info
    int face_count;                      //face detection num
} aliyun_face_info_t;

typedef enum
{
    ALR_FACE_PARAM_DETECT_INTERVAL        = 1,  // 人脸检测的帧率（默认值30，即每隔30帧检测一次）
    ALR_FACE_PARAM_SMOOTH_THRESHOLD       = 2,  // 人脸关键点平滑系数（默认值0.25）.
    ALR_FACE_PARAM_POSE_SMOOTH_THRESHOLD  = 4,  // 姿态平衡系数(0,1], 越大平滑成都越大
    ALR_FACE_PARAM_DETECT_THRESHOLD       = 5,  // 人脸检测阈值(0,1), 阈值越大，误检越少，但漏检测会增加, default 0.95 for faster rcnn; default 0.3 for SSD
    ALR_FACE_PARAM_ALIGNMENT_INTERVAL     = 11, // 人脸检测对齐间隔，默认1，一般不要超过5
    ALR_FACE_PARAM_MAX_FACE_SUPPORT       = 12, // 最多支持检出的人脸个数，最大设为32, 主要针对faster rcnn
    ALR_FACE_PARAM_DETECT_IMG_SIZE        = 13, // 人脸检测输入的图像大小，default： 240 for faster rcnn, recommend set 320 for tiny face detection.
} aliyun_face_param_type_t;

#ifdef __ANDROID__
/**
 * 创建人脸检测句柄
 * @param handle
 * @param det_paraPath 人脸检测模型的路径
 * @param pts_paraPath 关键点检测模型的路径
 * @param config
 * @return == 0 OK; < 0 error
 */
RACE_EXTERN int aliyun_face_create(race_t* handle,
                                   JNIEnv* env,
                                   const char* det_paraPath,
                                   const char* pts_paraPath,
                                   unsigned int config);
#else
/**
 * 创建人脸检测句柄
 * @param handle with initialed
 * @param det_paraPath 人脸检测模型的路径
 * @param pts_paraPath 关键点检测模型的路径
 * @param config 
 * @return == 0 OK; < 0 error
 */
RACE_EXTERN int aliyun_face_create(race_t* handle,
                       const char* det_paraPath,
                       const char* pts_paraPath,
                       unsigned int config);

/**
 * 创建人脸检测句柄
 * @param handle with initialed
 * @param config
 * @return == 0 OK;  < 0 error
 */
RACE_EXTERN int aliyun_face_default_create(race_t* handle, unsigned int config);
#endif

/**
 * 设置人脸检测参数
 * @param handle with initialed
 * @param type face_param_type
 * @param value new threshold
 * @return = 0 OK; < 0 error
 */
RACE_EXTERN int aliyun_face_setParam(race_t handle, aliyun_face_param_type_t type, float value);

/**
 * 人脸检测
 * @param handle with initialed
 * @param buffer input image
 * @param format support type BGR、RGBA、RGB、Y(GRAY)，推荐使用RGBA
 * @param width  width
 * @param height height
 * @param bytesPerRow 用于检测的图像的跨度(以像素为单位),即每行的字节数,
 * @param rotation rotate image to frontalization for face detection
 * @param config MOBILE_FACE_DETECT, or MOBILE_FACE_DETECT|MOBILE_EYE_BLINK et.al 默认值0
 * @param outRotation result process rotate specific angle first, angle =  0/90/180/270
 * @param outFlipAxis  flip x/y 0(no flip)/1(flip X axis)/2(flip Y axis)
 * @param faceInfo store face detetion result
 * @return = 0 OK; < 0 error
 */
RACE_EXTERN int aliyun_face_detect(race_t handle,
                                   uint8_t *buffer,
                                   aliyun_image_format_t format,
                                   uint32_t width,
                                   uint32_t height,
                                   uint32_t bytesPerRow,
                                   aliyun_rotation_t rotation,
                                   uint32_t config,
                                   aliyun_rotation_t outRotation,
                                   uint32_t outFlipAxis,
                                   aliyun_face_info_t* faceInfo);

/**
 * 销毁人脸检测句柄
 * @param handle  with initialed
 */
RACE_EXTERN void aliyun_face_destroy(race_t handle);

#ifdef __cplusplus
}
#endif
#endif //ALI_ALR_FACE_DETECT_H
