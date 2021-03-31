package wf.greeter;

import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

public class GreeterWorkflowImpl implements GreeterWorkflow {
    private GreeterActivitiesClient operations1 = new GreeterActivitiesClientImpl1(); 
    private GreeterActivitiesClient operations2 = new GreeterActivitiesClientImpl2(); 
    
    @Override
    public void greet() {
        Promise<String> name = operations1.getName();
        Promise<String> greeting = operations1.getGreeting(name);
        runSay(greeting);
    }
    
    @Asynchronous
    private void runSay(Promise<String> greeting){
        String taskList = operations1.getSchedulingOptions().getTaskList();
        ActivitySchedulingOptions schedulingOptions = new ActivitySchedulingOptions();
        schedulingOptions.setTaskList(taskList);
        operations2.say(greeting, schedulingOptions);
    }
}