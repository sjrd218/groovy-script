package com.plugin.groovyscript;

import com.dtolabs.rundeck.core.execution.workflow.steps.FailureReason;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.descriptions.RenderingOption;
import com.dtolabs.rundeck.plugins.descriptions.RenderingOptions
import com.dtolabs.rundeck.plugins.descriptions.ReplaceDataVariablesWithBlanks;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import com.dtolabs.rundeck.plugins.step.StepPlugin;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
import com.dtolabs.rundeck.plugins.PluginLogger
import org.codehaus.groovy.control.CompilationFailedException;

import java.util.Map

import static com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants.CODE_SYNTAX_MODE;
import static com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants.DISPLAY_TYPE_KEY;

@Plugin(service=ServiceNameConstants.WorkflowStep,name=GroovyScriptStepPlugin.SERVICE_PROVIDER_NAME)
@PluginDescription(title="Groovy Script", description="Workflow step that runs a groovy script")
public class GroovyScriptStepPlugin implements StepPlugin {

    public static final String SERVICE_PROVIDER_NAME = "groovy-script";

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
   public void executeStep(final PluginStepContext context, final Map<String, Object> configuration) throws
                                                                                                      StepException{
        def sharedData = new Binding()
        sharedData.setProperty("context",context)
        sharedData.setProperty("configuration",configuration)

        def gs = new GroovyShell(sharedData)
        try {
            gs.evaluate(script)
        } catch(CompilationFailedException cfx) {
            throw new StepException("Groovy script failed", cfx, Reason.CompilationFailed)
        }

   }

}