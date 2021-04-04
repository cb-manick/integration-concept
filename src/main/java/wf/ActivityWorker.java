package wf;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.*;


public class ActivityWorker {
    private static final AmazonSimpleWorkflow swf =
            AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

    private static String sayHello(String input) throws Throwable {
        return "Hello, " + input + "!";
    }

    public static void main(String[] args){
        System.out.println("Polling for an activity task from the tasklist '"
                + WorkflowTypes.TASKLIST + "' in the domain '" +
                WorkflowTypes.DOMAIN + "'.");

        ActivityTask task = swf.pollForActivityTask(
                new PollForActivityTaskRequest()
                        .withDomain(WorkflowTypes.DOMAIN)
                        .withTaskList(
                                new TaskList().withName(WorkflowTypes.TASKLIST)));

        String task_token = task.getTaskToken();

        if (task_token != null) {
            String result = null;
            Throwable error = null;

            try {
                System.out.println("Executing the activity task with input '" +
                        task.getInput() + "'.");
                result = sayHello(task.getInput());
            } catch (Throwable th) {
                error = th;
            }

            if (error == null) {
                System.out.println("The activity task succeeded with result '"
                        + result + "'.");
                swf.respondActivityTaskCompleted(
                        new RespondActivityTaskCompletedRequest()
                                .withTaskToken(task_token)
                                .withResult(result));
            } else {
                System.out.println("The activity task failed with the error '"
                        + error.getClass().getSimpleName() + "'.");
                swf.respondActivityTaskFailed(
                        new RespondActivityTaskFailedRequest()
                                .withTaskToken(task_token)
                                .withReason(error.getClass().getSimpleName())
                                .withDetails(error.getMessage()));
            }
        }
    }

}
