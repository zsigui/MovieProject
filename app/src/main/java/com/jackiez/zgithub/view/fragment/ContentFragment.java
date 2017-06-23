package com.jackiez.zgithub.view.fragment;

import android.os.Bundle;

import com.jackiez.zgithub.R;
import com.jackiez.zgithub.vm.fragment.FContentVM;

/**
 * Created by zsigui on 17-3-24.
 */
public class ContentFragment extends DefaultBaseFragment<FContentVM>{


    public static ContentFragment newInstance(String type) {
        ContentFragment c = new ContentFragment();
        Bundle b = new Bundle();
        b.putString("type", type);
        c.setArguments(b);
        return c;
    }

    @Override
    protected FContentVM createVM() {
        return new FContentVM(this);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_base_default;
    }
}
