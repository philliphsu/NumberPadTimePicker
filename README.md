# NumberPadTimePicker

Remember the time picker from the AOSP Clock app in Jelly Bean? It's back--in Material Design--with
a familiar but improved interaction design.

Remember there were placeholders in the header display and digits were filled in right-to-left?
Think that seemed backward? That's no longer an issue.

This implementation has no placeholders and digits are inserted left-to-right. The time separator
(e.g. ":") character is dynamically formatted into the typed time at the correct position.

Number keys and the 'OK' button are still enabled or disabled as you type, so you can't set an
invalid time.

The time picker is available as an alert dialog and as a bottom sheet dialog. **This library does not
force you to use Fragments.**

<img src="screenshots/1.png" width="180" height="320"> <img src="screenshots/2.png" width="180" height="320"> <img src="screenshots/3.png" width="180" height="320"> <img src="screenshots/4.png" width="180" height="320">

1. [Sample App](#sample-app)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Themes and Styles](#themes-and-styles)
    5. [XML Theming](#xml-theming)
    6. [Programmatic Theming](#programmatic-theming)
7. [Contributing](#contributing)
8. [License](#license)

## Sample App

The sample app demos some built-in themes. You can also dynamically customize your own theme with a
limited color palette.

* Google Play listing coming soon!
* [Source code](/sample)

## Installation

In your root `build.gradle`:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
In your module `build.gradle`:

```groovy
dependencies {
    compile 'com.github.philliphsu:numberpadtimepicker:1.0.0'
}
```

## Usage

Using the NumberPadTimePicker library is similar to the process outlined in the [Pickers][1] API
guide.

Implement the standard `android.app.TimePickerDialog.OnTimeSetListener` interface.

```java
// The TimePicker returned in this callback is a dummy object. It is not
// the actual number pad time picker used by this library.
@Override
public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    // Do something with the time chosen by the user
}
```

Obtain an instance of `NumberPadTimePickerDialog` or `BottomSheetNumberPadTimePickerDialog`. You may
pass in an optional style resource to use as the dialog's theme. Alternatively, you can theme the
dialog programmatically after you've obtained an instance. See [Themes and Styles][2] for a guide
to styling the dialog.

```java
NumberPadTimePickerDialog dialog = new NumberPadTimePickerDialog(
    context, R.style.MyAlertDialogTheme, listener, is24HourMode);
```

```java
BottomSheetNumberPadTimePickerDialog dialog = new BottomSheetNumberPadTimePickerDialog(
    context, R.style.MyBottomSheetDialogTheme, listener, is24HourMode);
```

If you want to use the dialog in a DialogFragment, make sure to use the support library's
`android.support.v4.app.DialogFragment`.

[1]: https://developer.android.com/guide/topics/ui/controls/pickers.html#TimePicker
[2]: #themes-and-styles

## Themes and Styles

The NumberPadTimePicker library allows you to customize the theme in XML or programmatically.

### XML Theming

Create a dialog theme that inherits from an appropriate parent theme.

If you are using `NumberPadTimePickerDialog`, this will be one of:

    * `Theme.AppCompat.Dialog.Alert`
    * `Theme.AppCompat.Light.Dialog.Alert`
    * `Theme.AppCompat.DayNight.Dialog.Alert`

If you are using `BottomSheetNumberPadTimePickerDialog`, this will be one of:

    * `Theme.Design.BottomSheetDialog`
    * `Theme.Design.Light.BottomSheetDialog`

Your dialog theme should include an item for the `nptp_numberPadTimePickerStyle` attribute. This
attribute references a style resource that styles the number pad time picker `View` contained in
the dialog. The library provides default styles that you can specify for this attribute. You may
override these styles.

    * If your dialog theme inherits from an alert dialog theme, use the
      `NPTP_NumberPadTimePickerStyle` style.

    * If your dialog theme inherits from a bottom sheet dialog theme, use the
      `NPTP_NumberPadTimePickerBottomSheetStyle` style.

#### Table 1. Common styleable attributes

Attribute                | Notes
-------------------------|:------------------------------------------------------------------------:
nptp_inputTimeTextColor  | -
nptp_inputAmPmTextColor  | -
nptp_backspaceTint       | This should be a color state list with the enabled and disabled states.
nptp_numberKeysTextColor | This should be a color state list with the enabled and disabled states.
nptp_altKeysTextColor    | "AM"/"PM" for 12-hour mode or ":00"/":30" for 24-hour mode. This should be a color state list with the enabled and disabled states.
nptp_headerBackground    | -
nptp_divider             | -
nptp_numberPadBackground | -

#### Table 2. Bottom sheet styleable attributes

Attribute                      | Notes
-------------------------------|:------------------------------------------------------------------:
nptp_fabBackgroundColor        | This should be a color state list with the enabled and disabled states.
nptp_animateFabBackgroundColor | Whether the `FloatingActionButton` should transition between its enabled and disabled colors. Does not apply if `nptp_showFab` is set to `validTime`.
nptp_fabRippleColor            | -
nptp_animateFabIn              | Whether the `FloatingActionButton` should animate onto the screen. Does not apply if `nptp_showFab` is set to `validTime`. 
nptp_showFab                   | `always` or `validTime`
nptp_backspaceLocation         | `header` or `footer`
nptp_fabIconTint               | This should be a color state list with the enabled and disabled states.

Here is an example `styles.xml`:

```xml
<style name="MyStyle" parent="NPTP_NumberPadTimePickerStyle">
    <!-- ... -->
</style>

<style name="MyAlertDialogTheme" parent="Theme.AppCompat.Dialog.Alert">
    <item name="nptp_numberPadTimePickerStyle">@style/MyStyle</item>
</style>

<style name="MyBottomSheetStyle" parent="NPTP_NumberPadTimePickerBottomSheetStyle">
    <!-- ... -->
</style>

<style name="MyBottomSheetDialogTheme" parent="Theme.Design.BottomSheetDialog">
    <item name="nptp_numberPadTimePickerStyle">@style/MyBottomSheetStyle</item>
</style>
```
With your dialog theme defined, obtain an instance of the dialog as described in [Usage](#usage).
Alternatively, if you don't want to pass the theme to the dialog's constructor, you can specify
the dialog's theme in your `Activity`'s theme.

```xml
<style name="AppTheme" parent="Theme.AppCompat">
    <item name="nptp_numberPadTimePickerAlertDialogTheme">@style/MyAlertDialogTheme</item>
    <item name="nptp_numberPadTimePickerBottomSheetDialogTheme">@style/MyBottomSheetDialogTheme</item>
</style>
```

### Programmatic Theming

Call `getThemer()` on your dialog to obtain

    * a `NumberPadTimePickerDialogThemer`, if the dialog is a `NumberPadTimePickerDialog`
    * a `BottomSheetNumberPadTimePickerDialogThemer`, if the dialog is a
      `BottomSheetNumberPadTimePickerDialog`

You can chain together the available setters. The names of the setters correspond to the attribute
names listed in [Table 1](#table-1) and [Table 2](#table-2).

## Contributing

Contributions are welcome. In particular, contributions to optimize dimensions for larger form
factors or to improve localization are appreciated. Send pull requests to the `develop` branch.

## License

```
Copyright 2017 Phillip Hsu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```