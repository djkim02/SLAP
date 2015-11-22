## *Sounds Like A Plan!*

### Introduction

Welcome to *Sounds Like A Plan*, or as we like to call it, SLAP. This quick start guide was written with a Mac user in mind, but the steps should be fairly platform agnostic.

### Required Downloads

> **Warning!  
> Android Studio utilizes a large amount of resources! For reasonable performance, run Android Studio on a computer with an SSD and at least 8GB RAM. Alternatively, testing on an actual Android Device will be easier and more efficient.**

1. [Android Studio GUI](https://developer.android.com/sdk/index.html)
2. [Clone our repository](https://github.com/djkim02/SLAP.git)

### Instructions

1. Upon successful installation, the set up window should appear labeled *Android Studio*.
2. Under *Quick Start*, choose open an existing Android Studio project.
3. Navigate to the SLAP folder that was cloned from the repository
4. Navigate one level down to the `/AndroidFiles` and open that as the existing project
5. The code will take a while to load and for Gradle to sync
6. Pay special attention to any warnings or errors. On the upper right hand corner, a *Configure* or *Sync* dialog box might appear. Sync or configure if necessary.

#### If Testing on Device

1. Connect your device via USB port
2. After opening the project, look for the *Android* tab in the file hierarchy's toolbar. This will show all the Android fields.
3. Navigate to Gradle Scripts > build.gradle (Module: app). Here, put `debuggable true` under `buildTypes`:
```
android {
    buildTypes {
        debug {
            debuggable true // Put this right under buildTypes.
        }
```
4. Navigate to `app/manifests/AndroidManifest.xml` right above Gradle Scripts
5. Add `android:debuggable="true"` under the `<application>` tag on its own line
6. Enable Debugging on your device under Settings > About phone and tap Build number seven times very quickly
7. Return to the previous screen and a new option *Developer options* will be enabled. Enable this option by clicking on it.
8. Plug in your device and run the program by pressing the green play button or `^R` command


#### If Testing on Emulator

1. Open the AVD Manager (the fourth last icon in the toolbar). You can hover over the icons to see what each icon stands for.
2. Create a virtual device on the bottom left
3. Choose a phone as the category, e.g. Nexus 5. Click *Next*.
4. For System Image, choose: Lollipop, API Level 22, ABI x86, Target Android 5.1.1. Change your AVD name to be `<Your Name>`'s Phone and select Finish. Leave the default settings or change it if you wish.
5. Your emulator is now set!
6. Go back to the AVD Manager and press the green play button
7. Wait a few moments for the emulator to show up. This might slow down your computer.
8. Once your emulator is finished and your able to interact with it, you can run the code and our app will play automatically.

### Test Plan

#### Layout:

1. Open the application to check out the UI, swipe right to see more activities.
2. Login using your Facebook credentials
3. The menu tab is located in the upper left of the action bar

#### Joining Groups:

Our matching algorithm finds groups that correspond to your interests as specified in your profile. If you have similar skills to any of the groups created in our backend, you will see the groups show up in the list card views. We have some pre-defined groups for Football and Basketball.

1. Tap *Find Matches* in the overflow menu
2. If you see a group you wish to join, you can directly tap on `N slots remaining`. Otherwise, you can go into the group through `Details` to find out more about the group before joining it.
3. On tapping to join a group, a Facebook confirmation window will appear to add you to the group.
4. Back on the Facebook app, an invite will have been sent to you.
5. The default screen on our app will now show that you are part of the group.

#### Creating Groups:

1. Tap *Create a Group*
2. Specify if it is going to be a Hacker group or an Athlete (recreational sport) group.
3. Enter a group name
4. Provide a description
5. Specify the group size
6. Select the matching criteria of skills you want in your group
7. Enter any tags you want to make a search easier to locate your group (This can be left empty)
8. Confirm your submission
9. The Facebook API will be called to confirm the group creation with you as the Admin of the group
10. Once the group is create you may go to Facebook to manage it


#### Editing Profile:

1. To edit your profile if your skills have changed go to *My Profile* and tap on *Edit Profile*
2. My Hacker Skills will display your Hacker Skills
3. My Athlete Skills will display your Athletic Skills

#### My Groups:

Tapping *My Groups* will bring you back to the home screen.
