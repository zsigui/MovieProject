package com.jackiez.zgithub.view.fragment;

import android.os.Bundle;

import com.jackiez.zgithub.R;
import com.jackiez.zgithub.vm.fragment.ContentVM;

/**
 * Created by zsigui on 17-3-24.
 */
public class ContentFragment extends DefaultBaseFragment<ContentVM>{


    public static ContentFragment newInstance(String type) {
        ContentFragment c = new ContentFragment();
        Bundle b = new Bundle();
        b.putString("type", type);
        c.setArguments(b);
        return c;
    }

    @Override
    protected ContentVM createVM() {
        return new ContentVM(this);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_base_default;
    }
}
