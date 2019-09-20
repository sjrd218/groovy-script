package com.plugin.groovyscript

import com.dtolabs.rundeck.plugins.step.PluginStepContext
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException
import com.dtolabs.rundeck.plugins.PluginLogger
import spock.lang.Specification

class GroovyScriptStepPluginSpec extends Specification {

    def getContext(PluginLogger logger){
        Mock(PluginStepContext){
            getLogger()>>logger
        }
    }


}