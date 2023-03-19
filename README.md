# Welcome to Battery Meter

The aim of this app is to provide an appwidget that shows the battery level of the main device. It
uses Glance and a couple of other Jetpack libraries.

For now the main goal is not to offer it on Google Play, but to use it as a tool for finding out how
easy / still possible it is to write appwidgets that are updated frequently enough to be useful.

I will be documenting some of my learnings in this README.

## Update intervals

### `android:updatePeriodMillis` ([UPDATE_PERIOD_MILLIS](https://github.com/tkuenneth/battery_meter/releases/tag/UPDATE_PERIOD_MILLIS))

The intrinsic method of requesting appwidget updates is to set
`android:updatePeriodMillis` inside the `<appwidget-provider>` tag. The documentation notes that

> Updates requested with updatePeriodMillis will not be delivered more than once every 30 minutes.

While 30 minutes are likely too much to be of use, the first version will be implemented exactly 
like this. That's because I want to find out if battery optimization features influence (block) 
such updates.
