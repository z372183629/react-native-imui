//
//  RCTAuroraIMUIModule.m
//  RCTAuroraIMUIModule
//
//  Created by oshumini on 2017/6/1.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import "RCTAuroraIMUIModule.h"
#import "DWShowImageVC.h"

#define SCREEN_W [UIScreen mainScreen].bounds.size.width
#define SCREEN_H [UIScreen mainScreen].bounds.size.height

@interface RCTAuroraIMUIModule () {
}

@end

@implementation RCTAuroraIMUIModule
RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

+ (id)allocWithZone:(NSZone *)zone {
  static RCTAuroraIMUIModule *sharedInstance = nil;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    sharedInstance = [super allocWithZone:zone];
  });
  return sharedInstance;
}

- (id)init {
  self = [super init];
  return self;
}

RCT_EXPORT_METHOD(appendMessages:(NSArray *)messages) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kAppendMessages object: messages];
}

RCT_EXPORT_METHOD(deleteMessage:(NSArray *)messages) {
    [[NSNotificationCenter defaultCenter] postNotificationName:kDeleteMessage object: messages];
}
RCT_EXPORT_METHOD(cleanAllMessages) {
    [[NSNotificationCenter defaultCenter] postNotificationName:kCleanAllMessages object: nil];
}


RCT_EXPORT_METHOD(updateMessage:(NSDictionary *)message) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kUpdateMessge object: message];
}

RCT_EXPORT_METHOD(insertMessagesToTop:(NSArray *)messages) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kInsertMessagesToTop object: messages];
}

RCT_EXPORT_METHOD(scrollToBottom:(BOOL) animate) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kScrollToBottom object: @(animate)];
}

RCT_EXPORT_METHOD(hidenFeatureView:(BOOL) animate) {
  [[NSNotificationCenter defaultCenter] postNotificationName:kHidenFeatureView object: @(animate)];
}


RCT_EXPORT_METHOD(clickRecordLevel:(NSInteger )level) {
    [[NSNotificationCenter defaultCenter] postNotificationName:kRecordLevelNotification object: [NSString stringWithFormat:@"%zd",level]];
}

RCT_EXPORT_METHOD(clickRecordTime:(NSInteger )time) {
    [[NSNotificationCenter defaultCenter] postNotificationName:kRecordLongNotification object: [NSString stringWithFormat:@"%zd",time]];
}

RCT_EXPORT_METHOD(clickGetAtPerson:(NSDictionary *)person) {
    [[NSNotificationCenter defaultCenter] postNotificationName:GetAtPersonNotification object:person];
}

RCT_EXPORT_METHOD(clickLoadEmotionPages) {
//    [[NSNotificationCenter defaultCenter] postNotificationName:LoadPagesNotification object:nil];//已弃用
}


RCT_EXPORT_METHOD(tapVoiceBubbleView:(NSString *)messageID) {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"tapVoiceBubbleViewNotification" object:messageID];
}

RCT_EXPORT_METHOD(stopPlayVoice) {
    [[NSNotificationCenter defaultCenter] postNotificationName:kStopPlayVoice object: nil];
}

RCT_EXPORT_METHOD(stopPlayActivity) {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"StopPlayActivity" object: nil];
}

RCT_EXPORT_METHOD(clickScrollEnabled:(BOOL)isScroll) {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"clickScrollEnabled" object: [NSNumber numberWithBool:isScroll]];
}

//RCT_EXPORT_METHOD(showOrigImage:(NSString *)msgID) {
//    [[NSNotificationCenter defaultCenter] postNotificationName:kShowOrigImageNotification object: msgID];
//}

RCT_EXPORT_METHOD(showImages:(NSArray *)images currentIndex:(NSInteger)index){
    NSMutableArray *imagesArr = [self formatImages:images];
    dispatch_async(dispatch_get_main_queue(), ^{
        UIWindow *win = [UIApplication sharedApplication].keyWindow;
        UIViewController *rootVC = win.rootViewController;
        DWShowImageVC *vc = [[DWShowImageVC alloc]init];
        vc.modalPresentationStyle = UIModalPresentationOverCurrentContext;
        vc.imageArr = imagesArr;
        vc.index = index+1;
        [rootVC presentViewController:vc animated:NO completion:nil];
    });
}

- (NSMutableArray *)formatImages:(NSArray *) images {
    NSMutableArray *imagesArr = [NSMutableArray array];
    for (NSMutableDictionary *image in images) {
        //url   thumbPath   imageWidth  imageHeight   rect    msgId   displayName
        NSMutableDictionary *imageDic = [NSMutableDictionary dictionary];
        //NSString *thumbPath = [self findImagePath:[image objectForKey:@"thumbPath"]];
        [imageDic setObject:@"" forKey:@"thumbPath"];
        [imageDic setObject:image[@"uri"] forKey:@"url"];
        [imageDic setObject:@(SCREEN_W) forKey:@"imageWidth"];
        [imageDic setObject:@(SCREEN_H) forKey:@"imageHeight"];
        [imageDic setObject:NSStringFromCGRect(CGRectMake(SCREEN_W*0.5-50, SCREEN_H*0.5-50, 100, 100)) forKey:@"rect"];
        [imagesArr insertObject:imageDic atIndex:[images indexOfObject:image]];
    }
    return imagesArr;
}

//- (NSString*)findImagePath:(NSString *)imageURL{
//    if (imageURL) {
//        __block NSString *imagePath = nil;
//        dispatch_semaphore_t semap = dispatch_semaphore_create(0);
//        [[SDWebImageManager sharedManager] diskImageExistsForURL:[NSURL URLWithString:imageURL] completion:^(BOOL isInCache) {
//            if (isInCache) {
//                NSString *cacheImageKey = [[SDWebImageManager sharedManager] cacheKeyForURL:[NSURL URLWithString:imageURL]];
//                if (cacheImageKey.length) {
//                    imagePath = [[SDImageCache sharedImageCache] defaultCachePathForKey:cacheImageKey];
//                }
//            }
//            dispatch_semaphore_signal(semap);
//        }];
//        dispatch_semaphore_wait(semap, DISPATCH_TIME_FOREVER);
//        return imagePath;
//    }
//    else{
//        return nil;
//    }
//}

@end
