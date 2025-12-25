# UI Design Guide - WorldPainter Modernization

## Philosophy

The goal of the new design is to create a **Modern, Minimalist, and Child-Friendly** interface.
This means:
1.  **Large Targets:** Buttons, tabs, and inputs should be large and easy to click/tap.
2.  **Readability:** Fonts should be large (20pt base) and high contrast.
3.  **Simplicity:** Reduce visual noise. Hide advanced options behind menus or "More" buttons.
4.  **Whitespace:** Use ample padding (24px+) to separate distinct areas.

## Architecture

The theming logic is centralized in `org.pepsoft.worldpainter.theme.ThemeManager`.

### Key Constants
*   `FONT_SIZE_BASE`: 20px (Main text size)
*   `ROW_HEIGHT`: 40px (Lists, Trees, Tables)
*   `BUTTON_ARC`: 999 (Capsule buttons)
*   `ICON_SIZE_MEDIUM`: 48px (Toolbar icons)

### Editing the Theme
To adjust global metrics, edit `src/main/java/org/pepsoft/worldpainter/theme/ThemeManager.java`.
Do not hardcode sizes or fonts in individual UI classes. Use `UIManager` keys or `ThemeManager` constants.

## Localization

All user-facing strings must be moved to `strings.properties`.
*   **English:** `src/main/resources/org/pepsoft/worldpainter/resources/strings.properties`
*   **Russian:** `src/main/resources/org/pepsoft/worldpainter/resources/strings_ru.properties`

### Adding a new String
1.  Add `key=Value` to `strings.properties`.
2.  Add `key=Значение` to `strings_ru.properties`.
3.  Use `strings.getString("key")` in Java code.

## Native Integration
*   **File Selection:** Always use `org.pepsoft.worldpainter.util.FileUtils` for file opening/saving. This ensures the native OS file picker is used where possible.
