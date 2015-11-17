package com.djkim.slap.match;

import android.content.Context;

import com.djkim.slap.selectionModel.AbstractWizardModel;
import com.djkim.slap.selectionModel.BranchPage;
import com.djkim.slap.selectionModel.EnterTextPage;
import com.djkim.slap.selectionModel.MultipleFixedChoicePage;
import com.djkim.slap.selectionModel.PageList;
import com.djkim.slap.selectionModel.SingleFixedChoicePage;

/**
 * Created by YooJung on 11/15/2015.
 */
public class MatchGroupWizardModel extends AbstractWizardModel {
    public MatchGroupWizardModel(Context context) { super(context); };

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new SingleFixedChoicePage(this, "What type of group are you looking for?")
                        .setChoices("Athlete", "Hacker")
                        .setRequired(true),
                new EnterTextPage(this, "Please specify any tags you want"));
    }
}

