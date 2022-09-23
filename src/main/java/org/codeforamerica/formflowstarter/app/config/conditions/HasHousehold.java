package org.codeforamerica.formflowstarter.app.config.conditions;

import org.codeforamerica.formflowstarter.app.config.Condition;
import org.codeforamerica.formflowstarter.app.data.Submission;

public class HasHousehold extends Condition {

    public Boolean runCondition(Submission submission) {
        var inputData = submission.getInputData();
        if (inputData.containsKey("hasHousehold")) {
            return inputData.get("hasHousehold").equals("true");
        }
        return false;
    }
}
