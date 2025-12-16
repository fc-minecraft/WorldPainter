# Building WorldPainter

This repository contains the source code for WorldPainter. The build system has been migrated to Gradle to support cross-platform builds and easier dependency management, especially with offline libraries.

## Prerequisites

*   **Java 17 JDK**: The build is configured to use Java 17 toolchains. Gradle will attempt to download it if not present, but having it installed is recommended.
*   **Gradle**: The repository includes a Gradle Wrapper (`gradlew`), so you do not need to install Gradle manually.

## Structure

*   `WorldPainter/`: Contains the source code modules.
    *   `WPCore`: Core logic.
    *   `WPDynmapPreviewer`: Dynmap integration.
    *   `WPGUI`: The main GUI application.
*   `lib/`: Contains offline dependencies (Jars) that are not easily available in public Maven repositories or are patched versions.

## Building Locally

To build the application and create a distribution:

1.  Open a terminal in the repository root.
2.  Run the following command:

    *   **Linux/macOS:**
        ```bash
        ./gradlew installDist
        ```
    *   **Windows:**
        ```cmd
        gradlew installDist
        ```

3.  The build output will be in `WorldPainter/WPGUI/build/install/WorldPainter`.
    *   You can run the application using the script in `bin/WorldPainter` (Linux/macOS) or `bin/WorldPainter.bat` (Windows).

## GitHub Actions CI/CD

The `.github/workflows/build.yml` file configures the Continuous Integration and Delivery pipeline.

*   **Triggers:** Pushes to any branch and Tags starting with `v*`.
*   **Platforms:** Builds are run on Ubuntu, Windows, and macOS runners.
*   **Artifacts:**
    *   `WorldPainter-Windows-Portable.zip`: A portable ZIP for Windows containing the application and libraries.
    *   `WorldPainter-Linux.tar.gz`: A tarball for Linux.
    *   `WorldPainter-macOS.zip`: A ZIP for macOS.
*   **Releases:** When a tag starting with `v` (e.g., `v2.26.1`) is pushed, a GitHub Release is automatically created, and the artifacts are uploaded.

## Troubleshooting

*   **Missing Dependencies:** If you encounter missing classes, ensure the relevant JAR file exists in the `lib/` directory and is listed in the `dependencies` block of the corresponding project in `build.gradle`.
*   **Toolchain Errors:** If Gradle complains about Java versions, ensure you have internet access so it can download the JDK, or configure a local Java 17 installation.
