package com.jackiez.zgithub.model.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by zsigui on 17-3-22.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespUserPlan implements Parcelable {

    public String name;

    public int space;

    public int private_repos;

    public int collaborators;

    public RespUserPlan() {
    }

    protected RespUserPlan(Parcel in) {
        name = in.readString();
        space = in.readInt();
        private_repos = in.readInt();
        collaborators = in.readInt();
    }

    public static final Creator<RespUserPlan> CREATOR = new Creator<RespUserPlan>() {
        @Override
        public RespUserPlan createFromParcel(Parcel in) {
            return new RespUserPlan(in);
        }

        @Override
        public RespUserPlan[] newArray(int size) {
            return new RespUserPlan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(space);
        dest.writeInt(private_repos);
        dest.writeInt(collaborators);
    }
}
