# UI Design Guide

This document explains how to customize the interface of WorldPainter. The design system has been centralized to make modifications straightforward.

## 1. Theme and Layout Configuration

All core visual settings are located in:
`WorldPainter/WPGUI/src/main/java/org/pepsoft/worldpainter/theme/ThemeManager.java`

You can change:
*   **Fonts:** `FONT_SIZE_BASE` (default: 16)
*   **Icons:** `ICON_SIZE_MEDIUM`, `ICON_SIZE_LARGE`, `ICON_SIZE_EXTRA_LARGE`
*   **Buttons:** `BUTTON_ARC` (Roundness), `BUTTON_MARGINS` (Padding)
*   **Layout:** `ROW_HEIGHT`, `TAB_HEIGHT`

### Example: Changing Font Size
To make the font larger, edit line ~20 in `ThemeManager.java`:
```java
public static final int FONT_SIZE_BASE = 18; // Changed from 16 to 18
```

## 2. Localization (Strings)

All text in the application is stored in property files. To change English or Russian text, edit:

*   **Russian:** `WorldPainter/WPGUI/src/main/resources/org/pepsoft/worldpainter/resources/strings_ru.properties`
*   **English:** `WorldPainter/WPGUI/src/main/resources/org/pepsoft/worldpainter/resources/strings.properties`

### Key Files
*   `ExportWorldDialog.java`: Uses keys starting with `dialog.export.*`
*   `App.java`: Uses keys starting with `dock.*`, `menu.*`, etc.

## 3. Icons

Icons are located in `WorldPainter/WPGUI/src/main/resources/org/pepsoft/worldpainter/icons/`.
To replace an icon, overwrite the png file with a new one of similar aspect ratio. The application scales them automatically based on `ThemeManager` constants.

## 4. Layout Engine

The side panels (Terrain, Brushes) use a custom `WrapLayout` engine located in:
`WorldPainter/WPGUI/src/main/java/org/pepsoft/worldpainter/util/swing/WrapLayout.java`

This ensures buttons wrap correctly into rows without stretching the panel horizontally.
