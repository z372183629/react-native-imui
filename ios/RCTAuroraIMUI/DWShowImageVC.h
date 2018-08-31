//
//  DWShowImageVC.h
//  RCTAuroraIMUI
//
//  Created by Dowin on 2017/9/11.
//  Copyright © 2017年 HXHG. All rights reserved.
//

#import <UIKit/UIKit.h>

#define onImageViewerDeleteImage @"onImageViewerDeleteImage"
#define ImageViewerDeleteImage @"ImageViewerDeleteImage"

@interface DWShowImageVC : UIViewController
@property (copy, nonatomic) NSMutableArray *imageArr;
@property (assign, nonatomic) NSInteger index;
@property (strong, nonatomic) UIImage *backgroundImg;
@property (assign, nonatomic) BOOL showDeleteBtn;

@end
