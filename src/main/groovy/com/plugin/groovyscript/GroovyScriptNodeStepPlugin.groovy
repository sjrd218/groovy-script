/*
 * Copyright 2019 Rundeck, Inc. (http://rundeck.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plugin.groovyscript

import com.dtolabs.rundeck.core.common.INodeEntry
import com.dtolabs.rundeck.core.execution.workflow.steps.FailureReason
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException
import com.dtolabs.rundeck.core.execution.workflow.steps.node.NodeStepException
import com.dtolabs.rundeck.core.plugins.Plugin
import com.dtolabs.rundeck.plugins.ServiceNameConstants
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty
import com.dtolabs.rundeck.plugins.descriptions.RenderingOption
import com.dtolabs.rundeck.plugins.descriptions.RenderingOptions
import com.dtolabs.rundeck.plugins.descriptions.ReplaceDataVariablesWithBlanks
import com.dtolabs.rundeck.plugins.step.NodeStepPlugin
import com.dtolabs.rundeck.plugins.step.PluginStepContext
import org.codehaus.groovy.control.CompilationFailedException

import static com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants.CODE_SYNTAX_MODE
import static com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants.DISPLAY_TYPE_KEY

@Plugin(service= ServiceNameConstants.WorkflowNodeStep,name=GroovyScriptNodeStepPlugin.SERVICE_PROVIDER_NAME)
@PluginDescription(title="Groovy Script Node Step", description="Workflow node step that runs a groovy script")
class GroovyScriptNodeStepPlugin implements NodeStepPlugin {

    public static final String SERVICE_PROVIDER_NAME = "groovy-script-node";

    @PluginProperty(title="Script", description = "The groovy script to run")
    @RenderingOptions(
            [
                    @RenderingOption(key = DISPLAY_TYPE_KEY, value = "CODE"),
                    @RenderingOption(key = CODE_SYNTAX_MODE, value = "groovy")
            ]
    )
    @ReplaceDataVariablesWithBlanks(value=false)
    String script

    /**
     * This enum lists the known reasons this plugin might fail
     */
    static enum Reason implements FailureReason{
        CompilationFailed
    }

    @Override
    void executeNodeStep(
            final PluginStepContext context,
            final Map<String, Object> configuration,
            final INodeEntry entry
    ) throws NodeStepException {

        def sharedData = new Binding()
        sharedData.setProperty("context",context)
        sharedData.setProperty("node",entry)

        def gs = new GroovyShell(sharedData)
        try {
            gs.evaluate(script)
        } catch(CompilationFailedException cfx) {
            throw new StepException("Groovy script failed", cfx, Reason.CompilationFailed)
        }
    }
}
