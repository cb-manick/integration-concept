package wf;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClientBuilder;
import com.amazonaws.services.simpleworkflow.model.*;
public class WorkflowWorker {
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