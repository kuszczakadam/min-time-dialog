# min-time-dialog
Progress dialog that stays on the screen for specified minimum amount of time.
Developer can specify minimum showing time in milliseconds as well as couple other useful options.
All stylings are available via standard `ProgressDialog` interface.

This library offers simple widget `MinTimeDialog` that extends standard `ProgressDialog` but offers some additional features:
* You can set up minimum showing time. Even if dialog is dismissed it will stay on screen for that time and dismiss
listener will be fired after specified amount of time.
* Silent dismiss, dismissing dialog without firing dismiss listener
* Force dismiss, ignoring minimum time specified
* Auto dismiss when minimum showing time is reached
* Callback when minimum showing time is reached
* Possibility to extend minimum showing time while dialog is in progress (update UI via eg `setMessage()`)
* Debug logger interface

Download
---------
Gradle configuration
```groovy
repositories{
    maven {
        url "https://dl.bintray.com/kuszczakadam/MinTimeDialog"
    }
}
dependencies {
    compile 'eu.tesseractsoft.mintimedialog:mintimedialog:0.1.1'
}
```
Sample Usage
---------

Creating simple progress dialog with just a spinner and message that will stay on screen for 2 seconds
and then automatically close itself
```java
  MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Simple processing", 2000);
  dialog.show();
  dialog.dismiss();
```

Options
---------
The code below shows all available options with default values
```java
    MinTimeDialog dialog = new MinTimeDialog(this);
    dialog.setMinShownTimeMs(0);//in millisec
    dialog.setSilentDismiss(false);
    dialog.setAutoDismissAfterMinShownTime(false);
    dialog.setMinTimeReachedListener(null);
    dialog.setDebugLogger(null);
    dialog.setOnDismissListener(null);

    boolean flag = dialog.isMinTimeReached();

    dialog.show();
    dialog.dismiss();
    dialog.dismissForced();
    dialog.extendMinShownTimeByMs(0);//in milisec
```

`MinTimeDialog` extends `ProgressDialog` so all styling options are available as well

Advanced notes
---------

Some more advanced topics and notes

1. Extending minimum showing time

    Extending minimum showing time is additive. It means that calling `extendMinShownTimeByMs()` multiple times
    will add more minimum showing time. Below code will result in dialog being shown for 4 seconds
    ```java
        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Initial 2s...", 2000);
        dialog.setAutoDismissAfterMinShownTime(true);
        dialog.show();
        dialog.extendMinShownTimeByMs(1000);
        dialog.extendMinShownTimeByMs(1000);
    ```
    Extending by negative number has no effect (`extendMinShownTimeByMs(-1000)`).
    The `extendMinShownTimeByMs()` method can be called before or after `show()` as well as inside
    `MinTimeReachedListener`. Calling after `dismiss()` but before min time was reached will also result
    in extending time. However, calling after min time was reached (and not inside `MinTimeReachedListener`)
    will NOT extend time (in other words, `extendMinShownTimeByMs()` method does NOT schedule timer again).
    Calling `extendMinShownTimeByMs()` method before `show()` method has no effect

2. Extending min shown time before it was reached

    The code below shows how to extend minimum showing time before initial timeout was reached
    ```java
        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Initial 2s...", 2000);
        dialog.setAutoDismissAfterMinShownTime(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              dialog.setMessage("Extending by 1s");
              dialog.extendMinShownTimeByMs(1000);
          }
        }, 1000);
    ```
3. Extending min shown time once it was reached

    The code below shows how to extend time when min time was reached.
    It is IMPORTANT to check total time otherwise we will be extending min showing time indefinitely
    ```java
        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Initial 2s...", 2000);
        dialog.setAutoDismissAfterMinShownTime(true);
        dialog.setMinTimeReachedListener(new MinTimeDialog.MinTimeReachedListener() {
            @Override
            public void onMinTimeReached(long totalMinShownTime) {
                if(totalMinShownTime < 2500){
                    dialog.setMessage("Extending by 1s");
                    dialog.extendMinShownTimeByMs(1000);
                }
            }
        });
        dialog.show();
    ```
4. 3-step processing

    The code below shows how to setup dialog to notify user about 3-step processing.
    It shows first message for 1 second, then updates message and extend time by 2 seconds, and
    finally shows last message for 1 second. In total, dialog is shown for 4 seconds
    ```java
        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Connecting...", 1000);
        dialog.setAutoDismissAfterMinShownTime(true);
        dialog.setMinTimeReachedListener(new MinTimeDialog.MinTimeReachedListener() {
            @Override
            public void onMinTimeReached(long totalMinShownTime) {
                if (totalMinShownTime < 1200) {//1.2 sec
                    dialog.setMessage("Processing...");
                    dialog.extendMinShownTimeByMs(2000);
                } else if (totalMinShownTime < 3200) {//3.2 sec
                    dialog.setMessage("Disconnecting...");
                    dialog.extendMinShownTimeByMs(1000);
                }
            }
        });
        dialog.show();
    ```

