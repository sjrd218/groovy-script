# Groovy Script Rundeck Plugins

This plugin bundle contains a Rundeck workflow step plugin and a node step plugin that can run Groovy scripts.

## Build and install

### Build

./gradlew build

### Install into Rundeck

cp build/libs/groovy-script-0.1.0.jar $RD_BASE/libext

## Usage

When adding a Groovy Script step, an ACE editor with Groovy syntax highlighting 
will allow  you to enter your Groovy script.  Note that the classpath that the 
script runs under is inherited from the context invoking the plugin (i.e., Rundeck itself).
This means you have access to any classes that are on the Rundeck classpath.  This
can be immensely useful--and also a security consideration.

If your Groovy script fails, a rather terse `Groovy script failed` message is
provided. You can re-run the job with debugging turned on to receive the full 
stacktrace indicating why the script failed.

### Usage - Workflow Step

Add a workflow step using this plugin - `Groovy Script`.

The following variable is passed to your script:

```
context : PluginStepContext - the context object passed to the plugin
```

### Usage - Workflow Node Step

Add a workflow node step using this plugin - `Groovy Script Node Step`.

The following variables will be passed to your script:

```
node : INodeEntry - the current node for which the script is executing
context : PluginStepContext - the context object passed to the plugin
```
