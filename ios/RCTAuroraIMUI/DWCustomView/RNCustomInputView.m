//
//  RNCustomInputView.m
//  RNCustomInputView
//
//  Created by Dowin on 2017/7/10.
//  Copyright © 2017年 Dowin. All rights reserved.
//

#import "RNCustomInputView.h"
#import "DWInputBarControl.h"
#import "UIView+Extend.h"


@interface RNCustomInputView ()<DWInputBarControlDelegate>{
    DWInputBarControl *inpuntBar;
}

@end

@implementation RNCustomInputView


RCT_EXPORT_MODULE()

RCT_EXPORT_VIEW_PROPERTY(menuViewH, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(defaultToolHeight, CGFloat);
RCT_EXPORT_VIEW_PROPERTY(onFeatureView, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onShowKeyboard, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeBarHeight, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSendTextMessage, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSendRecordMessage, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onClickMention, RCTBubblingEventBlock)


- (UIView *)view{
    inpuntBar = [[DWInputBarControl alloc]init];
    inpuntBar.delegate = self;
    return inpuntBar;
}

//显示menuView
- (void)changeMenuView{
    int showType = 0;
    if (inpuntBar.showRecordeBtn.selected) {
        inpuntBar.showRecordeBtn.selected = NO;
        inpuntBar.recordBtn.hidden = YES;
        inpuntBar.inputView.hidden = NO;
        inpuntBar.toolH = inpuntBar.inputViewHeight;
    }
    inpuntBar.showMenuBtn.selected = !inpuntBar.showMenuBtn.selected;
    inpuntBar.showExpressionBtn.selected = NO;
    if (inpuntBar.showMenuBtn.selected) {
        [inpuntBar.inputGrowView endEditing:YES];
        inpuntBar.expressionView.hidden = YES;
        CGFloat inputH = inpuntBar.inputViewHeight + inpuntBar.menuViewH;
        inpuntBar.height = inputH;
        showType = 1;
    }else{
        inpuntBar.height = inpuntBar.inputViewHeight;
        [inpuntBar.inputGrowView becomeFirstResponder];
        showType = 0;
    }
    if(!inpuntBar.onFeatureView) { return; }
    inpuntBar.onFeatureView(@{@"inputHeight":@(inpuntBar.height),@"showType":@(showType)});
}
//显示ExpressionView
- (void)changExpressionView{
    int showType = 0;
    [inpuntBar.inputGrowView endEditing:YES];
    if (inpuntBar.showRecordeBtn.selected) {
        inpuntBar.showRecordeBtn.selected = NO;
        inpuntBar.recordBtn.hidden = YES;
        inpuntBar.inputView.hidden = NO;
        inpuntBar.toolH = inpuntBar.inputViewHeight;
    }
    inpuntBar.showExpressionBtn.selected = !inpuntBar.showExpressionBtn.selected;
    inpuntBar.showMenuBtn.selected = NO;
    if (inpuntBar.showExpressionBtn.selected) {
        [inpuntBar.inputGrowView endEditing:YES];
        inpuntBar.expressionView.hidden = NO;
        CGFloat inputH = inpuntBar.inputViewHeight + expressionViewH;
        inpuntBar.height = inputH;

    }else{
        inpuntBar.expressionView.hidden = YES;
        inpuntBar.height = inpuntBar.inputViewHeight;
        [inpuntBar.inputGrowView becomeFirstResponder];
    }
    if(!inpuntBar.onFeatureView) { return; }
    inpuntBar.onFeatureView(@{@"inputHeight":@(inpuntBar.height),@"showType":@(showType)});
}

- (void)changeRecordView{
    inpuntBar.showRecordeBtn.selected = !inpuntBar.showRecordeBtn.selected;
    inpuntBar.showMenuBtn.selected = NO;
    inpuntBar.showExpressionBtn.selected = NO;
    inpuntBar.expressionView.hidden = YES;
    if (inpuntBar.showRecordeBtn.selected) {
        [inpuntBar.inputGrowView endEditing:YES];
        inpuntBar.height = inpuntBar.defaultToolHeight;
        inpuntBar.recordBtn.hidden = NO;
        inpuntBar.inputGrowView.hidden = YES;
        inpuntBar.toolH = inpuntBar.defaultToolHeight;
    }else{
        inpuntBar.recordBtn.hidden = YES;
        inpuntBar.inputGrowView.hidden = NO;
        inpuntBar.toolH = inpuntBar.inputViewHeight;
        inpuntBar.height = inpuntBar.inputViewHeight;
        [inpuntBar.inputGrowView becomeFirstResponder];
    }
    if(!inpuntBar.onFeatureView) { return; }
    inpuntBar.onFeatureView(@{@"inputHeight":@(inpuntBar.height),@"showType":@(0)});
}

#pragma mark -- DWIputBarDelegate
//点击按钮
- (void)inputBarClickBtn:(UIButton *)btn{
    NSLog(@"%zd",btn.tag);
    switch (btn.tag) {
        case DWInputBarControlBtnTypeRecord:
        {
            [self changeRecordView];
        }
            break;
        case DWInputBarControlBtnTypeMenu:
        {
            inpuntBar.inputGrowView.hidden = NO;
            [self changeMenuView];
        }
            break;
        case DWInputBarControlBtnTypeExpression:
        {
            inpuntBar.inputGrowView.hidden = NO;
            [self changExpressionView];
        }
            break;
            
        default:
            break;
    }
}




@end