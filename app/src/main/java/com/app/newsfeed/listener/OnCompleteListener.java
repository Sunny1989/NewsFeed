package com.app.newsfeed.listener;

import java.util.HashMap;
import java.util.List;

public interface OnCompleteListener {
    void onComplete(List<String> expandableTitleList, HashMap<String, List<String>> expandableListData);
}
