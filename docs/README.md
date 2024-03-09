# LabTronic

A WIP project of a desktop applicaton for performing various tasks related to the calibration process of digital multimeters and multifunction calibrators.

## :ledger: Index

- [About](#beginner-about)
- [Technologies used](#bulb-technologies-used)
- [Features](#star2-features)
- [Usage](#zap-usage)
  - [Requirements](#electric_plug-requirements)
- [Development](#wrench-development)
  - [Planned features](#bulb-planned-features)
  - [Pre-Requisites](#pencil-pre-requisites)
- [Gallery](#camera-gallery)
- [Credits](#pray-credits)

## :beginner: About
The core feature of the application's current version is a valuation tab. It allows the user to prepare a comprehensive quote of a calibration service requested by the client (according to their selection of measurement ranges / points and some other device-specific factors like e.g. display resolution).

At the moment the application features an initial take on the base GUI, which allows the user to go through the process of a customer (and their instrument) registration.

The main interface is in a form of a TabPane nested within another TabPane, which allows the user to have multiple calibration projects (files) opened and to edit them simultaneously.

An early version of the tab for estimating the measurement uncertainty (uncertainty budgets) has also been implemented (not fully functional yet).

See the [:zap: Usage](#zap-usage) section for more in-depth information on how to handle specific UI components and how to go through the service valuation step.

## :bulb: Technologies used
- JavaFX (GUI)
- Java SE 19 (base logic)

## :star2: Features
- base GUI that allows the user to perform customer registration and to input necessary instrument data 
- fully functional Service Valuation tab for preparing a calibration service quote, based on the DUT’s type, resolution (if applicable) and selected measurement functions / ranges / points
- validation for most input fields used so far to prevent both missing/incorrect data, but also more subtle errors like illegal calibration range/points outside the CMC boundaries, negative AC values etc.

<p align="center">
  <img src="/docs/media/preview_1.png" width="75%" height="75%" align="center">
</p>

## :zap: Usage
To test the application in its curent state just download the latest [pre-release](https://github.com/RobertR-01/mini-casino/releases), unzip it and run the `.jar` file.

#### :electric_plug: Requirements
- Windows OS
- Java 19+

## :wrench: Development
The application is still under development. The project's source code the can be freely cloned, inspected and modified using an IDE of choice. 

#### :bulb: Planned features
- finalizing the uncertainty budgets tab
- additional tab for inputting the environmental conditions present during the measurements
- small database module for storing and updating the reference standards' error and uncertainty values (these values play a role as a component of the uncertainty budget)
- results tab, allowing final data presentation and generation of a calibration certificate in a form of a `.pdf` file
- implementing some basic data export/import feature to let the user to store and edit their calibration file

#### :pencil: Pre-Requisites
- JDK 19 or higher
- IntelliJ IDEA or any other IDE with Maven support

## :camera: Gallery

<div align="center">
  <img src="/docs/media/preview_3.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>Main menu</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/preview_2.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>Slots game window (half of the symbols disabled)</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/profile_preview_1.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>In-game profile selection window</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/profile_preview_2.png" width="35%" height="35%" align="center">
  <p align="center">
    <i>Editing the selected profile</i>
  </p>
</div>

<div align="center">
  <img src="/docs/media/nudge_preview.png" width="60%" height="60%" align="center">
  <p align="center">
    <i>Nudge feature info window</i>
  </p>
</div>

## :pray: Credits
Graphics used for this application:

| Graphics pack                                              |          Author          | Link                              |
|------------------------------------------------------------|:------------------------:|-----------------------------------|
| ikonli (ikonli-bootstrapicons-pack & ikonli-boxicons-pack) | Andres Almiray           | https://github.com/kordamp/ikonli |
