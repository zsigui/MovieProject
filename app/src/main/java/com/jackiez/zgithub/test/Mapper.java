package com.jackiez.zgithub.test;

import com.jackiez.zgithub.test.data.Repo;
import com.jackiez.zgithub.test.data.User;
import com.jackiez.zgithub.test.vm.TestActivityVM;
import com.jackiez.zgithub.test.vm.TestItemRepoVM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsigui on 17-3-23.
 */

public class Mapper {

    public static void mapperUserToTestVM(User from, TestActivityVM to) {
        if (from == null || to == null)
            return;
        to.setName(from.getName());
        to.setPassword(from.getPassword());
    }

    public static List<TestItemRepoVM> mapperReposToItemRepoVM(List<Repo> data) {
        if (data == null || data.isEmpty())
            return null;
        List<TestItemRepoVM> result = new ArrayList<>(data.size());
        int size = data.size();
        TestItemRepoVM item;
        Repo r;
        for (int i = 0; i < size; i++) {
            item = new TestItemRepoVM();
            r = data.get(i);
            if (r == null)
                continue;
            item.setName(r.name);
            item.setContributors(r.contributors);
            item.setDes(r.des);
            item.setHref(r.href);
            item.setLanguage(r.language);
            item.setLink(r.link);
            item.setMeta(r.meta);
            item.setOwner(r.owner);
            item.setStart(((int) (Math.random() * 2)) == 0);
            result.add(item);
        }
        return result;
    }
}
