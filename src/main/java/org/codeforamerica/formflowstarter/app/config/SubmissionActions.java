package org.codeforamerica.formflowstarter.app.config;

import java.util.ArrayList;
import java.util.Map;
import org.codeforamerica.formflowstarter.app.data.Submission;

public class SubmissionActions {
    public static void clearIncomeAmountsBeforeSaving(Submission submission, String uuid) {
        //grab the current household members incometypes
        var entryByUuid = Submission.getSubflowEntryByUuid("income", uuid, submission);
        var incomeAmounts = entryByUuid.entrySet().stream()
                .filter(e -> e.getKey().matches(".*Amount$")).map(Map.Entry::getKey).toList();

        var incomeTypesArray = (ArrayList<String>) entryByUuid.get("incomeTypes[]");

        incomeAmounts.stream().forEach(incomeAmount -> {
            if (!incomeTypesArray.contains(incomeAmount.replace("Amount", ""))) {
                entryByUuid.remove(incomeAmount);
            }
        });
    }
}
