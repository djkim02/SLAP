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

package com.djkim.slap.createGroup;

import android.content.Context;

import com.djkim.slap.selectionModel.AbstractWizardModel;
import com.djkim.slap.selectionModel.BranchPage;
import com.djkim.slap.selectionModel.EnterTextPage;
import com.djkim.slap.selectionModel.MultipleFixedChoicePage;
import com.djkim.slap.selectionModel.PageList;
import com.djkim.slap.selectionModel.SingleFixedChoicePage;

import java.util.ArrayList;
import java.util.List;

public class SandwichWizardModel extends AbstractWizardModel {
    public SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(

                // BranchPage shows all of the branches available: Branch One, Branch Two, Branch Three. Each of these branches
                // have their own questions and the choices of the user will be summarised in the review section at the end
                new BranchPage(this, "Athlete or Hacker?")
                        .addBranch("Athlete",
                                new EnterTextPage(this, "What is the group name?").setRequired(true),
                                new EnterTextPage(this, "Please give the group a short description").setRequired(true),
                                new SingleFixedChoicePage(this, "What is the size of the group?")
                                        .setChoices("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
                                        .setRequired(true),
                                new MultipleFixedChoicePage(this, "What sports are desired?")
                                        .setChoices("Running", "Soccer", "Basketball", "Baseball", "Football",
                                                "Weight Training", "Frisbee", "Biking", "Bowling", "Badminton",
                                                "Ping Pong", "Cricket", "Golf", "Handball", "Yoga", "Boxing"),
                                new EnterTextPage(this, "Please specify any tags to be associated with the group (separate with commas)")
                        )

                                // Second branch of questions
                        .addBranch("Hacker",
                                new EnterTextPage(this, "What is the group name?").setRequired(true),
                                new EnterTextPage(this, "Please give the group a short description").setRequired(true),
                                new SingleFixedChoicePage(this, "What is the size of the group?")
                                        .setChoices("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
                                        .setRequired(true),
                                new MultipleFixedChoicePage(this, "What skills are desired?")
                                        .setChoices("Android Development", "iOS Development", "Web Development",
                                                "Front-end Development", "Back-end Development", "Java",
                                                "C++", "C", "C#", "Python", "PHP", "HTML", "CSS", "JavaScript",
                                                "Node.js", "AngularJS", "Ruby", "Rails", "Coffeescript",
                                                "MongoDB", "MySQL", "PostgreSQL", ".NET", "Git", "Linux",
                                                "Photoshop", "Illustrator"),
                                new EnterTextPage(this, "Please specify any tags to be associated with the group (separate with commas)")
                        )

        );
    }
}
