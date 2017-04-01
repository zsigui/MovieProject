package com.jackiez.zgithub.model.net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by zsigui on 17-3-21.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class RespUser implements Parcelable{

    public String login;

    public String id;

    public String avatar_url;

    public String gravatar_id;

    public String url;

    public String html_url;

    public String followers_url;

    public String following_url;

    public String gists_url;

    public String starred_url;

    public String subscriptions_url;

    public String organizations_url;

    public String repos_url;

    public String events_url;

    public String received_events_url;

    public String type;

    public String site_admin;

    public String name;

    public String company;

    public String blog;

    public String location;

    public String email;

    public boolean hireable;

    public String bio;

    public int public_repos;

    public int public_gists;

    public int followers;

    public int following;

    // 时间返回格式如: "2013-08-23T09:14:48Z"
    public String created_at;

    public String updated_at;

    // 以下登录后获取

    public int total_private_repos;

    public int owned_private_repos;

    public int private_gists;

    public int disk_usage;

    public int collaborators;

    public RespUserPlan plan;

    public RespUser() {}

    protected RespUser(Parcel in) {
        login = in.readString();
        id = in.readString();
        avatar_url = in.readString();
        gravatar_id = in.readString();
        url = in.readString();
        html_url = in.readString();
        followers_url = in.readString();
        following_url = in.readString();
        gists_url = in.readString();
        starred_url = in.readString();
        subscriptions_url = in.readString();
        organizations_url = in.readString();
        repos_url = in.readString();
        events_url = in.readString();
        received_events_url = in.readString();
        type = in.readString();
        site_admin = in.readString();
        name = in.readString();
        company = in.readString();
        blog = in.readString();
        location = in.readString();
        email = in.readString();
        hireable = in.readByte() != 0;
        bio = in.readString();
        public_repos = in.readInt();
        public_gists = in.readInt();
        followers = in.readInt();
        following = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        total_private_repos = in.readInt();
        owned_private_repos = in.readInt();
        private_gists = in.readInt();
        disk_usage = in.readInt();
        collaborators = in.readInt();
        plan = in.readParcelable(RespUserPlan.class.getClassLoader());
    }

    public static final Creator<RespUser> CREATOR = new Creator<RespUser>() {
        @Override
        public RespUser createFromParcel(Parcel in) {
            return new RespUser(in);
        }

        @Override
        public RespUser[] newArray(int size) {
            return new RespUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(id);
        dest.writeString(avatar_url);
        dest.writeString(gravatar_id);
        dest.writeString(url);
        dest.writeString(html_url);
        dest.writeString(followers_url);
        dest.writeString(following_url);
        dest.writeString(gists_url);
        dest.writeString(starred_url);
        dest.writeString(subscriptions_url);
        dest.writeString(organizations_url);
        dest.writeString(repos_url);
        dest.writeString(events_url);
        dest.writeString(received_events_url);
        dest.writeString(type);
        dest.writeString(site_admin);
        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(blog);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeByte((byte) (hireable ? 1 : 0));
        dest.writeString(bio);
        dest.writeInt(public_repos);
        dest.writeInt(public_gists);
        dest.writeInt(followers);
        dest.writeInt(following);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeInt(total_private_repos);
        dest.writeInt(owned_private_repos);
        dest.writeInt(private_gists);
        dest.writeInt(disk_usage);
        dest.writeInt(collaborators);
        dest.writeParcelable(plan, flags);
    }
}
