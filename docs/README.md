# LabTronic

A work-in-progress project of a desktop applicaton for performing various tasks related to the calibration process of digital multimeters and multifunction calibrators.

## :ledger: Index

- [About](#beginner-about)
- [Technologies used](#bulb-technologies-used)
- [Features](#star2-features)
- [Usage](#zap-usage)
  - [Requirements](#electric_plug-requirements)
  - [How to use the Service Valuation tab](#heavy_dollar_sign-how-to-use-the-service-valuation-tab)
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
  <img src="/docs/media/preview_main_window.png" width="100%" height="100%" align="center">
</p>

## :zap: Usage
To test the application in its curent state just download the latest [pre-release](https://github.com/RobertR-01/labtronic/releases), unzip it and run the `.jar` file.

#### :electric_plug: Requirements
- Windows OS
- Java 19+

### :heavy_dollar_sign: How to use the Service Valuation tab
The algorithm implemented for performing a service valuation for both DMMs and calibrators reflects the guidelines outlined in this pricing table:

<div align="center">
  <img src="/docs/media/pricing_table.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>Pricing table used as a base for the valuation algortihm</i>
  </p>
</div>
&nbsp;

To prepare a calibration service quote (according to the customer's requirements) use the Service Valuation tab by following these steps:

<div align="center">
  <img src="/docs/media/valuation/valuation_step_1.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>#1. Tick a checkbox for any of the measurement fucntions at the top.</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/valuation/valuation_step_2.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>#2. Add a new measurement range for the selected function either by using the "+" button or via the right click context menu (invoked by clicking inside the table).</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/valuation/valuation_step_3.png" width="50%" height="50%" align="center">
  <p align="center">
    <i>#3. Fill in all necessary info in the new range creation dialog. The EURAMET button can be used to automatically generate measurement points for this range (following the guidelines of EURAMET cg-15).</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/valuation/valuation_step_4.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>#4. The newly added measurement range is now visible in the table within a corresponding section.</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/valuation/valuation_step_5.png" width="75%" height="75%" align="center">
  <p align="center">
    <i>#5. You can inspect the measurement points assigned to that range by double clicking it inside the table.</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/valuation/valuation_step_6.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>#6. While adding a new AC section (voltage or current), the frequency level and its unit must be entered in an extra dialog. Any additional AC sections for different frequencies can be added and removed via the "+|-" buttons at the top.</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/valuation/valuation_step_7.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>#7. The total price of the service can be viewed at the top-right side of this tab. The price of each section (measurement function) and its corresponding ranges can be viewed at the top of each table and inside the "Cost" column.</i>
  </p>
</div>

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
  <img src="/docs/media/preview_customer_registration_popup.png" width="50%" height="50%" align="center">
  <p align="center">
    <i>Customer registration dialog</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/preview_valuation_1.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>Service Valuation tab</i>
  </p>
</div>
&nbsp;

<div align="center">
  <img src="/docs/media/preview_budgets.png" width="100%" height="100%" align="center">
  <p align="center">
    <i>Uncertainty Budgets tab (WIP)</i>
  </p>
</div>

## :pray: Credits
Graphics used for this application:

| Graphics pack                                              |          Author          | Link                              |
|------------------------------------------------------------|:------------------------:|-----------------------------------|
| ikonli (ikonli-bootstrapicons-pack & ikonli-boxicons-pack) | Andres Almiray           | https://github.com/kordamp/ikonli |
