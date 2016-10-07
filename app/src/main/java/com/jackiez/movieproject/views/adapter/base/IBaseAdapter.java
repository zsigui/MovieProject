package com.jackiez.movieproject.views.adapter.base;

import java.util.List;

public interface IBaseAdapter<Data> {

    int TAG_POSITION = 0x1234478F;

    void updateData(List<Data> newData);

    void addMoreData(List<Data> moreData);

    void onDestroy();
}