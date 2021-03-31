package wf.greeter;

import wf.ActivityWorker;

public class GreeterWorker {

    public static void main(String[] args) throws Exception {

        String domain = " helloWorldExamples";
        String taskListToPoll = "HelloWorldList";

        ActivityWorker aw = new ActivityWorker(service, domain, taskListToPoll);
        aw.addActivitiesImplementation(new GreeterActivitiesImpl());
        aw.start();
    }
}
