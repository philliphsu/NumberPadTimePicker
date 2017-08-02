# 1.1.1 (TBD)
### Changes
* The following [`NumberPadTimePicker`] APIs are no longer public: `setNumberKeysEnabled()`, 
`setBackspaceEnabled()`, `updateTimeDisplay()`, `updateAmPmDisplay()`, `setAmPmDisplayVisible()`,
`setAmPmDisplayIndex()`, `setLeftAltKeyText()`, `setRightAltKeyText()`, `setLeftAltKeyEnabled()`, 
`setRightAltKeyEnabled()`, `setOkButtonEnabled()`, `setResult()`, `showOkButton()`. These are 
implementation details and you should not have called them.
* Reduced library size by about 2 KB.

# 1.1.0 (2017-07-30)
### New Features and Changes
* Use [`NumberPadTimePicker`] as a `View` in your own layouts.
* New [`NumberPadTimePicker`] APIs:
    * `OkButtonCallbacks` interface allows you to use a custom, 
    externally-defined "OK" button with the time picker. It is also how you will retrieve the 
    selected time if you are using [`NumberPadTimePicker`] as a `View`. Set with `setOkButtonCallbacks()`.
    * `confirmTimeSelection()` calls your `OkButtonCallbacks.onOkButtonClick()` and returns the 
    selected time.
    * `getLayout()` returns a constant representing the layout, which determines the presence and 
    appearance of dialog buttons. Return values: `LAYOUT_STANDALONE`, `LAYOUT_ALERT`, 
    `LAYOUT_BOTTOM_SHEET`.
    * `setCancelButtonClickListener()` sets a `View.OnClickListener` on the time picker's cancel 
    button, which is present only in `LAYOUT_ALERT`.
    * `getThemer()` returns a [`NumberPadTimePickerThemer`] that can be casted to 
    [`BottomSheetNumberPadTimePickerThemer`] if the layout is `LAYOUT_BOTTOM_SHEET`.
* [`NumberPadTimePickerThemer`] and [`BottomSheetNumberPadTimePickerThemer`] are now public.
These are the super classes of [`NumberPadTimePickerDialogThemer`] and 
[`BottomSheetNumberPadTimePickerDialogThemer`], respectively.
* New styleable attributes: 
    * `nptp_numberPadTimePickerLayout` determines the presence and appearance of additional dialog 
    buttons. Possible values: `standalone`, `alert`, `bottomSheet`.
    * `nptp_is24HourMode` indicates whether the number pad should use 24-hour mode.
* New style resources:
    * `NPTP_Base_NumberPadTimePickerStandaloneStyle`
    * `NPTP_Base_NumberPadTimePickerAlertStyle`
    * `NPTP_Base_NumberPadTimePickerBottomSheetStyle`
    * `NPTP_NumberPadTimePickerStandaloneStyle_FillHeight`
    * `NPTP_NumberPadTimePickerStandaloneStyle_ExactHeight`
    * `NPTP_NumberPadTimePickerAlertStyle_FillHeight`
    * `NPTP_NumberPadTimePickerAlertStyle_ExactHeight`
    * `NPTP_NumberPadTimePickerBottomSheetStyle_FillHeight`
    * `NPTP_NumberPadTimePickerBottomSheetStyle_ExactHeight`
* Deprecated style resources: 
    * `NPTP_NumberPadTimePickerStyle`. Use `NPTP_Base_NumberPadTimePickerAlertStyle` instead.
    * `NPTP_NumberPadTimePickerBottomSheetStyle`. Use `NPTP_Base_NumberPadTimePickerBottomSheetStyle` 
    instead.
* Updated sample app to demo use of [`NumberPadTimePicker`] as a `View`. 

### Known Issues
* [`NumberPadTimePicker`] is not optimized for small screen heights (e.g. in landscape).
If the full view cannot fit in the current screen height, the top portion of its contents will be 
stretched out and the remaining portion cannot be seen.

### Bug Fixes
* [`BottomSheetNumberPadTimePickerDialog`] enter animation is janky and distorted in landscape 
mode for SDK 23+
* [`BottomSheetNumberPadTimePickerDialog`] is raised slightly away from screen edge in landscape 
mode for SDK 23+

# 1.0.0 (2017-06-14)
Initial release

[`BottomSheetNumberPadTimePickerDialog`]: library/src/main/java/com/philliphsu/numberpadtimepicker/BottomSheetNumberPadTimePickerDialog.java
[`NumberPadTimePicker`]: library/src/main/java/com/philliphsu/numberpadtimepicker/NumberPadTimePicker.java
[`NumberPadTimePickerThemer`]: library/src/main/java/com/philliphsu/numberpadtimepicker/NumberPadTimePickerThemer.java
[`BottomSheetNumberPadTimePickerThemer`]: library/src/main/java/com/philliphsu/numberpadtimepicker/BottomSheetNumberPadTimePickerThemer.java
[`NumberPadTimePickerDialogThemer`]: library/src/main/java/com/philliphsu/numberpadtimepicker/NumberPadTimePickerDialogThemer.java
[`BottomSheetNumberPadTimePickerDialogThemer`]: library/src/main/java/com/philliphsu/numberpadtimepicker/BottomSheetNumberPadTimePickerDialogThemer.java