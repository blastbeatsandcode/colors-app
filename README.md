# colors-app
**Fun with colors and drawing.**

This application is my take on the first required homework for CEG 4110 at WSU. The intention of this assignment is to test students' ability to work within a mobile development environment and to prepare them for the overall group assignment that largely makes up rest of the course.

## MainActivity
`MainActivity` is the initial activity of this application. The user can enter a string of text at the top of the screen and select the "Change Color" button which will set the text to a random color while also displaying the RGB and Hex value of that color.
<img src="images/scren-01.png">

## DrawingActivity
<img src="images/scrn-03.png" align="right">
`DrawingActivity` is the second activity of this application which allows the user to draw on the screen, save the image they drew, clear the image they've drawn, or select a new color to draw on the screen with.

## DrawingCanvasView
`DrawingCanvasView` is a custom view which enables drawing on a canvas. This can be used in any android application to add the ability for users to draw.

**You can even save your beautiful art to your device!**
<img src="images/src-04.png">

### Dependencies

There is one main [external dependency](https://github.com/duanhong169/ColorPicker) in this project made up by the color picker library. Include this in `build.gradle` to implement:
```
dependencies {
    ...
implementation 'com.github.duanhong169:colorpicker:1.1.2'
    ...
}
```

### Resources
This app was created with the help of a few kind individuals on the internet sharing their knowledge and abilities.

- [Color Picker](https://github.com/duanhong169/ColorPicker) library implemented to allow users to select a color to draw with.

- [Helpful Article](https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202) for understanding drawing in an android application.