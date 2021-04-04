package wf;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkflowWorker {
    private static final AmazonSimpleWorkflow swf =
            AmazonSimpleWorkflowClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();

    public static void main(String[] args){
        PollForDecisionTaskRequest task_request =
                new PollForDecisionTaskRequest()
                        .withDomain(WorkflowTypes.DOMAIN)
                        .withTaskList(new TaskList().withName(WorkflowTypes.TASKLIST));

        while (true) {
            System.out.println(
                    "Polling for a decision task from the tasklist '" +
                            WorkflowTypes.TASKLIST + "' in the domain '" +
                            WorkflowTypes.DOMAIN + "'.");

            DecisionTask task = swf.pollForDecisionTask(task_request);

            String taskToken = task.getTaskToken();
            if (taskToken != null) {
                try {
                    executeDecisionTask(taskToken, task.getEvents());
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    private static void executeDecisionTask(String taskToken, List<HistoryEvent> events) {
        List<Decision> decisions = new ArrayList<Decision>();
        String workflow_input = null;
        int scheduled_activities = 0;
        int open_activities = 0;
        boolean activity_completed = false;
        String result = null;
        System.out.println("Executing the decision task for the history events: [");
        for (HistoryEvent event : events) {
            System.out.println("  " + event);
            switch(event.getEventType()) {
                case "WorkflowExecutionStarted":
                    workflow_input =
                            event.getWorkflowExecutionStartedEventAttributes()
                                    .getInput();
                    break;
                case "ActivityTaskScheduled":
                    scheduled_activities++;
                    break;
                case "ScheduleActivityTaskFailed":
                    scheduled_activities--;
                    break;
                case "ActivityTaskStarted":
                    scheduled_activities--;
                    open_activities++;
                    break;
                case "ActivityTaskCompleted":
                    open_activities--;
                    activity_completed = true;
                    result = event.getActivityTaskCompletedEventAttributes()
                            .getResult();
                    break;
                case "ActivityTaskFailed":
                    open_activities--;
                    break;
                case "ActivityTaskTimedOut":
                    open_activities--;
                    break;
            }


        }
        System.out.println("]");

        if (activity_completed) {
            decisions.add(
                    new Decision()
                            .withDecisionType(DecisionType.CompleteWorkflowExecution)
                            .withCompleteWorkflowExecutionDecisionAttributes(
                                    new CompleteWorkflowExecutionDecisionAttributes()
                                            .withResult(result)));
        } else {
            if (open_activities == 0 && scheduled_activities == 0) {

                ScheduleActivityTaskDecisionAttributes attrs =
                        new ScheduleActivityTaskDecisionAttributes()
                                .withActivityType(new ActivityType()
                                        .withName(WorkflowTypes.ACTIVITY)
                                        .withVersion(WorkflowTypes.ACTIVITY_VERSION))
                                .withActivityId(UUID.randomUUID().toString())
                                .withInput(workflow_input);

                decisions.add(
                        new Decision()
                                .withDecisionType(DecisionType.ScheduleActivityTask)
                                .withScheduleActivityTaskDecisionAttributes(attrs));
            } else {
                // an instance of HelloActivity is already scheduled or running. Do nothing, another
                // task will be scheduled once the activity completes, fails or times out
            }
        }

        System.out.println("Exiting the decision task with the decisions " + decisions);
    }



}


//
/*

  features
          feature1
          feature2
   plan
      Baseplan
      Enterprise

   Entitlement
   BasePlan -> feature2
   EnterpisePlan - feawture1, feature2




// -> who is asking

// customer knows of a subscription and wants to find what he is eligilble for
// customer (merchant/customer) knows of a plan and wants to find what he is elibigle for

merchant -> user/ for find what he ie eligible
customer -> what am eligible for ?

features, can exist without any plan
Entitle neede a plan
plan can exist even withou subscription

Entitlement is sea

/plans/{planid}/entitlements
/customers/{customerid}/plans/entitlements

/plans/{planid}/entitlements - not a viable
/entitlements/ - ensuring some thing a plan exist
/en
/features/ {merchantid}


entitllement domain
/entitlement/{entitlementid/featues

/subsction/

/ems
 entitlement
  RestofWorld:   /entitlements/{refid}

       /entitlements/{refid}

chargebee <-/subscriptions/{subscriptionid}/
          -> look up
               -> /entilement/refid/
                ->  {
                      url:/entitlements/refId
                      }
 subscriptionentitlement
/

API Gateway,
  -> {composes multiple microservier
  -> /entitlement/?planId
  ->entilment/?subscrition
             =>get plan /subscription/
             -> get entilemtent id
              -> getentilment


plan/
{plan,entitlementid}

Entitlements
{EntitlementGroup}
  ->{BaseEntitlement?-featuir
  -> {Entitlement) -> {features}



/entitlemens/en

 */

//