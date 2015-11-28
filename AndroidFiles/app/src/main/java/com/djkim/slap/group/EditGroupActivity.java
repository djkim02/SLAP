/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.djkim.slap.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.djkim.slap.R;
import com.djkim.slap.menubar.MainActivity;
import com.djkim.slap.models.Group;
import com.djkim.slap.models.GroupCallback;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;
import com.djkim.slap.selectionModel.EnterTextPage;
import com.djkim.slap.selectionModel.ModelCallbacks;
import com.djkim.slap.selectionModel.MultipleFixedChoicePage;
import com.djkim.slap.selectionModel.Page;
import com.djkim.slap.selectionModel.PageFragmentCallbacks;
import com.djkim.slap.selectionModel.PageList;
import com.djkim.slap.selectionModel.ReviewFragment;
import com.djkim.slap.selectionModel.ReviewItem;
import com.djkim.slap.selectionModel.SingleFixedChoicePage;
import com.djkim.slap.selectionModel.StepPagerStrip;
import com.djkim.slap.selectionModel.AbstractWizardModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppGroupCreationContent;
import com.facebook.share.widget.CreateAppGroupDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditGroupActivity extends ActionBarActivity implements
        PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks {
    public static final String EDIT_GROUP_EXTRA = "edit_group_extra";

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private AbstractWizardModel mWizardModel;

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    public Toolbar toolbar;
    CreateAppGroupDialog createAppGroupDialog;
    CallbackManager callbackManager;
    private Group group;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_layout);

        group = (Group) getIntent().getExtras().getSerializable(EDIT_GROUP_EXTRA);
        mWizardModel = createWizardModel(group);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        createAppGroupDialog = new CreateAppGroupDialog(this);
        createAppGroupDialog.registerCallback(
                callbackManager, new FacebookCallback<CreateAppGroupDialog.Result>() {
            public void onSuccess(CreateAppGroupDialog.Result result) {
                String id = result.getId();
                group.set_facebookGroupId(id);
                group.saveInBackground(new GroupCallback() {
                    @Override
                    public void done() {
                        Toast.makeText(EditGroupActivity.this, "Successfully created the group!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.putExtra(EDIT_GROUP_EXTRA, group);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }

            public void onCancel() {
                group.saveInBackground(new GroupCallback() {
                    @Override
                    public void done() {
                        Toast.makeText(EditGroupActivity.this, "Failed to create the group!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.putExtra(EDIT_GROUP_EXTRA, group);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }

            public void onError(FacebookException error) {
            }
        });

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });
        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    DialogFragment dg = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            return new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.submit_confirm_message)
                                    .setPositiveButton(R.string.submit_confirm_button, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            createGroup();
                                            if (group.get_facebookGroupId() == null) {
                                                onClickCreateButton();
                                            } else {
                                                group.saveInBackground(new GroupCallback() {
                                                    @Override
                                                    public void done() {
                                                        Intent intent = new Intent();
                                                        intent.putExtra(EDIT_GROUP_EXTRA, group);
                                                        setResult(RESULT_OK, intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .create();
                        }
                    };
                    dg.show(getSupportFragmentManager(), "place_order_dialog");
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == 0) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                }
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void createGroup() {
        ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (Page page : mWizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        // reviewItems:
        // 0: Name, 1: Description, 2: Capacity, 3: Skills, 4: Custom Tags
        // TODO: support custom tags
        String name = reviewItems.get(0).getDisplayValue();
        String description = reviewItems.get(1).getDisplayValue();
        int capacity = Integer.parseInt(reviewItems.get(2).getDisplayValue());
        String skills = reviewItems.get(3).getDisplayValue();
        String tags = reviewItems.get(4).getDisplayValue();
        group.set_name(name);
        group.set_capacity(capacity);
        group.set_description(description);
        group.set_skills(skills);
        group.set_tags(tags == null ? "" : tags);
    }

    private AbstractWizardModel createWizardModel(final Group group) {
        return new AbstractWizardModel(this) {
            @Override
            protected PageList onNewRootPageList() {
                ArrayList<String> currentSelections =
                        new ArrayList<>(Arrays.asList(group.get_skills().split(" ,")));

                // TODO(victorkwan): Maybe we can refactor this to use the Abstract Factory pattern.
                // Also, we might be better off defining shared constants for the setChoices.
                if (group.get_type().equals(Group.ATHLETE_GROUP)) {
                    return new PageList(
                            new EnterTextPage(this, "What is the group name?")
                                    .setValue(group.get_name())
                                    .setRequired(true),
                            new EnterTextPage(this, "Please give the group a short description")
                                    .setValue(group.get_description())
                                    .setRequired(true),
                            new SingleFixedChoicePage(this, "What is the size of the group?")
                                    .setValue(Integer.toString(group.get_capacity()))
                                    .setChoices("2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
                                            "12", "13", "14", "15")
                                    .setRequired(true),
                            new MultipleFixedChoicePage(this, "What sports are desired?")
                                    .setValues(currentSelections)
                                    .setChoices("Running", "Soccer", "Basketball", "Baseball",
                                            "Football", "Weight Training", "Frisbee", "Biking",
                                            "Bowling", "Badminton", "Ping Pong", "Cricket", "Golf",
                                            "Handball", "Yoga", "Boxing"),
                            new EnterTextPage(this,
                                    "Please specify any tags to be associated with the group")
                                    .setValue(group.get_tags()));
                } else {
                    return new PageList(
                            new EnterTextPage(this, "What is the group name?")
                                    .setValue(group.get_name())
                                    .setRequired(true),
                            new EnterTextPage(this, "Please give the group a short description")
                                    .setValue(group.get_description())
                                    .setRequired(true),
                            new SingleFixedChoicePage(this, "What is the size of the group?")
                                    .setValue(Integer.toString(group.get_capacity()))
                                    .setChoices("2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
                                            "12", "13", "14", "15")
                                    .setRequired(true),
                            new MultipleFixedChoicePage(this, "What skills are desired?")
                                    .setValues(currentSelections)
                                    .setChoices("Android Development", "iOS Development",
                                            "Web Development", "Front-end Development",
                                            "Back-end Development", "Java", "C++", "C", "C#",
                                            "Python", "PHP", "HTML", "CSS", "JavaScript", "Node.js",
                                            "AngularJS", "Ruby", "Rails", "Coffeescript", "MongoDB",
                                            "MySQL", "PostgreSQL", ".NET", "Git", "Linux",
                                            "Photoshop", "Illustrator"),
                            new EnterTextPage(this,
                                    "Please specify any tags to be associated with the group")
                                    .setValue(group.get_tags()));
                }
            }
        };
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == 0) {
            mPrevButton.setText("Cancel");
        } else {
            mPrevButton.setText("Previous");
        }

        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            //mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview
                    ? R.string.review
                    : R.string.next);
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            //mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position < 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }

    private void onClickCreateButton() {
        AppGroupCreationContent content = new AppGroupCreationContent.Builder()
                .setName(group.get_name() == null ? "Enter a group name" : group.get_name())
                .setDescription(group.get_description() == null
                        ? "Enter a description" : group.get_description())
                .setAppGroupPrivacy(AppGroupCreationContent.AppGroupPrivacy.Closed)
                .build();
        createAppGroupDialog.show(content);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
