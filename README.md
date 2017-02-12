# min-time-dialog
Progress dialog that stay on the screen for specified minimum amount of time.
This library offers simple widget `MinTimeDialog` that extends standard ProgressDialog but offers some additional features:
* You can set up minimum showing time. Even if dialog is dimissed it will stay on screen for that time and dismiss litener will be fired after specified amount of time
* Silent dimiss. Dismissing dialog without firing dimiss listener
* `dismissForced` offers force dismiss ignoring minimum time specified

Download
---------

Sample Usage
---------

Creating simple progress dialog with just a spinner and message that will stay on screen for 2 seconds
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
  dialog.show();
  dialog.dismissForced();
```

`MinTimeDialog` extends `ProgressDialog` so all styling options are available as well
