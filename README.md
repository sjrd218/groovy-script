# Groovy Script Rundeck Plugins

This plugin bundle contains a Rundeck workflow step plugin and a node step plugin that can run Groovy scripts.

## Build and install

#### Build

./gradlew build

#### Install into Rundeck

cp build/libs/groovy-script-0.1.0.jar $RD_BASE/libext

## Usage

Add a workflow step using this plugin - `Groovy Script`.

An ACE editor with groovy syntax highlighting allows you to enter your Groovy script.

The classpath that the script runs with is inherited from the Classpath that invokes the plugin.
This means you have access to classes that are on the Rundeck classpath.

Two variables are passed as binding variables to your script.

```
configuration : Map<String,Object> - the configuration of the plugin
context : PluginStepContext - the context object passed to the plugin
```

If the groovy script you write fails your step will fail
with a `Groovy script failed` message. Re-run the job with debugging turned
on and you will get the full stacktrace detailing why the script failed.

### Node Step Plugin

Add a workflow node step using this plugin - `Groovy Script Node Step`.

The node step plugin also contains the following variable:

```node : INodeEntry - the current node for which the script is executing```