package com.jackiez.zgithub.test.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.jackiez.zgithub.BR;
import com.jackiez.zgithub.test.data.Contributor;

import java.util.List;

/**
 * Created by zsigui on 17-3-28.
 */

public class TestItemRepoVM extends BaseObservable {

    private String name;

    private String language;

    private String meta;

    private String des;

    private String href;

    private String link;

    private List<Contributor> contributors;

    private String owner;

    private boolean isStart;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        notifyPropertyChanged(BR.language);
    }

    @Bindable
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
        notifyPropertyChanged(BR.meta);
    }

    @Bindable
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
        notifyPropertyChanged(BR.des);
    }

    @Bindable
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
        notifyPropertyChanged(BR.href);
    }

    @Bindable
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        notifyPropertyChanged(BR.link);
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    @Bindable
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        notifyPropertyChanged(BR.owner);
    }

    @Bindable
    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
        notifyPropertyChanged(BR.start);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj instanceof TestItemRepoVM) {
            TestItemRepoVM other = (TestItemRepoVM) obj;
            if (name != null && name.equals(other.name)
                    && owner != null && owner.equals(other.owner))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return owner + " / " + name;
    }
}
