package com.songdehuai.commonlib.widget.myrefreshlayout.processor;


import com.songdehuai.commonlib.widget.myrefreshlayout.MyRefreshLayout;

/**
 * 描述：
 *
 * @author songdehuai
 * @ClassName: com.songdehuai.myrefreshlayout.widget.refresh.processor.Decorator
 * @date 2017/8/14 11:12
 */
public abstract class Decorator implements IDecorator {
    protected IDecorator decorator;
    protected MyRefreshLayout.CoContext cp;

    public Decorator(MyRefreshLayout.CoContext processor, IDecorator decorator1) {
        cp = processor;
        decorator = decorator1;
    }
}
