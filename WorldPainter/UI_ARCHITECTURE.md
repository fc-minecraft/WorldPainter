# UI Architecture

## Overview
The UI is built using a modern, modular approach centered around the `org.pepsoft.worldpainter.ui` package.

## Components

### Main Layout (`ModernMainLayout`)
- **Location:** `org.pepsoft.worldpainter.ui.ModernMainLayout`
- **Description:** Defines the high-level structure of the application window.
- **Structure:**
  - `NORTH`: Toolbar
  - `WEST`: Sidebar (Palette)
  - `CENTER`: Workspace (WorldPainter View)
  - `SOUTH`: Status Bar

### Theme & Styling
- **Location:** `org.pepsoft.worldpainter.theme.ThemeManager`
- **Description:** Centralized configuration for fonts, colors, and component metrics.
- **Key Constants:**
  - `FONT_SIZE_BASE`: Base font size (e.g., 20px).
  - `BUTTON_MARGINS`: Margins for buttons.
  - `ICON_SIZE`: Standard icon sizes.

### Icons (`Icons`)
- **Location:** `org.pepsoft.worldpainter.ui.Icons`
- **Description:** Centralized loading and scaling of icons. Use this instead of `IconUtils` directly to ensure consistent sizing.

### Factories
- **PanelFactory:** Creates the main content panels (Palette, Brushes).
- **StatusBarFactory:** Creates the status bar.
- **ToolbarFactory:** Creates the main toolbar.

## Editing the UI

### Changing Panel Order
Edit `org.pepsoft.worldpainter.ui.PanelFactory`.

### Changing Colors/Fonts
Edit `org.pepsoft.worldpainter.theme.ThemeManager`.

### Adding a New Tool
1. Register the tool in `App.java`.
2. Add the button in `PanelFactory` or `ToolbarFactory`.
